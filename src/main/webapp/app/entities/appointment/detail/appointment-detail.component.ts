import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAppointment } from '../appointment.model';

@Component({
  standalone: true,
  selector: 'jhi-appointment-detail',
  templateUrl: './appointment-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AppointmentDetailComponent {
  @Input() appointment: IAppointment | null = null;

  previousState(): void {
    window.history.back();
  }
}
