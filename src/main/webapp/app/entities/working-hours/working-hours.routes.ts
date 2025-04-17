import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { WorkingHoursComponent } from './list/working-hours.component';
import { WorkingHoursDetailComponent } from './detail/working-hours-detail.component';
import { WorkingHoursUpdateComponent } from './update/working-hours-update.component';
import WorkingHoursResolve from './route/working-hours-routing-resolve.service';

const workingHoursRoute: Routes = [
  {
    path: '',
    component: WorkingHoursComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WorkingHoursDetailComponent,
    resolve: {
      workingHours: WorkingHoursResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WorkingHoursUpdateComponent,
    resolve: {
      workingHours: WorkingHoursResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WorkingHoursUpdateComponent,
    resolve: {
      workingHours: WorkingHoursResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default workingHoursRoute;
