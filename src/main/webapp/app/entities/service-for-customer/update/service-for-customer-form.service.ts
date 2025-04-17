import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IServiceForCustomer, NewServiceForCustomer } from '../service-for-customer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IServiceForCustomer for edit and NewServiceForCustomerFormGroupInput for create.
 */
type ServiceForCustomerFormGroupInput = IServiceForCustomer | PartialWithRequiredKeyOf<NewServiceForCustomer>;

type ServiceForCustomerFormDefaults = Pick<NewServiceForCustomer, 'id' | 'employees'>;

type ServiceForCustomerFormGroupContent = {
  id: FormControl<IServiceForCustomer['id'] | NewServiceForCustomer['id']>;
  name: FormControl<IServiceForCustomer['name']>;
  description: FormControl<IServiceForCustomer['description']>;
  duration: FormControl<IServiceForCustomer['duration']>;
  serviceProvider: FormControl<IServiceForCustomer['serviceProvider']>;
  employees: FormControl<IServiceForCustomer['employees']>;
};

export type ServiceForCustomerFormGroup = FormGroup<ServiceForCustomerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ServiceForCustomerFormService {
  createServiceForCustomerFormGroup(serviceForCustomer: ServiceForCustomerFormGroupInput = { id: null }): ServiceForCustomerFormGroup {
    const serviceForCustomerRawValue = {
      ...this.getFormDefaults(),
      ...serviceForCustomer,
    };
    return new FormGroup<ServiceForCustomerFormGroupContent>({
      id: new FormControl(
        { value: serviceForCustomerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(serviceForCustomerRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      description: new FormControl(serviceForCustomerRawValue.description),
      duration: new FormControl(serviceForCustomerRawValue.duration, {
        validators: [Validators.required],
      }),
      serviceProvider: new FormControl(serviceForCustomerRawValue.serviceProvider),
      employees: new FormControl(serviceForCustomerRawValue.employees ?? []),
    });
  }

  getServiceForCustomer(form: ServiceForCustomerFormGroup): IServiceForCustomer | NewServiceForCustomer {
    return form.getRawValue() as IServiceForCustomer | NewServiceForCustomer;
  }

  resetForm(form: ServiceForCustomerFormGroup, serviceForCustomer: ServiceForCustomerFormGroupInput): void {
    const serviceForCustomerRawValue = { ...this.getFormDefaults(), ...serviceForCustomer };
    form.reset(
      {
        ...serviceForCustomerRawValue,
        id: { value: serviceForCustomerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ServiceForCustomerFormDefaults {
    return {
      id: null,
      employees: [],
    };
  }
}
