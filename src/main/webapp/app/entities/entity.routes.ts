import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'appointmentApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'service-provider',
    data: { pageTitle: 'appointmentApp.serviceProvider.home.title' },
    loadChildren: () => import('./service-provider/service-provider.routes'),
  },
  {
    path: 'employee',
    data: { pageTitle: 'appointmentApp.employee.home.title' },
    loadChildren: () => import('./employee/employee.routes'),
  },
  {
    path: 'room',
    data: { pageTitle: 'appointmentApp.room.home.title' },
    loadChildren: () => import('./room/room.routes'),
  },
  {
    path: 'service',
    data: { pageTitle: 'appointmentApp.service.home.title' },
    loadChildren: () => import('./service/service.routes'),
  },
  {
    path: 'working-hours',
    data: { pageTitle: 'appointmentApp.workingHours.home.title' },
    loadChildren: () => import('./working-hours/working-hours.routes'),
  },
  {
    path: 'appointment',
    data: { pageTitle: 'appointmentApp.appointment.home.title' },
    loadChildren: () => import('./appointment/appointment.routes'),
  },
  {
    path: 'service-for-customer',
    data: { pageTitle: 'appointmentApp.serviceForCustomer.home.title' },
    loadChildren: () => import('./service-for-customer/service-for-customer.routes'),
  },
  {
    path: 'appointment-service',
    data: { pageTitle: 'appointment-service' },
    loadChildren: () => import('./client/appointment-service/appointment-service.routes'),
  },
  {
    path: 'book-appointment',
    data: { pageTitle: 'book-appointment' },
    loadChildren: () => import('./client/book-appointment/book-appointment.routes'),
  },
];

export default routes;
