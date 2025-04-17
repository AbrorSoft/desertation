import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWorkingHours } from '../working-hours.model';
import { WorkingHoursService } from '../service/working-hours.service';

@Component({
  standalone: true,
  templateUrl: './working-hours-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WorkingHoursDeleteDialogComponent {
  workingHours?: IWorkingHours;

  protected workingHoursService = inject(WorkingHoursService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workingHoursService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
