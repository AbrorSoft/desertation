import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IRoom } from '../room.model';

@Component({
  standalone: true,
  selector: 'jhi-room-detail',
  templateUrl: './room-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RoomDetailComponent {
  @Input() room: IRoom | null = null;

  previousState(): void {
    window.history.back();
  }
}
