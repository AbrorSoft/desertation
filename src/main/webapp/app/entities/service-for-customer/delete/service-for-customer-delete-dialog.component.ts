import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IServiceForCustomer } from '../service-for-customer.model';
import { ServiceForCustomerService } from '../service/service-for-customer.service';

@Component({
  standalone: true,
  templateUrl: './service-for-customer-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ServiceForCustomerDeleteDialogComponent {
  serviceForCustomer?: IServiceForCustomer;

  protected serviceForCustomerService = inject(ServiceForCustomerService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serviceForCustomerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
