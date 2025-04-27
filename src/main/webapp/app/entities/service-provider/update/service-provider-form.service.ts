import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IServiceProvider, NewServiceProvider } from '../service-provider.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IServiceProvider for edit and NewServiceProviderFormGroupInput for create.
 */
type ServiceProviderFormGroupInput = IServiceProvider | PartialWithRequiredKeyOf<NewServiceProvider>;

type ServiceProviderFormDefaults = Pick<NewServiceProvider, 'id'>;

type ServiceProviderFormGroupContent = {
  id: FormControl<IServiceProvider['id'] | NewServiceProvider['id']>;
  name: FormControl<IServiceProvider['name']>;
  type: FormControl<IServiceProvider['type']>;
  address: FormControl<IServiceProvider['address']>;
  contactInfo: FormControl<IServiceProvider['contactInfo']>;
  imageKey: FormControl<IServiceProvider['imageKey']>;
  imageFile: FormControl<IServiceProvider['imageFile']>;
};

export type ServiceProviderFormGroup = FormGroup<ServiceProviderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ServiceProviderFormService {
  createServiceProviderFormGroup(serviceProvider: ServiceProviderFormGroupInput = { id: null }): ServiceProviderFormGroup {
    const serviceProviderRawValue = {
      ...this.getFormDefaults(),
      ...serviceProvider,
    };
    return new FormGroup<ServiceProviderFormGroupContent>({
      id: new FormControl(
        { value: serviceProviderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(serviceProviderRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      type: new FormControl(serviceProviderRawValue.type, {
        validators: [Validators.required],
      }),
      address: new FormControl(serviceProviderRawValue.address),
      contactInfo: new FormControl(serviceProviderRawValue.contactInfo),
      imageKey: new FormControl(serviceProviderRawValue.imageKey),
      imageFile: new FormControl(serviceProviderRawValue.imageFile, {
        validators: [Validators.required],
      }),
    });
  }

  getServiceProvider(form: ServiceProviderFormGroup): IServiceProvider | NewServiceProvider {
    return form.getRawValue() as IServiceProvider | NewServiceProvider;
  }

  resetForm(form: ServiceProviderFormGroup, serviceProvider: ServiceProviderFormGroupInput): void {
    const serviceProviderRawValue = { ...this.getFormDefaults(), ...serviceProvider };
    form.reset(
      {
        ...serviceProviderRawValue,
        id: { value: serviceProviderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ServiceProviderFormDefaults {
    return {
      id: null,
    };
  }
}
