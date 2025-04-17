import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ServiceProviderService } from '../service/service-provider.service';
import { IServiceProvider } from '../service-provider.model';
import { ServiceProviderFormService } from './service-provider-form.service';

import { ServiceProviderUpdateComponent } from './service-provider-update.component';

describe('ServiceProvider Management Update Component', () => {
  let comp: ServiceProviderUpdateComponent;
  let fixture: ComponentFixture<ServiceProviderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let serviceProviderFormService: ServiceProviderFormService;
  let serviceProviderService: ServiceProviderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ServiceProviderUpdateComponent],
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
      .overrideTemplate(ServiceProviderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ServiceProviderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    serviceProviderFormService = TestBed.inject(ServiceProviderFormService);
    serviceProviderService = TestBed.inject(ServiceProviderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const serviceProvider: IServiceProvider = { id: 456 };

      activatedRoute.data = of({ serviceProvider });
      comp.ngOnInit();

      expect(comp.serviceProvider).toEqual(serviceProvider);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceProvider>>();
      const serviceProvider = { id: 123 };
      jest.spyOn(serviceProviderFormService, 'getServiceProvider').mockReturnValue(serviceProvider);
      jest.spyOn(serviceProviderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceProvider });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceProvider }));
      saveSubject.complete();

      // THEN
      expect(serviceProviderFormService.getServiceProvider).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(serviceProviderService.update).toHaveBeenCalledWith(expect.objectContaining(serviceProvider));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceProvider>>();
      const serviceProvider = { id: 123 };
      jest.spyOn(serviceProviderFormService, 'getServiceProvider').mockReturnValue({ id: null });
      jest.spyOn(serviceProviderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceProvider: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceProvider }));
      saveSubject.complete();

      // THEN
      expect(serviceProviderFormService.getServiceProvider).toHaveBeenCalled();
      expect(serviceProviderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceProvider>>();
      const serviceProvider = { id: 123 };
      jest.spyOn(serviceProviderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceProvider });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(serviceProviderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
