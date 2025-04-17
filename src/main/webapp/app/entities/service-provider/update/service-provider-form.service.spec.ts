import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../service-provider.test-samples';

import { ServiceProviderFormService } from './service-provider-form.service';

describe('ServiceProvider Form Service', () => {
  let service: ServiceProviderFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiceProviderFormService);
  });

  describe('Service methods', () => {
    describe('createServiceProviderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createServiceProviderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            address: expect.any(Object),
            contactInfo: expect.any(Object),
          }),
        );
      });

      it('passing IServiceProvider should create a new form with FormGroup', () => {
        const formGroup = service.createServiceProviderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            address: expect.any(Object),
            contactInfo: expect.any(Object),
          }),
        );
      });
    });

    describe('getServiceProvider', () => {
      it('should return NewServiceProvider for default ServiceProvider initial value', () => {
        const formGroup = service.createServiceProviderFormGroup(sampleWithNewData);

        const serviceProvider = service.getServiceProvider(formGroup) as any;

        expect(serviceProvider).toMatchObject(sampleWithNewData);
      });

      it('should return NewServiceProvider for empty ServiceProvider initial value', () => {
        const formGroup = service.createServiceProviderFormGroup();

        const serviceProvider = service.getServiceProvider(formGroup) as any;

        expect(serviceProvider).toMatchObject({});
      });

      it('should return IServiceProvider', () => {
        const formGroup = service.createServiceProviderFormGroup(sampleWithRequiredData);

        const serviceProvider = service.getServiceProvider(formGroup) as any;

        expect(serviceProvider).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IServiceProvider should not enable id FormControl', () => {
        const formGroup = service.createServiceProviderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewServiceProvider should disable id FormControl', () => {
        const formGroup = service.createServiceProviderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
