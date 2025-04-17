import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ServiceForCustomerComponent } from './list/service-for-customer.component';
import { ServiceForCustomerDetailComponent } from './detail/service-for-customer-detail.component';
import { ServiceForCustomerUpdateComponent } from './update/service-for-customer-update.component';
import ServiceForCustomerResolve from './route/service-for-customer-routing-resolve.service';

const serviceForCustomerRoute: Routes = [
  {
    path: '',
    component: ServiceForCustomerComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ServiceForCustomerDetailComponent,
    resolve: {
      serviceForCustomer: ServiceForCustomerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ServiceForCustomerUpdateComponent,
    resolve: {
      serviceForCustomer: ServiceForCustomerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ServiceForCustomerUpdateComponent,
    resolve: {
      serviceForCustomer: ServiceForCustomerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default serviceForCustomerRoute;
