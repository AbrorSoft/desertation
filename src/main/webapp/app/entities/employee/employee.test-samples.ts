import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 31379,
  name: 'bandwidth sternly',
  specialization: 'disseminate',
};

export const sampleWithPartialData: IEmployee = {
  id: 664,
  name: 'apprehend deceivingly',
  specialization: 'concerning microwave',
};

export const sampleWithFullData: IEmployee = {
  id: 22798,
  name: 'tut lever',
  specialization: 'squiggly burn-out drat',
};

export const sampleWithNewData: NewEmployee = {
  name: 'rabbi',
  specialization: 'if deadhead frizzy',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
