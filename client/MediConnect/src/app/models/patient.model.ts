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

export interface PatientRequest {
  full_name: string;
  email: string;
  password: string;
  role: 'PATIENT';
  nik: number;
  phoneNumber: string;
  address: string;
  gender: string;
  dateOfBirth: Date;
}
