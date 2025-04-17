import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IServiceForCustomer } from '../service-for-customer.model';

@Component({
  standalone: true,
  selector: 'jhi-service-for-customer-detail',
  templateUrl: './service-for-customer-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ServiceForCustomerDetailComponent {
  @Input() serviceForCustomer: IServiceForCustomer | null = null;

  previousState(): void {
    window.history.back();
  }
}
