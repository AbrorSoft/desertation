import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IServiceForCustomer } from '../service-for-customer.model';
import { ServiceForCustomerService } from '../service/service-for-customer.service';

import serviceForCustomerResolve from './service-for-customer-routing-resolve.service';

describe('ServiceForCustomer routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: ServiceForCustomerService;
  let resultServiceForCustomer: IServiceForCustomer | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(ServiceForCustomerService);
    resultServiceForCustomer = undefined;
  });

  describe('resolve', () => {
    it('should return IServiceForCustomer returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        serviceForCustomerResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultServiceForCustomer = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultServiceForCustomer).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        serviceForCustomerResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultServiceForCustomer = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultServiceForCustomer).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IServiceForCustomer>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        serviceForCustomerResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultServiceForCustomer = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultServiceForCustomer).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
