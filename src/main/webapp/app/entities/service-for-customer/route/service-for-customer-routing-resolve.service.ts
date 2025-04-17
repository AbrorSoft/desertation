import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IServiceForCustomer } from '../service-for-customer.model';
import { ServiceForCustomerService } from '../service/service-for-customer.service';

const serviceForCustomerResolve = (route: ActivatedRouteSnapshot): Observable<null | IServiceForCustomer> => {
  const id = route.params['id'];
  if (id) {
    return inject(ServiceForCustomerService)
      .find(id)
      .pipe(
        mergeMap((serviceForCustomer: HttpResponse<IServiceForCustomer>) => {
          if (serviceForCustomer.body) {
            return of(serviceForCustomer.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default serviceForCustomerResolve;
