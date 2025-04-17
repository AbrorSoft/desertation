import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IServiceProvider } from 'app/entities/service-provider/service-provider.model';
import { ServiceProviderService } from 'app/entities/service-provider/service/service-provider.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IService } from '../service.model';
import { ServiceService } from '../service/service.service';
import { ServiceFormService } from './service-form.service';

import { ServiceUpdateComponent } from './service-update.component';

describe('Service Management Update Component', () => {
  let comp: ServiceUpdateComponent;
  let fixture: ComponentFixture<ServiceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let serviceFormService: ServiceFormService;
  let serviceService: ServiceService;
  let serviceProviderService: ServiceProviderService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ServiceUpdateComponent],
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
      .overrideTemplate(ServiceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ServiceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    serviceFormService = TestBed.inject(ServiceFormService);
    serviceService = TestBed.inject(ServiceService);
    serviceProviderService = TestBed.inject(ServiceProviderService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ServiceProvider query and add missing value', () => {
      const service: IService = { id: 456 };
      const serviceProvider: IServiceProvider = { id: 7352 };
      service.serviceProvider = serviceProvider;

      const serviceProviderCollection: IServiceProvider[] = [{ id: 1200 }];
      jest.spyOn(serviceProviderService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceProviderCollection })));
      const additionalServiceProviders = [serviceProvider];
      const expectedCollection: IServiceProvider[] = [...additionalServiceProviders, ...serviceProviderCollection];
      jest.spyOn(serviceProviderService, 'addServiceProviderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ service });
      comp.ngOnInit();

      expect(serviceProviderService.query).toHaveBeenCalled();
      expect(serviceProviderService.addServiceProviderToCollectionIfMissing).toHaveBeenCalledWith(
        serviceProviderCollection,
        ...additionalServiceProviders.map(expect.objectContaining),
      );
      expect(comp.serviceProvidersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const service: IService = { id: 456 };
      const employees: IEmployee[] = [{ id: 3717 }];
      service.employees = employees;

      const employeeCollection: IEmployee[] = [{ id: 24874 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [...employees];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ service });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const service: IService = { id: 456 };
      const serviceProvider: IServiceProvider = { id: 4882 };
      service.serviceProvider = serviceProvider;
      const employees: IEmployee = { id: 11817 };
      service.employees = [employees];

      activatedRoute.data = of({ service });
      comp.ngOnInit();

      expect(comp.serviceProvidersSharedCollection).toContain(serviceProvider);
      expect(comp.employeesSharedCollection).toContain(employees);
      expect(comp.service).toEqual(service);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IService>>();
      const service = { id: 123 };
      jest.spyOn(serviceFormService, 'getService').mockReturnValue(service);
      jest.spyOn(serviceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ service });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: service }));
      saveSubject.complete();

      // THEN
      expect(serviceFormService.getService).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(serviceService.update).toHaveBeenCalledWith(expect.objectContaining(service));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IService>>();
      const service = { id: 123 };
      jest.spyOn(serviceFormService, 'getService').mockReturnValue({ id: null });
      jest.spyOn(serviceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ service: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: service }));
      saveSubject.complete();

      // THEN
      expect(serviceFormService.getService).toHaveBeenCalled();
      expect(serviceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IService>>();
      const service = { id: 123 };
      jest.spyOn(serviceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ service });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(serviceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareServiceProvider', () => {
      it('Should forward to serviceProviderService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(serviceProviderService, 'compareServiceProvider');
        comp.compareServiceProvider(entity, entity2);
        expect(serviceProviderService.compareServiceProvider).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
