import { IServiceProvider } from 'app/entities/service-provider/service-provider.model';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface IService {
  id: number;
  name?: string | null;
  description?: string | null;
  duration?: number | null;
  serviceProvider?: Pick<IServiceProvider, 'id'> | null;
  employees?: Pick<IEmployee, 'id'>[] | null;
}

export type NewService = Omit<IService, 'id'> & { id: null };
