import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '9670d9f9-ffb6-499e-b570-a19fc5a14124',
};

export const sampleWithPartialData: IAuthority = {
  name: '906ab624-b28c-4d38-abb2-d26ecfd89cd0',
};

export const sampleWithFullData: IAuthority = {
  name: 'e5b919e8-c870-49e4-bcc8-af189da3c85f',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
