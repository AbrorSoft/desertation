import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWorkingHours, NewWorkingHours } from '../working-hours.model';

export type PartialUpdateWorkingHours = Partial<IWorkingHours> & Pick<IWorkingHours, 'id'>;

type RestOf<T extends IWorkingHours | NewWorkingHours> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

export type RestWorkingHours = RestOf<IWorkingHours>;

export type NewRestWorkingHours = RestOf<NewWorkingHours>;

export type PartialUpdateRestWorkingHours = RestOf<PartialUpdateWorkingHours>;

export type EntityResponseType = HttpResponse<IWorkingHours>;
export type EntityArrayResponseType = HttpResponse<IWorkingHours[]>;

@Injectable({ providedIn: 'root' })
export class WorkingHoursService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/working-hours');

  create(workingHours: NewWorkingHours): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workingHours);
    return this.http
      .post<RestWorkingHours>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(workingHours: IWorkingHours): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workingHours);
    return this.http
      .put<RestWorkingHours>(`${this.resourceUrl}/${this.getWorkingHoursIdentifier(workingHours)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(workingHours: PartialUpdateWorkingHours): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workingHours);
    return this.http
      .patch<RestWorkingHours>(`${this.resourceUrl}/${this.getWorkingHoursIdentifier(workingHours)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestWorkingHours>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestWorkingHours[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWorkingHoursIdentifier(workingHours: Pick<IWorkingHours, 'id'>): number {
    return workingHours.id;
  }

  compareWorkingHours(o1: Pick<IWorkingHours, 'id'> | null, o2: Pick<IWorkingHours, 'id'> | null): boolean {
    return o1 && o2 ? this.getWorkingHoursIdentifier(o1) === this.getWorkingHoursIdentifier(o2) : o1 === o2;
  }

  addWorkingHoursToCollectionIfMissing<Type extends Pick<IWorkingHours, 'id'>>(
    workingHoursCollection: Type[],
    ...workingHoursToCheck: (Type | null | undefined)[]
  ): Type[] {
    const workingHours: Type[] = workingHoursToCheck.filter(isPresent);
    if (workingHours.length > 0) {
      const workingHoursCollectionIdentifiers = workingHoursCollection.map(workingHoursItem =>
        this.getWorkingHoursIdentifier(workingHoursItem),
      );
      const workingHoursToAdd = workingHours.filter(workingHoursItem => {
        const workingHoursIdentifier = this.getWorkingHoursIdentifier(workingHoursItem);
        if (workingHoursCollectionIdentifiers.includes(workingHoursIdentifier)) {
          return false;
        }
        workingHoursCollectionIdentifiers.push(workingHoursIdentifier);
        return true;
      });
      return [...workingHoursToAdd, ...workingHoursCollection];
    }
    return workingHoursCollection;
  }

  protected convertDateFromClient<T extends IWorkingHours | NewWorkingHours | PartialUpdateWorkingHours>(workingHours: T): RestOf<T> {
    return {
      ...workingHours,
      startTime: workingHours.startTime?.toJSON() ?? null,
      endTime: workingHours.endTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restWorkingHours: RestWorkingHours): IWorkingHours {
    return {
      ...restWorkingHours,
      startTime: restWorkingHours.startTime ? dayjs(restWorkingHours.startTime) : undefined,
      endTime: restWorkingHours.endTime ? dayjs(restWorkingHours.endTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestWorkingHours>): HttpResponse<IWorkingHours> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestWorkingHours[]>): HttpResponse<IWorkingHours[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
