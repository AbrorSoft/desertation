import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { DayOfWeek } from 'app/entities/enumerations/day-of-week.model';

export interface IWorkingHours {
  id: number;
  dayOfWeek?: keyof typeof DayOfWeek | null;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  employee?: Pick<IEmployee, 'id' | 'name'> | null;
}

export type NewWorkingHours = Omit<IWorkingHours, 'id'> & { id: null };
