import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IWorkingHours } from '../working-hours.model';

@Component({
  standalone: true,
  selector: 'jhi-working-hours-detail',
  templateUrl: './working-hours-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class WorkingHoursDetailComponent {
  @Input() workingHours: IWorkingHours | null = null;

  previousState(): void {
    window.history.back();
  }
}
