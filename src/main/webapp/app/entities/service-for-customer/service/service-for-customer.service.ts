import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IServiceForCustomer, NewServiceForCustomer } from '../service-for-customer.model';

export type PartialUpdateServiceForCustomer = Partial<IServiceForCustomer> & Pick<IServiceForCustomer, 'id'>;

export type EntityResponseType = HttpResponse<IServiceForCustomer>;
export type EntityArrayResponseType = HttpResponse<IServiceForCustomer[]>;

@Injectable({ providedIn: 'root' })
export class ServiceForCustomerService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/service-for-customers');

  create(serviceForCustomer: NewServiceForCustomer): Observable<EntityResponseType> {
    return this.http.post<IServiceForCustomer>(this.resourceUrl, serviceForCustomer, { observe: 'response' });
  }

  update(serviceForCustomer: IServiceForCustomer): Observable<EntityResponseType> {
    return this.http.put<IServiceForCustomer>(
      `${this.resourceUrl}/${this.getServiceForCustomerIdentifier(serviceForCustomer)}`,
      serviceForCustomer,
      { observe: 'response' },
    );
  }

  partialUpdate(serviceForCustomer: PartialUpdateServiceForCustomer): Observable<EntityResponseType> {
    return this.http.patch<IServiceForCustomer>(
      `${this.resourceUrl}/${this.getServiceForCustomerIdentifier(serviceForCustomer)}`,
      serviceForCustomer,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IServiceForCustomer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IServiceForCustomer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getServiceForCustomerIdentifier(serviceForCustomer: Pick<IServiceForCustomer, 'id'>): number {
    return serviceForCustomer.id;
  }

  compareServiceForCustomer(o1: Pick<IServiceForCustomer, 'id'> | null, o2: Pick<IServiceForCustomer, 'id'> | null): boolean {
    return o1 && o2 ? this.getServiceForCustomerIdentifier(o1) === this.getServiceForCustomerIdentifier(o2) : o1 === o2;
  }

  addServiceForCustomerToCollectionIfMissing<Type extends Pick<IServiceForCustomer, 'id'>>(
    serviceForCustomerCollection: Type[],
    ...serviceForCustomersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const serviceForCustomers: Type[] = serviceForCustomersToCheck.filter(isPresent);
    if (serviceForCustomers.length > 0) {
      const serviceForCustomerCollectionIdentifiers = serviceForCustomerCollection.map(serviceForCustomerItem =>
        this.getServiceForCustomerIdentifier(serviceForCustomerItem),
      );
      const serviceForCustomersToAdd = serviceForCustomers.filter(serviceForCustomerItem => {
        const serviceForCustomerIdentifier = this.getServiceForCustomerIdentifier(serviceForCustomerItem);
        if (serviceForCustomerCollectionIdentifiers.includes(serviceForCustomerIdentifier)) {
          return false;
        }
        serviceForCustomerCollectionIdentifiers.push(serviceForCustomerIdentifier);
        return true;
      });
      return [...serviceForCustomersToAdd, ...serviceForCustomerCollection];
    }
    return serviceForCustomerCollection;
  }
}
