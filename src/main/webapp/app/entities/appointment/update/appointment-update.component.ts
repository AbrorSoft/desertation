import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IServiceProvider } from 'app/entities/service-provider/service-provider.model';
import { ServiceProviderService } from 'app/entities/service-provider/service/service-provider.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IServiceForCustomer } from 'app/entities/service-for-customer/service-for-customer.model';
import { ServiceForCustomerService } from 'app/entities/service-for-customer/service/service-for-customer.service';
import { IRoom } from 'app/entities/room/room.model';
import { RoomService } from 'app/entities/room/service/room.service';
import { AppointmentStatus } from 'app/entities/enumerations/appointment-status.model';
import { AppointmentService } from '../service/appointment.service';
import { IAppointment } from '../appointment.model';
import { AppointmentFormService, AppointmentFormGroup } from './appointment-form.service';

@Component({
  standalone: true,
  selector: 'jhi-appointment-update',
  templateUrl: './appointment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AppointmentUpdateComponent implements OnInit {
  isSaving = false;
  appointment: IAppointment | null = null;
  appointmentStatusValues = Object.keys(AppointmentStatus);

  serviceProvidersSharedCollection: IServiceProvider[] = [];
  employeesSharedCollection: IEmployee[] = [];
  serviceForCustomersSharedCollection: IServiceForCustomer[] = [];
  roomsSharedCollection: IRoom[] = [];

  protected appointmentService = inject(AppointmentService);
  protected appointmentFormService = inject(AppointmentFormService);
  protected serviceProviderService = inject(ServiceProviderService);
  protected employeeService = inject(EmployeeService);
  protected serviceForCustomerService = inject(ServiceForCustomerService);
  protected roomService = inject(RoomService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AppointmentFormGroup = this.appointmentFormService.createAppointmentFormGroup();

  compareServiceProvider = (o1: IServiceProvider | null, o2: IServiceProvider | null): boolean =>
    this.serviceProviderService.compareServiceProvider(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareServiceForCustomer = (o1: IServiceForCustomer | null, o2: IServiceForCustomer | null): boolean =>
    this.serviceForCustomerService.compareServiceForCustomer(o1, o2);

  compareRoom = (o1: IRoom | null, o2: IRoom | null): boolean => this.roomService.compareRoom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appointment }) => {
      this.appointment = appointment;
      if (appointment) {
        this.updateForm(appointment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appointment = this.appointmentFormService.getAppointment(this.editForm);
    if (appointment.id !== null) {
      this.subscribeToSaveResponse(this.appointmentService.update(appointment));
    } else {
      this.subscribeToSaveResponse(this.appointmentService.create(appointment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppointment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(appointment: IAppointment): void {
    this.appointment = appointment;
    this.appointmentFormService.resetForm(this.editForm, appointment);

    this.serviceProvidersSharedCollection = this.serviceProviderService.addServiceProviderToCollectionIfMissing<IServiceProvider>(
      this.serviceProvidersSharedCollection,
      appointment.serviceProvider,
    );
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      appointment.employee,
    );
    this.serviceForCustomersSharedCollection =
      this.serviceForCustomerService.addServiceForCustomerToCollectionIfMissing<IServiceForCustomer>(
        this.serviceForCustomersSharedCollection,
        appointment.serviceForCustomer,
      );
    this.roomsSharedCollection = this.roomService.addRoomToCollectionIfMissing<IRoom>(this.roomsSharedCollection, appointment.room);
  }

  protected loadRelationshipsOptions(): void {
    this.serviceProviderService
      .query()
      .pipe(map((res: HttpResponse<IServiceProvider[]>) => res.body ?? []))
      .pipe(
        map((serviceProviders: IServiceProvider[]) =>
          this.serviceProviderService.addServiceProviderToCollectionIfMissing<IServiceProvider>(
            serviceProviders,
            this.appointment?.serviceProvider,
          ),
        ),
      )
      .subscribe((serviceProviders: IServiceProvider[]) => (this.serviceProvidersSharedCollection = serviceProviders));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.appointment?.employee),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.serviceForCustomerService
      .query()
      .pipe(map((res: HttpResponse<IServiceForCustomer[]>) => res.body ?? []))
      .pipe(
        map((serviceForCustomers: IServiceForCustomer[]) =>
          this.serviceForCustomerService.addServiceForCustomerToCollectionIfMissing<IServiceForCustomer>(
            serviceForCustomers,
            this.appointment?.serviceForCustomer,
          ),
        ),
      )
      .subscribe((serviceForCustomers: IServiceForCustomer[]) => (this.serviceForCustomersSharedCollection = serviceForCustomers));

    this.roomService
      .query()
      .pipe(map((res: HttpResponse<IRoom[]>) => res.body ?? []))
      .pipe(map((rooms: IRoom[]) => this.roomService.addRoomToCollectionIfMissing<IRoom>(rooms, this.appointment?.room)))
      .subscribe((rooms: IRoom[]) => (this.roomsSharedCollection = rooms));
  }
}
