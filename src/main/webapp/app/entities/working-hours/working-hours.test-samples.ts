import dayjs from 'dayjs/esm';

import { IWorkingHours, NewWorkingHours } from './working-hours.model';

export const sampleWithRequiredData: IWorkingHours = {
  id: 9075,
  dayOfWeek: 'TUESDAY',
  startTime: dayjs('2025-03-02T14:11'),
  endTime: dayjs('2025-03-02T07:09'),
};

export const sampleWithPartialData: IWorkingHours = {
  id: 25583,
  dayOfWeek: 'THURSDAY',
  startTime: dayjs('2025-03-03T06:09'),
  endTime: dayjs('2025-03-02T23:29'),
};

export const sampleWithFullData: IWorkingHours = {
  id: 22725,
  dayOfWeek: 'FRIDAY',
  startTime: dayjs('2025-03-02T18:53'),
  endTime: dayjs('2025-03-02T10:41'),
};

export const sampleWithNewData: NewWorkingHours = {
  dayOfWeek: 'MONDAY',
  startTime: dayjs('2025-03-02T16:44'),
  endTime: dayjs('2025-03-02T09:56'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
