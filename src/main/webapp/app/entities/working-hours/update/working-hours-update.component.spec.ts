import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { WorkingHoursService } from '../service/working-hours.service';
import { IWorkingHours } from '../working-hours.model';
import { WorkingHoursFormService } from './working-hours-form.service';

import { WorkingHoursUpdateComponent } from './working-hours-update.component';

describe('WorkingHours Management Update Component', () => {
  let comp: WorkingHoursUpdateComponent;
  let fixture: ComponentFixture<WorkingHoursUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let workingHoursFormService: WorkingHoursFormService;
  let workingHoursService: WorkingHoursService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), WorkingHoursUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(WorkingHoursUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkingHoursUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    workingHoursFormService = TestBed.inject(WorkingHoursFormService);
    workingHoursService = TestBed.inject(WorkingHoursService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const workingHours: IWorkingHours = { id: 456 };
      const employee: IEmployee = { id: 177 };
      workingHours.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 25433 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workingHours });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const workingHours: IWorkingHours = { id: 456 };
      const employee: IEmployee = { id: 23676 };
      workingHours.employee = employee;

      activatedRoute.data = of({ workingHours });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.workingHours).toEqual(workingHours);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkingHours>>();
      const workingHours = { id: 123 };
      jest.spyOn(workingHoursFormService, 'getWorkingHours').mockReturnValue(workingHours);
      jest.spyOn(workingHoursService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workingHours });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workingHours }));
      saveSubject.complete();

      // THEN
      expect(workingHoursFormService.getWorkingHours).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(workingHoursService.update).toHaveBeenCalledWith(expect.objectContaining(workingHours));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkingHours>>();
      const workingHours = { id: 123 };
      jest.spyOn(workingHoursFormService, 'getWorkingHours').mockReturnValue({ id: null });
      jest.spyOn(workingHoursService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workingHours: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workingHours }));
      saveSubject.complete();

      // THEN
      expect(workingHoursFormService.getWorkingHours).toHaveBeenCalled();
      expect(workingHoursService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkingHours>>();
      const workingHours = { id: 123 };
      jest.spyOn(workingHoursService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workingHours });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(workingHoursService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
