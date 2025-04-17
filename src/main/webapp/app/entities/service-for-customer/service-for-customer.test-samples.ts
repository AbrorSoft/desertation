import { IServiceForCustomer, NewServiceForCustomer } from './service-for-customer.model';

export const sampleWithRequiredData: IServiceForCustomer = {
  id: 18596,
  name: 'what highly',
  duration: 360,
};

export const sampleWithPartialData: IServiceForCustomer = {
  id: 13080,
  name: 'where',
  description: 'righteously briefly',
  duration: 18640,
};

export const sampleWithFullData: IServiceForCustomer = {
  id: 10796,
  name: 'at',
  description: 'considering',
  duration: 11992,
};

export const sampleWithNewData: NewServiceForCustomer = {
  name: 'ack',
  duration: 30889,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
