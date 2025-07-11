import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ServiceProviderType } from 'app/entities/enumerations/service-provider-type.model';
import { IServiceProvider } from '../service-provider.model';
import { ServiceProviderService } from '../service/service-provider.service';
import { ServiceProviderFormService, ServiceProviderFormGroup } from './service-provider-form.service';
import { DataUtils, FileLoadError } from '../../../core/util/data-util.service';
import { EventManager, EventWithContent } from '../../../core/util/event-manager.service';
import { AlertError } from '../../../shared/alert/alert-error.model';

@Component({
  standalone: true,
  selector: 'jhi-service-provider-update',
  templateUrl: './service-provider-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ServiceProviderUpdateComponent implements OnInit {
  isSaving = false;
  serviceProvider: IServiceProvider | null = null;
  serviceProviderTypeValues = Object.keys(ServiceProviderType);
  protected dataUtils = inject(DataUtils);
  protected serviceProviderService = inject(ServiceProviderService);
  protected serviceProviderFormService = inject(ServiceProviderFormService);
  protected activatedRoute = inject(ActivatedRoute);
  protected eventManager = inject(EventManager);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ServiceProviderFormGroup = this.serviceProviderFormService.createServiceProviderFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceProvider }) => {
      this.serviceProvider = serviceProvider;
      if (serviceProvider) {
        this.updateForm(serviceProvider);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const serviceProvider = this.serviceProviderFormService.getServiceProvider(this.editForm);
    if (serviceProvider.id !== null) {
      this.subscribeToSaveResponse(this.serviceProviderService.update(serviceProvider));
    } else {
      this.subscribeToSaveResponse(this.serviceProviderService.create(serviceProvider));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceProvider>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }
  setFileData(event: any): void {
    console.log(event.target?.files![0]);
    this.dataUtils.loadFileToForm(event, this.editForm, 'imageFile', true).subscribe({
      next: () => this.editForm.patchValue({ imageKey: event.target?.files![0].name }),
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('auctionApp.error', { ...err, key: `error.file.${err.key}` })),
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

  protected updateForm(serviceProvider: IServiceProvider): void {
    this.serviceProvider = serviceProvider;
    this.serviceProviderFormService.resetForm(this.editForm, serviceProvider);
  }
}
