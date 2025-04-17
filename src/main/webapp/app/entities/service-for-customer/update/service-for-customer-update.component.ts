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
import { ServiceForCustomerService } from '../service/service-for-customer.service';
import { IServiceForCustomer } from '../service-for-customer.model';
import { ServiceForCustomerFormService, ServiceForCustomerFormGroup } from './service-for-customer-form.service';

@Component({
  standalone: true,
  selector: 'jhi-service-for-customer-update',
  templateUrl: './service-for-customer-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ServiceForCustomerUpdateComponent implements OnInit {
  isSaving = false;
  serviceForCustomer: IServiceForCustomer | null = null;

  serviceProvidersSharedCollection: IServiceProvider[] = [];
  employeesSharedCollection: IEmployee[] = [];

  protected serviceForCustomerService = inject(ServiceForCustomerService);
  protected serviceForCustomerFormService = inject(ServiceForCustomerFormService);
  protected serviceProviderService = inject(ServiceProviderService);
  protected employeeService = inject(EmployeeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ServiceForCustomerFormGroup = this.serviceForCustomerFormService.createServiceForCustomerFormGroup();

  compareServiceProvider = (o1: IServiceProvider | null, o2: IServiceProvider | null): boolean =>
    this.serviceProviderService.compareServiceProvider(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceForCustomer }) => {
      this.serviceForCustomer = serviceForCustomer;
      if (serviceForCustomer) {
        this.updateForm(serviceForCustomer);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const serviceForCustomer = this.serviceForCustomerFormService.getServiceForCustomer(this.editForm);
    if (serviceForCustomer.id !== null) {
      this.subscribeToSaveResponse(this.serviceForCustomerService.update(serviceForCustomer));
    } else {
      this.subscribeToSaveResponse(this.serviceForCustomerService.create(serviceForCustomer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceForCustomer>>): void {
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

  protected updateForm(serviceForCustomer: IServiceForCustomer): void {
    this.serviceForCustomer = serviceForCustomer;
    this.serviceForCustomerFormService.resetForm(this.editForm, serviceForCustomer);

    this.serviceProvidersSharedCollection = this.serviceProviderService.addServiceProviderToCollectionIfMissing<IServiceProvider>(
      this.serviceProvidersSharedCollection,
      serviceForCustomer.serviceProvider,
    );
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      ...(serviceForCustomer.employees ?? []),
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
            this.serviceForCustomer?.serviceProvider,
          ),
        ),
      )
      .subscribe((serviceProviders: IServiceProvider[]) => (this.serviceProvidersSharedCollection = serviceProviders));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, ...(this.serviceForCustomer?.employees ?? [])),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
