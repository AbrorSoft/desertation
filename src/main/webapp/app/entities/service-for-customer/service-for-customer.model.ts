import { IServiceProvider } from 'app/entities/service-provider/service-provider.model';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IServiceForCustomer {
  id: number;
  name?: string | null;
  description?: string | null;
  duration?: number | null;
  serviceProvider?: Pick<IServiceProvider, 'id' | 'name'> | null;
  employees?: Pick<IEmployee, 'id' | 'name'>[] | null;
}

export type NewServiceForCustomer = Omit<IServiceForCustomer, 'id'> & { id: null };
