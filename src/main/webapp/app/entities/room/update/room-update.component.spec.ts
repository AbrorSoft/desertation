import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IServiceProvider } from 'app/entities/service-provider/service-provider.model';
import { ServiceProviderService } from 'app/entities/service-provider/service/service-provider.service';
import { RoomService } from '../service/room.service';
import { IRoom } from '../room.model';
import { RoomFormService } from './room-form.service';

import { RoomUpdateComponent } from './room-update.component';

describe('Room Management Update Component', () => {
  let comp: RoomUpdateComponent;
  let fixture: ComponentFixture<RoomUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let roomFormService: RoomFormService;
  let roomService: RoomService;
  let serviceProviderService: ServiceProviderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), RoomUpdateComponent],
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
      .overrideTemplate(RoomUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RoomUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    roomFormService = TestBed.inject(RoomFormService);
    roomService = TestBed.inject(RoomService);
    serviceProviderService = TestBed.inject(ServiceProviderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ServiceProvider query and add missing value', () => {
      const room: IRoom = { id: 456 };
      const serviceProvider: IServiceProvider = { id: 3150 };
      room.serviceProvider = serviceProvider;

      const serviceProviderCollection: IServiceProvider[] = [{ id: 5302 }];
      jest.spyOn(serviceProviderService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceProviderCollection })));
      const additionalServiceProviders = [serviceProvider];
      const expectedCollection: IServiceProvider[] = [...additionalServiceProviders, ...serviceProviderCollection];
      jest.spyOn(serviceProviderService, 'addServiceProviderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ room });
      comp.ngOnInit();

      expect(serviceProviderService.query).toHaveBeenCalled();
      expect(serviceProviderService.addServiceProviderToCollectionIfMissing).toHaveBeenCalledWith(
        serviceProviderCollection,
        ...additionalServiceProviders.map(expect.objectContaining),
      );
      expect(comp.serviceProvidersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const room: IRoom = { id: 456 };
      const serviceProvider: IServiceProvider = { id: 27108 };
      room.serviceProvider = serviceProvider;

      activatedRoute.data = of({ room });
      comp.ngOnInit();

      expect(comp.serviceProvidersSharedCollection).toContain(serviceProvider);
      expect(comp.room).toEqual(room);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoom>>();
      const room = { id: 123 };
      jest.spyOn(roomFormService, 'getRoom').mockReturnValue(room);
      jest.spyOn(roomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ room });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: room }));
      saveSubject.complete();

      // THEN
      expect(roomFormService.getRoom).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(roomService.update).toHaveBeenCalledWith(expect.objectContaining(room));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoom>>();
      const room = { id: 123 };
      jest.spyOn(roomFormService, 'getRoom').mockReturnValue({ id: null });
      jest.spyOn(roomService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ room: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: room }));
      saveSubject.complete();

      // THEN
      expect(roomFormService.getRoom).toHaveBeenCalled();
      expect(roomService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRoom>>();
      const room = { id: 123 };
      jest.spyOn(roomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ room });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(roomService.update).toHaveBeenCalled();
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
  });
});
