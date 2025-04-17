import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ServiceProviderComponent } from './list/service-provider.component';
import { ServiceProviderDetailComponent } from './detail/service-provider-detail.component';
import { ServiceProviderUpdateComponent } from './update/service-provider-update.component';
import ServiceProviderResolve from './route/service-provider-routing-resolve.service';

const serviceProviderRoute: Routes = [
  {
    path: '',
    component: ServiceProviderComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ServiceProviderDetailComponent,
    resolve: {
      serviceProvider: ServiceProviderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ServiceProviderUpdateComponent,
    resolve: {
      serviceProvider: ServiceProviderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ServiceProviderUpdateComponent,
    resolve: {
      serviceProvider: ServiceProviderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default serviceProviderRoute;
