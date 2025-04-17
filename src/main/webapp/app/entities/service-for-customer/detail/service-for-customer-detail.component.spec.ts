import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ServiceForCustomerDetailComponent } from './service-for-customer-detail.component';

describe('ServiceForCustomer Management Detail Component', () => {
  let comp: ServiceForCustomerDetailComponent;
  let fixture: ComponentFixture<ServiceForCustomerDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServiceForCustomerDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ServiceForCustomerDetailComponent,
              resolve: { serviceForCustomer: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ServiceForCustomerDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ServiceForCustomerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load serviceForCustomer on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ServiceForCustomerDetailComponent);

      // THEN
      expect(instance.serviceForCustomer).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
