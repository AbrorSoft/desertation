import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWorkingHours } from '../working-hours.model';
import { WorkingHoursService } from '../service/working-hours.service';

const workingHoursResolve = (route: ActivatedRouteSnapshot): Observable<null | IWorkingHours> => {
  const id = route.params['id'];
  if (id) {
    return inject(WorkingHoursService)
      .find(id)
      .pipe(
        mergeMap((workingHours: HttpResponse<IWorkingHours>) => {
          if (workingHours.body) {
            return of(workingHours.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default workingHoursResolve;
