import { Component, inject } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { NgClass, NgIf } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import TranslateDirective from '../../../../shared/language/translate.directive';
import { DATE_TIME_FORMAT } from '../../../../config/input.constants';
import dayjs from 'dayjs/esm';
import { AppointmentService } from '../../../appointment/service/appointment.service';
import { HttpClient } from '@angular/common/http';
import { Account } from '../../../../core/auth/account.model';
import { AccountService } from '../../../../core/auth/account.service';
import SharedModule from '../../../../shared/shared.module';

@Component({
  templateUrl: 'booked-modal.component.html',
  styleUrl: 'booked-modal.component.scss',
  imports: [NgIf, NgClass, FormsModule, ReactiveFormsModule, TranslateDirective, SharedModule],
  standalone: true,
})
export class BookedModalComponent {
  protected activeModal = inject(NgbActiveModal);
  protected appointmentService = inject(AppointmentService);
  protected http = inject(HttpClient);
  protected accountService = inject(AccountService);
  booked?: any;

  // Set your "disabled" day — e.g., Sunday (0 = Sunday, 6 = Saturday)
  disabledDay = 0;

  minDate?: string;
  maxDate?: string;

  editForm = new FormGroup({
    startTime: new FormControl('', [Validators.required, this.timeWindowValidator.bind(this)]),
  });

  constructor() {}

  timeWindowValidator(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;

    // Parse as UTC to avoid timezone issues
    const date = new Date(control.value);
    const utcHours = date.getUTCHours();
    const utcDay = date.getUTCDay();

    if (utcDay === this.disabledDay && (utcHours < 8 || utcHours >= 18)) {
      return { timeOutOfRange: true };
    }
    return null;
  }

  // Call this method on input change
  onDateTimeChange(target: any) {
    if (!target.value) {
      this.minDate = undefined;
      this.maxDate = undefined;
      return;
    }

    const date = new Date(target.value);
    const utcDay = date.getUTCDay();

    if (utcDay === this.disabledDay) {
      const dateStr = target.value.split('T')[0];
      this.minDate = `${dateStr}T08:00`;
      this.maxDate = `${dateStr}T18:00`;

      // Adjust time if outside range
      const currentHours = date.getUTCHours();
      if (currentHours < 8 || currentHours >= 18) {
        const correctedDate = new Date(date);
        correctedDate.setUTCHours(currentHours < 8 ? 8 : 17, 59);
        target.value = correctedDate.toISOString().slice(0, 16);
        this.editForm.get('startTime')?.setValue(target.value);
      }
    } else {
      this.minDate = undefined;
      this.maxDate = undefined;
    }

    this.editForm.get('startTime')?.updateValueAndValidity();
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  getInitials(name: string | undefined): string {
    if (!name) return '';
    return name
      .split(' ')
      .map(n => n[0])
      .join('')
      .toUpperCase();
  }

  confirmBooking() {
    let startDate: any = this.editForm.get('startTime')?.value;

    let endLocal = dayjs(startDate).add(180, 'minute').format('YYYY-MM-DD[T]HH:mm');
    if (!startDate || startDate.length > 16) {
      console.log('FALSE');
    } else {
      startDate = dayjs(startDate).toISOString();
      endLocal = dayjs(endLocal).toISOString();
      console.log({ startDate, endLocal });
      this.booked = {
        ...this.booked,
        startTime: startDate,
        endDate: endLocal,
        status: 'BOOKED',
        id: null,
        user: this.accountService.userIdentity(),
      };
      this.http.post(this.appointmentService.resourceUrl, this.booked).subscribe();
      this.cancel();
    }
  }

  protected readonly close = close;
}
