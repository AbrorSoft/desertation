import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRoom, NewRoom } from '../room.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRoom for edit and NewRoomFormGroupInput for create.
 */
type RoomFormGroupInput = IRoom | PartialWithRequiredKeyOf<NewRoom>;

type RoomFormDefaults = Pick<NewRoom, 'id'>;

type RoomFormGroupContent = {
  id: FormControl<IRoom['id'] | NewRoom['id']>;
  roomNumber: FormControl<IRoom['roomNumber']>;
  description: FormControl<IRoom['description']>;
  serviceProvider: FormControl<IRoom['serviceProvider']>;
};

export type RoomFormGroup = FormGroup<RoomFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RoomFormService {
  createRoomFormGroup(room: RoomFormGroupInput = { id: null }): RoomFormGroup {
    const roomRawValue = {
      ...this.getFormDefaults(),
      ...room,
    };
    return new FormGroup<RoomFormGroupContent>({
      id: new FormControl(
        { value: roomRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      roomNumber: new FormControl(roomRawValue.roomNumber, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(roomRawValue.description),
      serviceProvider: new FormControl(roomRawValue.serviceProvider),
    });
  }

  getRoom(form: RoomFormGroup): IRoom | NewRoom {
    return form.getRawValue() as IRoom | NewRoom;
  }

  resetForm(form: RoomFormGroup, room: RoomFormGroupInput): void {
    const roomRawValue = { ...this.getFormDefaults(), ...room };
    form.reset(
      {
        ...roomRawValue,
        id: { value: roomRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RoomFormDefaults {
    return {
      id: null,
    };
  }
}
