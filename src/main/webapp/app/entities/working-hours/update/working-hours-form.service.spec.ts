import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../working-hours.test-samples';

import { WorkingHoursFormService } from './working-hours-form.service';

describe('WorkingHours Form Service', () => {
  let service: WorkingHoursFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WorkingHoursFormService);
  });

  describe('Service methods', () => {
    describe('createWorkingHoursFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWorkingHoursFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dayOfWeek: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            employee: expect.any(Object),
          }),
        );
      });

      it('passing IWorkingHours should create a new form with FormGroup', () => {
        const formGroup = service.createWorkingHoursFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dayOfWeek: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            employee: expect.any(Object),
          }),
        );
      });
    });

    describe('getWorkingHours', () => {
      it('should return NewWorkingHours for default WorkingHours initial value', () => {
        const formGroup = service.createWorkingHoursFormGroup(sampleWithNewData);

        const workingHours = service.getWorkingHours(formGroup) as any;

        expect(workingHours).toMatchObject(sampleWithNewData);
      });

      it('should return NewWorkingHours for empty WorkingHours initial value', () => {
        const formGroup = service.createWorkingHoursFormGroup();

        const workingHours = service.getWorkingHours(formGroup) as any;

        expect(workingHours).toMatchObject({});
      });

      it('should return IWorkingHours', () => {
        const formGroup = service.createWorkingHoursFormGroup(sampleWithRequiredData);

        const workingHours = service.getWorkingHours(formGroup) as any;

        expect(workingHours).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWorkingHours should not enable id FormControl', () => {
        const formGroup = service.createWorkingHoursFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWorkingHours should disable id FormControl', () => {
        const formGroup = service.createWorkingHoursFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
