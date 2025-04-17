import dayjs from 'dayjs/esm';

import { IAppointment, NewAppointment } from './appointment.model';

export const sampleWithRequiredData: IAppointment = {
  id: 10144,
  date: dayjs('2025-03-02'),
  startTime: dayjs('2025-03-02T14:36'),
  endTime: dayjs('2025-03-03T06:59'),
  status: 'BOOKED',
};

export const sampleWithPartialData: IAppointment = {
  id: 6161,
  date: dayjs('2025-03-03'),
  startTime: dayjs('2025-03-03T05:54'),
  endTime: dayjs('2025-03-02T11:51'),
  status: 'CANCELLED',
};

export const sampleWithFullData: IAppointment = {
  id: 23219,
  date: dayjs('2025-03-03'),
  startTime: dayjs('2025-03-03T02:11'),
  endTime: dayjs('2025-03-03T06:35'),
  status: 'BOOKED',
};

export const sampleWithNewData: NewAppointment = {
  date: dayjs('2025-03-02'),
  startTime: dayjs('2025-03-02T07:52'),
  endTime: dayjs('2025-03-02T22:41'),
  status: 'CANCELLED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
