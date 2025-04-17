import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAppointment, NewAppointment } from '../appointment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAppointment for edit and NewAppointmentFormGroupInput for create.
 */
type AppointmentFormGroupInput = IAppointment | PartialWithRequiredKeyOf<NewAppointment>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAppointment | NewAppointment> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

type AppointmentFormRawValue = FormValueOf<IAppointment>;

type NewAppointmentFormRawValue = FormValueOf<NewAppointment>;

type AppointmentFormDefaults = Pick<NewAppointment, 'id' | 'startTime' | 'endTime'>;

type AppointmentFormGroupContent = {
  id: FormControl<AppointmentFormRawValue['id'] | NewAppointment['id']>;
  date: FormControl<AppointmentFormRawValue['date']>;
  startTime: FormControl<AppointmentFormRawValue['startTime']>;
  endTime: FormControl<AppointmentFormRawValue['endTime']>;
  status: FormControl<AppointmentFormRawValue['status']>;
  serviceProvider: FormControl<AppointmentFormRawValue['serviceProvider']>;
  employee: FormControl<AppointmentFormRawValue['employee']>;
  serviceForCustomer: FormControl<AppointmentFormRawValue['serviceForCustomer']>;
  room: FormControl<AppointmentFormRawValue['room']>;
};

export type AppointmentFormGroup = FormGroup<AppointmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AppointmentFormService {
  createAppointmentFormGroup(appointment: AppointmentFormGroupInput = { id: null }): AppointmentFormGroup {
    const appointmentRawValue = this.convertAppointmentToAppointmentRawValue({
      ...this.getFormDefaults(),
      ...appointment,
    });
    return new FormGroup<AppointmentFormGroupContent>({
      id: new FormControl(
        { value: appointmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      date: new FormControl(appointmentRawValue.date, {
        validators: [Validators.required],
      }),
      startTime: new FormControl(appointmentRawValue.startTime, {
        validators: [Validators.required],
      }),
      endTime: new FormControl(appointmentRawValue.endTime, {
        validators: [Validators.required],
      }),
      status: new FormControl(appointmentRawValue.status, {
        validators: [Validators.required],
      }),
      serviceProvider: new FormControl(appointmentRawValue.serviceProvider),
      employee: new FormControl(appointmentRawValue.employee),
      serviceForCustomer: new FormControl(appointmentRawValue.serviceForCustomer),
      room: new FormControl(appointmentRawValue.room),
    });
  }

  getAppointment(form: AppointmentFormGroup): IAppointment | NewAppointment {
    return this.convertAppointmentRawValueToAppointment(form.getRawValue() as AppointmentFormRawValue | NewAppointmentFormRawValue);
  }

  resetForm(form: AppointmentFormGroup, appointment: AppointmentFormGroupInput): void {
    const appointmentRawValue = this.convertAppointmentToAppointmentRawValue({ ...this.getFormDefaults(), ...appointment });
    form.reset(
      {
        ...appointmentRawValue,
        id: { value: appointmentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AppointmentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startTime: currentTime,
      endTime: currentTime,
    };
  }

  private convertAppointmentRawValueToAppointment(
    rawAppointment: AppointmentFormRawValue | NewAppointmentFormRawValue,
  ): IAppointment | NewAppointment {
    return {
      ...rawAppointment,
      startTime: dayjs(rawAppointment.startTime, DATE_TIME_FORMAT),
      endTime: dayjs(rawAppointment.endTime, DATE_TIME_FORMAT),
    };
  }

  private convertAppointmentToAppointmentRawValue(
    appointment: IAppointment | (Partial<NewAppointment> & AppointmentFormDefaults),
  ): AppointmentFormRawValue | PartialWithRequiredKeyOf<NewAppointmentFormRawValue> {
    return {
      ...appointment,
      startTime: appointment.startTime ? appointment.startTime.format(DATE_TIME_FORMAT) : undefined,
      endTime: appointment.endTime ? appointment.endTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
