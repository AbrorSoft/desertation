import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 3956,
  login: 'X`$`*!@mNgyA\\4Cx',
};

export const sampleWithPartialData: IUser = {
  id: 15985,
  login: 'A.OY@2\\8Jl0K\\nOn6',
};

export const sampleWithFullData: IUser = {
  id: 1411,
  login: 'QLM1Mo@b\\,zWyr\\GSm4\\G61yiq\\e0\\oUW1u',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
