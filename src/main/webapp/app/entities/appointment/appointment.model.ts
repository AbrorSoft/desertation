import dayjs from 'dayjs/esm';
import { IServiceProvider } from 'app/entities/service-provider/service-provider.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IServiceForCustomer } from 'app/entities/service-for-customer/service-for-customer.model';
import { IRoom } from 'app/entities/room/room.model';
import { AppointmentStatus } from 'app/entities/enumerations/appointment-status.model';
import { IUser } from '../user/user.model';

export interface IAppointment {
  id: number;
  date?: dayjs.Dayjs | null;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  status?: keyof typeof AppointmentStatus | null;
  serviceProvider?: Pick<IServiceProvider, 'id' | 'name'> | null;
  employee?: Pick<IEmployee, 'id' | 'name'> | null;
  serviceForCustomer?: Pick<IServiceForCustomer, 'id' | 'name'> | null;
  room?: Pick<IRoom, 'id' | 'roomNumber'> | null;
  user?: IUser;
}

export type NewAppointment = Omit<IAppointment, 'id'> & { id: null };
