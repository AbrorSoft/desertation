import { IRoom, NewRoom } from './room.model';

export const sampleWithRequiredData: IRoom = {
  id: 16939,
  roomNumber: 'poleaxe overturn furiously',
};

export const sampleWithPartialData: IRoom = {
  id: 15071,
  roomNumber: 'how',
};

export const sampleWithFullData: IRoom = {
  id: 18591,
  roomNumber: 'energetically',
  description: 'descend via',
};

export const sampleWithNewData: NewRoom = {
  roomNumber: 'demonise',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
