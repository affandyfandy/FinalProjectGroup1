import { User } from './user.model';

export interface Patient {
  id: string;
  user: User;
  nik: number;
  phoneNumber: string;
  address: string;
  gender: string;
  dateOfBirth: [number, number, number]; // [year, month, day]
}
