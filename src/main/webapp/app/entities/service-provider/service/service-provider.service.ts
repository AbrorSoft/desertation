import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IServiceProvider, NewServiceProvider } from '../service-provider.model';

export type PartialUpdateServiceProvider = Partial<IServiceProvider> & Pick<IServiceProvider, 'id'>;

export type EntityResponseType = HttpResponse<IServiceProvider>;
export type EntityArrayResponseType = HttpResponse<IServiceProvider[]>;

@Injectable({ providedIn: 'root' })
export class ServiceProviderService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/service-providers');

  create(serviceProvider: NewServiceProvider): Observable<EntityResponseType> {
    return this.http.post<IServiceProvider>(this.resourceUrl, serviceProvider, { observe: 'response' });
  }

  update(serviceProvider: IServiceProvider): Observable<EntityResponseType> {
    return this.http.put<IServiceProvider>(`${this.resourceUrl}/${this.getServiceProviderIdentifier(serviceProvider)}`, serviceProvider, {
      observe: 'response',
    });
  }

  partialUpdate(serviceProvider: PartialUpdateServiceProvider): Observable<EntityResponseType> {
    return this.http.patch<IServiceProvider>(`${this.resourceUrl}/${this.getServiceProviderIdentifier(serviceProvider)}`, serviceProvider, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IServiceProvider>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IServiceProvider[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getServiceProviderIdentifier(serviceProvider: Pick<IServiceProvider, 'id'>): number {
    return serviceProvider.id;
  }

  compareServiceProvider(o1: Pick<IServiceProvider, 'id'> | null, o2: Pick<IServiceProvider, 'id'> | null): boolean {
    return o1 && o2 ? this.getServiceProviderIdentifier(o1) === this.getServiceProviderIdentifier(o2) : o1 === o2;
  }

  addServiceProviderToCollectionIfMissing<Type extends Pick<IServiceProvider, 'id'>>(
    serviceProviderCollection: Type[],
    ...serviceProvidersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const serviceProviders: Type[] = serviceProvidersToCheck.filter(isPresent);
    if (serviceProviders.length > 0) {
      const serviceProviderCollectionIdentifiers = serviceProviderCollection.map(serviceProviderItem =>
        this.getServiceProviderIdentifier(serviceProviderItem),
      );
      const serviceProvidersToAdd = serviceProviders.filter(serviceProviderItem => {
        const serviceProviderIdentifier = this.getServiceProviderIdentifier(serviceProviderItem);
        if (serviceProviderCollectionIdentifiers.includes(serviceProviderIdentifier)) {
          return false;
        }
        serviceProviderCollectionIdentifiers.push(serviceProviderIdentifier);
        return true;
      });
      return [...serviceProvidersToAdd, ...serviceProviderCollection];
    }
    return serviceProviderCollection;
  }
}
