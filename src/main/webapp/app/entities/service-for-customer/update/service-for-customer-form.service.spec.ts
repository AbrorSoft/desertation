import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../service-for-customer.test-samples';

import { ServiceForCustomerFormService } from './service-for-customer-form.service';

describe('ServiceForCustomer Form Service', () => {
  let service: ServiceForCustomerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiceForCustomerFormService);
  });

  describe('Service methods', () => {
    describe('createServiceForCustomerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createServiceForCustomerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            duration: expect.any(Object),
            serviceProvider: expect.any(Object),
            employees: expect.any(Object),
          }),
        );
      });

      it('passing IServiceForCustomer should create a new form with FormGroup', () => {
        const formGroup = service.createServiceForCustomerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            duration: expect.any(Object),
            serviceProvider: expect.any(Object),
            employees: expect.any(Object),
          }),
        );
      });
    });

    describe('getServiceForCustomer', () => {
      it('should return NewServiceForCustomer for default ServiceForCustomer initial value', () => {
        const formGroup = service.createServiceForCustomerFormGroup(sampleWithNewData);

        const serviceForCustomer = service.getServiceForCustomer(formGroup) as any;

        expect(serviceForCustomer).toMatchObject(sampleWithNewData);
      });

      it('should return NewServiceForCustomer for empty ServiceForCustomer initial value', () => {
        const formGroup = service.createServiceForCustomerFormGroup();

        const serviceForCustomer = service.getServiceForCustomer(formGroup) as any;

        expect(serviceForCustomer).toMatchObject({});
      });

      it('should return IServiceForCustomer', () => {
        const formGroup = service.createServiceForCustomerFormGroup(sampleWithRequiredData);

        const serviceForCustomer = service.getServiceForCustomer(formGroup) as any;

        expect(serviceForCustomer).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IServiceForCustomer should not enable id FormControl', () => {
        const formGroup = service.createServiceForCustomerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewServiceForCustomer should disable id FormControl', () => {
        const formGroup = service.createServiceForCustomerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
