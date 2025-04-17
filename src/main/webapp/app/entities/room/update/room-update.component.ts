import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IServiceProvider } from 'app/entities/service-provider/service-provider.model';
import { ServiceProviderService } from 'app/entities/service-provider/service/service-provider.service';
import { IRoom } from '../room.model';
import { RoomService } from '../service/room.service';
import { RoomFormService, RoomFormGroup } from './room-form.service';

@Component({
  standalone: true,
  selector: 'jhi-room-update',
  templateUrl: './room-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RoomUpdateComponent implements OnInit {
  isSaving = false;
  room: IRoom | null = null;

  serviceProvidersSharedCollection: IServiceProvider[] = [];

  protected roomService = inject(RoomService);
  protected roomFormService = inject(RoomFormService);
  protected serviceProviderService = inject(ServiceProviderService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RoomFormGroup = this.roomFormService.createRoomFormGroup();

  compareServiceProvider = (o1: IServiceProvider | null, o2: IServiceProvider | null): boolean =>
    this.serviceProviderService.compareServiceProvider(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ room }) => {
      this.room = room;
      if (room) {
        this.updateForm(room);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const room = this.roomFormService.getRoom(this.editForm);
    if (room.id !== null) {
      this.subscribeToSaveResponse(this.roomService.update(room));
    } else {
      this.subscribeToSaveResponse(this.roomService.create(room));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoom>>): void {
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

  protected updateForm(room: IRoom): void {
    this.room = room;
    this.roomFormService.resetForm(this.editForm, room);

    this.serviceProvidersSharedCollection = this.serviceProviderService.addServiceProviderToCollectionIfMissing<IServiceProvider>(
      this.serviceProvidersSharedCollection,
      room.serviceProvider,
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
            this.room?.serviceProvider,
          ),
        ),
      )
      .subscribe((serviceProviders: IServiceProvider[]) => (this.serviceProvidersSharedCollection = serviceProviders));
  }
}
