import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWorkingHours, NewWorkingHours } from '../working-hours.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWorkingHours for edit and NewWorkingHoursFormGroupInput for create.
 */
type WorkingHoursFormGroupInput = IWorkingHours | PartialWithRequiredKeyOf<NewWorkingHours>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IWorkingHours | NewWorkingHours> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

type WorkingHoursFormRawValue = FormValueOf<IWorkingHours>;

type NewWorkingHoursFormRawValue = FormValueOf<NewWorkingHours>;

type WorkingHoursFormDefaults = Pick<NewWorkingHours, 'id' | 'startTime' | 'endTime'>;

type WorkingHoursFormGroupContent = {
  id: FormControl<WorkingHoursFormRawValue['id'] | NewWorkingHours['id']>;
  dayOfWeek: FormControl<WorkingHoursFormRawValue['dayOfWeek']>;
  startTime: FormControl<WorkingHoursFormRawValue['startTime']>;
  endTime: FormControl<WorkingHoursFormRawValue['endTime']>;
  employee: FormControl<WorkingHoursFormRawValue['employee']>;
};

export type WorkingHoursFormGroup = FormGroup<WorkingHoursFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WorkingHoursFormService {
  createWorkingHoursFormGroup(workingHours: WorkingHoursFormGroupInput = { id: null }): WorkingHoursFormGroup {
    const workingHoursRawValue = this.convertWorkingHoursToWorkingHoursRawValue({
      ...this.getFormDefaults(),
      ...workingHours,
    });
    return new FormGroup<WorkingHoursFormGroupContent>({
      id: new FormControl(
        { value: workingHoursRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dayOfWeek: new FormControl(workingHoursRawValue.dayOfWeek, {
        validators: [Validators.required],
      }),
      startTime: new FormControl(workingHoursRawValue.startTime, {
        validators: [Validators.required],
      }),
      endTime: new FormControl(workingHoursRawValue.endTime, {
        validators: [Validators.required],
      }),
      employee: new FormControl(workingHoursRawValue.employee),
    });
  }

  getWorkingHours(form: WorkingHoursFormGroup): IWorkingHours | NewWorkingHours {
    return this.convertWorkingHoursRawValueToWorkingHours(form.getRawValue() as WorkingHoursFormRawValue | NewWorkingHoursFormRawValue);
  }

  resetForm(form: WorkingHoursFormGroup, workingHours: WorkingHoursFormGroupInput): void {
    const workingHoursRawValue = this.convertWorkingHoursToWorkingHoursRawValue({ ...this.getFormDefaults(), ...workingHours });
    form.reset(
      {
        ...workingHoursRawValue,
        id: { value: workingHoursRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WorkingHoursFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startTime: currentTime,
      endTime: currentTime,
    };
  }

  private convertWorkingHoursRawValueToWorkingHours(
    rawWorkingHours: WorkingHoursFormRawValue | NewWorkingHoursFormRawValue,
  ): IWorkingHours | NewWorkingHours {
    return {
      ...rawWorkingHours,
      startTime: dayjs(rawWorkingHours.startTime, DATE_TIME_FORMAT),
      endTime: dayjs(rawWorkingHours.endTime, DATE_TIME_FORMAT),
    };
  }

  private convertWorkingHoursToWorkingHoursRawValue(
    workingHours: IWorkingHours | (Partial<NewWorkingHours> & WorkingHoursFormDefaults),
  ): WorkingHoursFormRawValue | PartialWithRequiredKeyOf<NewWorkingHoursFormRawValue> {
    return {
      ...workingHours,
      startTime: workingHours.startTime ? workingHours.startTime.format(DATE_TIME_FORMAT) : undefined,
      endTime: workingHours.endTime ? workingHours.endTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
