import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IServiceProvider } from 'app/entities/service-provider/service-provider.model';
import { ServiceProviderService } from 'app/entities/service-provider/service/service-provider.service';
import { IServiceForCustomer } from 'app/entities/service-for-customer/service-for-customer.model';
import { ServiceForCustomerService } from 'app/entities/service-for-customer/service/service-for-customer.service';
import { IService } from 'app/entities/service/service.model';
import { ServiceService } from 'app/entities/service/service/service.service';
import { IEmployee } from '../employee.model';
import { EmployeeService } from '../service/employee.service';
import { EmployeeFormService } from './employee-form.service';

import { EmployeeUpdateComponent } from './employee-update.component';

describe('Employee Management Update Component', () => {
  let comp: EmployeeUpdateComponent;
  let fixture: ComponentFixture<EmployeeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeFormService: EmployeeFormService;
  let employeeService: EmployeeService;
  let serviceProviderService: ServiceProviderService;
  let serviceForCustomerService: ServiceForCustomerService;
  let serviceService: ServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), EmployeeUpdateComponent],
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
      .overrideTemplate(EmployeeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeFormService = TestBed.inject(EmployeeFormService);
    employeeService = TestBed.inject(EmployeeService);
    serviceProviderService = TestBed.inject(ServiceProviderService);
    serviceForCustomerService = TestBed.inject(ServiceForCustomerService);
    serviceService = TestBed.inject(ServiceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ServiceProvider query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const serviceProvider: IServiceProvider = { id: 9564 };
      employee.serviceProvider = serviceProvider;

      const serviceProviderCollection: IServiceProvider[] = [{ id: 6501 }];
      jest.spyOn(serviceProviderService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceProviderCollection })));
      const additionalServiceProviders = [serviceProvider];
      const expectedCollection: IServiceProvider[] = [...additionalServiceProviders, ...serviceProviderCollection];
      jest.spyOn(serviceProviderService, 'addServiceProviderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(serviceProviderService.query).toHaveBeenCalled();
      expect(serviceProviderService.addServiceProviderToCollectionIfMissing).toHaveBeenCalledWith(
        serviceProviderCollection,
        ...additionalServiceProviders.map(expect.objectContaining),
      );
      expect(comp.serviceProvidersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ServiceForCustomer query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const servicesForCustomers: IServiceForCustomer[] = [{ id: 12804 }];
      employee.servicesForCustomers = servicesForCustomers;

      const serviceForCustomerCollection: IServiceForCustomer[] = [{ id: 1897 }];
      jest.spyOn(serviceForCustomerService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceForCustomerCollection })));
      const additionalServiceForCustomers = [...servicesForCustomers];
      const expectedCollection: IServiceForCustomer[] = [...additionalServiceForCustomers, ...serviceForCustomerCollection];
      jest.spyOn(serviceForCustomerService, 'addServiceForCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(serviceForCustomerService.query).toHaveBeenCalled();
      expect(serviceForCustomerService.addServiceForCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        serviceForCustomerCollection,
        ...additionalServiceForCustomers.map(expect.objectContaining),
      );
      expect(comp.serviceForCustomersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Service query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const services: IService[] = [{ id: 12668 }];
      employee.services = services;

      const serviceCollection: IService[] = [{ id: 10952 }];
      jest.spyOn(serviceService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceCollection })));
      const additionalServices = [...services];
      const expectedCollection: IService[] = [...additionalServices, ...serviceCollection];
      jest.spyOn(serviceService, 'addServiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(serviceService.query).toHaveBeenCalled();
      expect(serviceService.addServiceToCollectionIfMissing).toHaveBeenCalledWith(
        serviceCollection,
        ...additionalServices.map(expect.objectContaining),
      );
      expect(comp.servicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employee: IEmployee = { id: 456 };
      const serviceProvider: IServiceProvider = { id: 24379 };
      employee.serviceProvider = serviceProvider;
      const servicesForCustomer: IServiceForCustomer = { id: 19138 };
      employee.servicesForCustomers = [servicesForCustomer];
      const services: IService = { id: 5183 };
      employee.services = [services];

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(comp.serviceProvidersSharedCollection).toContain(serviceProvider);
      expect(comp.serviceForCustomersSharedCollection).toContain(servicesForCustomer);
      expect(comp.servicesSharedCollection).toContain(services);
      expect(comp.employee).toEqual(employee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeFormService, 'getEmployee').mockReturnValue(employee);
      jest.spyOn(employeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employee }));
      saveSubject.complete();

      // THEN
      expect(employeeFormService.getEmployee).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeService.update).toHaveBeenCalledWith(expect.objectContaining(employee));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeFormService, 'getEmployee').mockReturnValue({ id: null });
      jest.spyOn(employeeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employee }));
      saveSubject.complete();

      // THEN
      expect(employeeFormService.getEmployee).toHaveBeenCalled();
      expect(employeeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeService.update).toHaveBeenCalled();
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

    describe('compareServiceForCustomer', () => {
      it('Should forward to serviceForCustomerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(serviceForCustomerService, 'compareServiceForCustomer');
        comp.compareServiceForCustomer(entity, entity2);
        expect(serviceForCustomerService.compareServiceForCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareService', () => {
      it('Should forward to serviceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(serviceService, 'compareService');
        comp.compareService(entity, entity2);
        expect(serviceService.compareService).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
