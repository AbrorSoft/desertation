import { IServiceProvider, NewServiceProvider } from './service-provider.model';

export const sampleWithRequiredData: IServiceProvider = {
  id: 19875,
  name: 'or scout',
  type: 'SPA',
};

export const sampleWithPartialData: IServiceProvider = {
  id: 26022,
  name: 'lightly whoa when',
  type: 'SPA',
};

export const sampleWithFullData: IServiceProvider = {
  id: 10685,
  name: 'brace',
  type: 'BARBERSHOP',
  address: 'bird brr',
  contactInfo: 'vastly yet',
};

export const sampleWithNewData: NewServiceProvider = {
  name: 'yoga ugh',
  type: 'CLINIC',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
