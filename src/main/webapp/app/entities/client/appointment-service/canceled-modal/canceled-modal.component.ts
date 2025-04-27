import { Component, inject } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgClass, NgIf } from '@angular/common';
import TranslateDirective from '../../../../shared/language/translate.directive';
import { AppointmentService } from '../../../appointment/service/appointment.service';
import SharedModule from '../../../../shared/shared.module';

@Component({
  templateUrl: 'canceled-modal.component.html',
  styleUrl: 'canceled-modal.component.scss',
  imports: [NgIf, NgClass, FormsModule, ReactiveFormsModule, TranslateDirective, SharedModule],
  standalone: true,
})
export class CanceledModalComponent {
  protected activeModal = inject(NgbActiveModal);
  protected appointmentService = inject(AppointmentService);
  appointment?: any;
  constructor() {}

  cancel(): void {
    this.activeModal.dismiss();
  }
  confirmBooking() {
    if (this.appointment) {
      this.appointment.status = 'CANCELLED';
      this.appointmentService.update(this.appointment).subscribe(
        updatedAppointment => {
          console.log('Appointment canceled successfully:', updatedAppointment);
          this.activeModal.close(); // Close the modal after confirming
        },
        error => {
          console.error('Error canceling appointment:', error);
        },
      );
    }
  }
  protected readonly close = close;
}
