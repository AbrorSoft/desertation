import { ServiceProviderType } from 'app/entities/enumerations/service-provider-type.model';

export interface IServiceProvider {
  id: number;
  name?: string | null;
  type?: keyof typeof ServiceProviderType | null;
  address?: string | null;
  contactInfo?: string | null;
}

export type NewServiceProvider = Omit<IServiceProvider, 'id'> & { id: null };
