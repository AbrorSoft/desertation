import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AppointmentServiceComponent } from './appointment-service.component';



const appointmentService: Routes = [
  {
    path: '',
    component: AppointmentServiceComponent,
    // data: {
    //   defaultSort: 'id,' + ASC, if sort needed
    // },
    canActivate: [UserRouteAccessService],
  },
  // {
  //   path: ':id/view',
  //   component: RoomDetailComponent,
  //   resolve: {
  //     room: RoomResolve,
  //   },
  //   canActivate: [UserRouteAccessService],
  // },
  // {
  //   path: 'new',
  //   component: RoomUpdateComponent,
  //   resolve: {
  //     room: RoomResolve,
  //   },
  //   canActivate: [UserRouteAccessService],
  // },

];

export default appointmentService;
