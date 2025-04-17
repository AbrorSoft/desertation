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
import { IServiceForCustomer } from 'app/entities/service-for-customer/service-for-customer.model';
import { ServiceForCustomerService } from 'app/entities/service-for-customer/service/service-for-customer.service';
import { IRoom } from 'app/entities/room/room.model';
import { RoomService } from 'app/entities/room/service/room.service';
import { IAppointment } from '../appointment.model';
import { AppointmentService } from '../service/appointment.service';
import { AppointmentFormService } from './appointment-form.service';

import { AppointmentUpdateComponent } from './appointment-update.component';

describe('Appointment Management Update Component', () => {
  let comp: AppointmentUpdateComponent;
  let fixture: ComponentFixture<AppointmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let appointmentFormService: AppointmentFormService;
  let appointmentService: AppointmentService;
  let serviceProviderService: ServiceProviderService;
  let employeeService: EmployeeService;
  let serviceForCustomerService: ServiceForCustomerService;
  let roomService: RoomService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AppointmentUpdateComponent],
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
      .overrideTemplate(AppointmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AppointmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    appointmentFormService = TestBed.inject(AppointmentFormService);
    appointmentService = TestBed.inject(AppointmentService);
    serviceProviderService = TestBed.inject(ServiceProviderService);
    employeeService = TestBed.inject(EmployeeService);
    serviceForCustomerService = TestBed.inject(ServiceForCustomerService);
    roomService = TestBed.inject(RoomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ServiceProvider query and add missing value', () => {
      const appointment: IAppointment = { id: 456 };
      const serviceProvider: IServiceProvider = { id: 24413 };
      appointment.serviceProvider = serviceProvider;

      const serviceProviderCollection: IServiceProvider[] = [{ id: 26796 }];
      jest.spyOn(serviceProviderService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceProviderCollection })));
      const additionalServiceProviders = [serviceProvider];
      const expectedCollection: IServiceProvider[] = [...additionalServiceProviders, ...serviceProviderCollection];
      jest.spyOn(serviceProviderService, 'addServiceProviderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      expect(serviceProviderService.query).toHaveBeenCalled();
      expect(serviceProviderService.addServiceProviderToCollectionIfMissing).toHaveBeenCalledWith(
        serviceProviderCollection,
        ...additionalServiceProviders.map(expect.objectContaining),
      );
      expect(comp.serviceProvidersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const appointment: IAppointment = { id: 456 };
      const employee: IEmployee = { id: 21996 };
      appointment.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 1053 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ServiceForCustomer query and add missing value', () => {
      const appointment: IAppointment = { id: 456 };
      const serviceForCustomer: IServiceForCustomer = { id: 30385 };
      appointment.serviceForCustomer = serviceForCustomer;

      const serviceForCustomerCollection: IServiceForCustomer[] = [{ id: 8243 }];
      jest.spyOn(serviceForCustomerService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceForCustomerCollection })));
      const additionalServiceForCustomers = [serviceForCustomer];
      const expectedCollection: IServiceForCustomer[] = [...additionalServiceForCustomers, ...serviceForCustomerCollection];
      jest.spyOn(serviceForCustomerService, 'addServiceForCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      expect(serviceForCustomerService.query).toHaveBeenCalled();
      expect(serviceForCustomerService.addServiceForCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        serviceForCustomerCollection,
        ...additionalServiceForCustomers.map(expect.objectContaining),
      );
      expect(comp.serviceForCustomersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Room query and add missing value', () => {
      const appointment: IAppointment = { id: 456 };
      const room: IRoom = { id: 18072 };
      appointment.room = room;

      const roomCollection: IRoom[] = [{ id: 29548 }];
      jest.spyOn(roomService, 'query').mockReturnValue(of(new HttpResponse({ body: roomCollection })));
      const additionalRooms = [room];
      const expectedCollection: IRoom[] = [...additionalRooms, ...roomCollection];
      jest.spyOn(roomService, 'addRoomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      expect(roomService.query).toHaveBeenCalled();
      expect(roomService.addRoomToCollectionIfMissing).toHaveBeenCalledWith(
        roomCollection,
        ...additionalRooms.map(expect.objectContaining),
      );
      expect(comp.roomsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const appointment: IAppointment = { id: 456 };
      const serviceProvider: IServiceProvider = { id: 8173 };
      appointment.serviceProvider = serviceProvider;
      const employee: IEmployee = { id: 41 };
      appointment.employee = employee;
      const serviceForCustomer: IServiceForCustomer = { id: 26347 };
      appointment.serviceForCustomer = serviceForCustomer;
      const room: IRoom = { id: 10510 };
      appointment.room = room;

      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      expect(comp.serviceProvidersSharedCollection).toContain(serviceProvider);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.serviceForCustomersSharedCollection).toContain(serviceForCustomer);
      expect(comp.roomsSharedCollection).toContain(room);
      expect(comp.appointment).toEqual(appointment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppointment>>();
      const appointment = { id: 123 };
      jest.spyOn(appointmentFormService, 'getAppointment').mockReturnValue(appointment);
      jest.spyOn(appointmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appointment }));
      saveSubject.complete();

      // THEN
      expect(appointmentFormService.getAppointment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(appointmentService.update).toHaveBeenCalledWith(expect.objectContaining(appointment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppointment>>();
      const appointment = { id: 123 };
      jest.spyOn(appointmentFormService, 'getAppointment').mockReturnValue({ id: null });
      jest.spyOn(appointmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appointment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appointment }));
      saveSubject.complete();

      // THEN
      expect(appointmentFormService.getAppointment).toHaveBeenCalled();
      expect(appointmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppointment>>();
      const appointment = { id: 123 };
      jest.spyOn(appointmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appointment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(appointmentService.update).toHaveBeenCalled();
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

    describe('compareServiceForCustomer', () => {
      it('Should forward to serviceForCustomerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(serviceForCustomerService, 'compareServiceForCustomer');
        comp.compareServiceForCustomer(entity, entity2);
        expect(serviceForCustomerService.compareServiceForCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRoom', () => {
      it('Should forward to roomService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(roomService, 'compareRoom');
        comp.compareRoom(entity, entity2);
        expect(roomService.compareRoom).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
