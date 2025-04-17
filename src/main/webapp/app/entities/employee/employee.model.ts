import { IServiceProvider } from 'app/entities/service-provider/service-provider.model';
import { IServiceForCustomer } from 'app/entities/service-for-customer/service-for-customer.model';
import { IService } from 'app/entities/service/service.model';

export interface IEmployee {
  id: number;
  name?: string | null;
  specialization?: string | null;
  serviceProvider?: Pick<IServiceProvider, 'id' | 'name'> | null;
  servicesForCustomers?: Pick<IServiceForCustomer, 'id'>[] | null;
  services?: Pick<IService, 'id'>[] | null;
}

export type NewEmployee = Omit<IEmployee, 'id'> & { id: null };
