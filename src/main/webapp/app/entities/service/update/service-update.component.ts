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
import { ServiceService } from '../service/service.service';
import { IService } from '../service.model';
import { ServiceFormService, ServiceFormGroup } from './service-form.service';

@Component({
  standalone: true,
  selector: 'jhi-service-update',
  templateUrl: './service-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ServiceUpdateComponent implements OnInit {
  isSaving = false;
  service: IService | null = null;

  serviceProvidersSharedCollection: IServiceProvider[] = [];
  employeesSharedCollection: IEmployee[] = [];

  protected serviceService = inject(ServiceService);
  protected serviceFormService = inject(ServiceFormService);
  protected serviceProviderService = inject(ServiceProviderService);
  protected employeeService = inject(EmployeeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ServiceFormGroup = this.serviceFormService.createServiceFormGroup();

  compareServiceProvider = (o1: IServiceProvider | null, o2: IServiceProvider | null): boolean =>
    this.serviceProviderService.compareServiceProvider(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ service }) => {
      this.service = service;
      if (service) {
        this.updateForm(service);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const service = this.serviceFormService.getService(this.editForm);
    if (service.id !== null) {
      this.subscribeToSaveResponse(this.serviceService.update(service));
    } else {
      this.subscribeToSaveResponse(this.serviceService.create(service));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IService>>): void {
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

  protected updateForm(service: IService): void {
    this.service = service;
    this.serviceFormService.resetForm(this.editForm, service);

    this.serviceProvidersSharedCollection = this.serviceProviderService.addServiceProviderToCollectionIfMissing<IServiceProvider>(
      this.serviceProvidersSharedCollection,
      service.serviceProvider,
    );
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      ...(service.employees ?? []),
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
            this.service?.serviceProvider,
          ),
        ),
      )
      .subscribe((serviceProviders: IServiceProvider[]) => (this.serviceProvidersSharedCollection = serviceProviders));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, ...(this.service?.employees ?? [])),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
