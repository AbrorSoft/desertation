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
import { IServiceForCustomer } from '../service-for-customer.model';
import { ServiceForCustomerService } from '../service/service-for-customer.service';
import { ServiceForCustomerFormService } from './service-for-customer-form.service';

import { ServiceForCustomerUpdateComponent } from './service-for-customer-update.component';

describe('ServiceForCustomer Management Update Component', () => {
  let comp: ServiceForCustomerUpdateComponent;
  let fixture: ComponentFixture<ServiceForCustomerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let serviceForCustomerFormService: ServiceForCustomerFormService;
  let serviceForCustomerService: ServiceForCustomerService;
  let serviceProviderService: ServiceProviderService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ServiceForCustomerUpdateComponent],
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
      .overrideTemplate(ServiceForCustomerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ServiceForCustomerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    serviceForCustomerFormService = TestBed.inject(ServiceForCustomerFormService);
    serviceForCustomerService = TestBed.inject(ServiceForCustomerService);
    serviceProviderService = TestBed.inject(ServiceProviderService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ServiceProvider query and add missing value', () => {
      const serviceForCustomer: IServiceForCustomer = { id: 456 };
      const serviceProvider: IServiceProvider = { id: 3699 };
      serviceForCustomer.serviceProvider = serviceProvider;

      const serviceProviderCollection: IServiceProvider[] = [{ id: 3682 }];
      jest.spyOn(serviceProviderService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceProviderCollection })));
      const additionalServiceProviders = [serviceProvider];
      const expectedCollection: IServiceProvider[] = [...additionalServiceProviders, ...serviceProviderCollection];
      jest.spyOn(serviceProviderService, 'addServiceProviderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ serviceForCustomer });
      comp.ngOnInit();

      expect(serviceProviderService.query).toHaveBeenCalled();
      expect(serviceProviderService.addServiceProviderToCollectionIfMissing).toHaveBeenCalledWith(
        serviceProviderCollection,
        ...additionalServiceProviders.map(expect.objectContaining),
      );
      expect(comp.serviceProvidersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const serviceForCustomer: IServiceForCustomer = { id: 456 };
      const employees: IEmployee[] = [{ id: 15122 }];
      serviceForCustomer.employees = employees;

      const employeeCollection: IEmployee[] = [{ id: 19068 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [...employees];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ serviceForCustomer });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const serviceForCustomer: IServiceForCustomer = { id: 456 };
      const serviceProvider: IServiceProvider = { id: 10179 };
      serviceForCustomer.serviceProvider = serviceProvider;
      const employees: IEmployee = { id: 5821 };
      serviceForCustomer.employees = [employees];

      activatedRoute.data = of({ serviceForCustomer });
      comp.ngOnInit();

      expect(comp.serviceProvidersSharedCollection).toContain(serviceProvider);
      expect(comp.employeesSharedCollection).toContain(employees);
      expect(comp.serviceForCustomer).toEqual(serviceForCustomer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceForCustomer>>();
      const serviceForCustomer = { id: 123 };
      jest.spyOn(serviceForCustomerFormService, 'getServiceForCustomer').mockReturnValue(serviceForCustomer);
      jest.spyOn(serviceForCustomerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceForCustomer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceForCustomer }));
      saveSubject.complete();

      // THEN
      expect(serviceForCustomerFormService.getServiceForCustomer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(serviceForCustomerService.update).toHaveBeenCalledWith(expect.objectContaining(serviceForCustomer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceForCustomer>>();
      const serviceForCustomer = { id: 123 };
      jest.spyOn(serviceForCustomerFormService, 'getServiceForCustomer').mockReturnValue({ id: null });
      jest.spyOn(serviceForCustomerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceForCustomer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceForCustomer }));
      saveSubject.complete();

      // THEN
      expect(serviceForCustomerFormService.getServiceForCustomer).toHaveBeenCalled();
      expect(serviceForCustomerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceForCustomer>>();
      const serviceForCustomer = { id: 123 };
      jest.spyOn(serviceForCustomerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceForCustomer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(serviceForCustomerService.update).toHaveBeenCalled();
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
