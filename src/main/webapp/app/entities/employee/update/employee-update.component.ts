import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IServiceProvider } from 'app/entities/service-provider/service-provider.model';
import { ServiceProviderService } from 'app/entities/service-provider/service/service-provider.service';
import { IServiceForCustomer } from 'app/entities/service-for-customer/service-for-customer.model';
import { ServiceForCustomerService } from 'app/entities/service-for-customer/service/service-for-customer.service';
import { IService } from 'app/entities/service/service.model';
import { ServiceService } from 'app/entities/service/service/service.service';
import { EmployeeService } from '../service/employee.service';
import { IEmployee } from '../employee.model';
import { EmployeeFormService, EmployeeFormGroup } from './employee-form.service';
import { CustomMultiSelectComponent } from '../../../shared/custom-multi-select/custom-multi-select.component';

@Component({
  standalone: true,
  selector: 'jhi-employee-update',
  templateUrl: './employee-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, CustomMultiSelectComponent],
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;
  employee: IEmployee | null = null;

  serviceProvidersSharedCollection: IServiceProvider[] = [];
  serviceForCustomersSharedCollection: IServiceForCustomer[] = [];
  servicesSharedCollection: IService[] = [];

  protected employeeService = inject(EmployeeService);
  protected employeeFormService = inject(EmployeeFormService);
  protected serviceProviderService = inject(ServiceProviderService);
  protected serviceForCustomerService = inject(ServiceForCustomerService);
  protected serviceService = inject(ServiceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmployeeFormGroup = this.employeeFormService.createEmployeeFormGroup();

  compareServiceProvider = (o1: IServiceProvider | null, o2: IServiceProvider | null): boolean =>
    this.serviceProviderService.compareServiceProvider(o1, o2);

  compareServiceForCustomer = (o1: IServiceForCustomer | null, o2: IServiceForCustomer | null): boolean =>
    this.serviceForCustomerService.compareServiceForCustomer(o1, o2);

  compareService = (o1: IService | null, o2: IService | null): boolean => this.serviceService.compareService(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.employee = employee;
      if (employee) {
        this.updateForm(employee);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.employeeFormService.getEmployee(this.editForm);
    if (employee.id !== null) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
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

  protected updateForm(employee: IEmployee): void {
    this.employee = employee;
    this.employeeFormService.resetForm(this.editForm, employee);

    this.serviceProvidersSharedCollection = this.serviceProviderService.addServiceProviderToCollectionIfMissing<IServiceProvider>(
      this.serviceProvidersSharedCollection,
      employee.serviceProvider,
    );
    this.serviceForCustomersSharedCollection =
      this.serviceForCustomerService.addServiceForCustomerToCollectionIfMissing<IServiceForCustomer>(
        this.serviceForCustomersSharedCollection,
        ...(employee.servicesForCustomers ?? []),
      );
    this.servicesSharedCollection = this.serviceService.addServiceToCollectionIfMissing<IService>(
      this.servicesSharedCollection,
      ...(employee.services ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.serviceProviderService
      .query()
      .pipe(map((res: HttpResponse<IServiceProvider[]>) => res.body ?? []))
      .pipe(
        map((serviceProviders: IServiceProvider[]) =>
          this.serviceProviderService.addServiceProviderToCollectionIfMissing<IServiceProvider>(
            serviceProviders,
            this.employee?.serviceProvider,
          ),
        ),
      )
      .subscribe((serviceProviders: IServiceProvider[]) => (this.serviceProvidersSharedCollection = serviceProviders));

    this.serviceForCustomerService
      .query()
      .pipe(map((res: HttpResponse<IServiceForCustomer[]>) => res.body ?? []))
      .pipe(
        map((serviceForCustomers: IServiceForCustomer[]) =>
          this.serviceForCustomerService.addServiceForCustomerToCollectionIfMissing<IServiceForCustomer>(
            serviceForCustomers,
            ...(this.employee?.servicesForCustomers ?? []),
          ),
        ),
      )
      .subscribe((serviceForCustomers: IServiceForCustomer[]) => (this.serviceForCustomersSharedCollection = serviceForCustomers));

    this.serviceService
      .query()
      .pipe(map((res: HttpResponse<IService[]>) => res.body ?? []))
      .pipe(
        map((services: IService[]) =>
          this.serviceService.addServiceToCollectionIfMissing<IService>(services, ...(this.employee?.services ?? [])),
        ),
      )
      .subscribe((services: IService[]) => (this.servicesSharedCollection = services));
  }
}
