import { IServiceProvider } from 'app/entities/service-provider/service-provider.model';

export interface IRoom {
  id: number;
  roomNumber?: string | null;
  description?: string | null;
  serviceProvider?: Pick<IServiceProvider, 'id' | 'name'> | null;
}

export type NewRoom = Omit<IRoom, 'id'> & { id: null };
