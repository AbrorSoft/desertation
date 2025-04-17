import { IService, NewService } from './service.model';

export const sampleWithRequiredData: IService = {
  id: 24507,
  name: 'loyally among below',
  duration: 15766,
};

export const sampleWithPartialData: IService = {
  id: 27547,
  name: 'forenenst psst',
  duration: 5055,
};

export const sampleWithFullData: IService = {
  id: 4947,
  name: 'geez bashfully',
  description: 'afflict',
  duration: 6083,
};

export const sampleWithNewData: NewService = {
  name: 'blah jaw',
  duration: 7596,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
