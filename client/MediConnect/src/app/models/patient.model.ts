import { User } from './user.model';

export interface Patient {
  id: string;
  user: User;
  nik: number;
  phoneNumber: string;
  address: string;
  gender: string;
  dateOfBirth: string;
  createdTime: string;
  updatedTime: string;
  createdBy: string;
  updatedBy: string;
}

export interface PatientRequest {
  fullName: string;
  email: string;
  password: string;
  role: 'PATIENT';
  nik: number;
  phoneNumber: string;
  address: string;
  gender: string;
  dateOfBirth: Date;
}

export interface PatientDTO {
  id: string;
  user: string;
  nik: string;
  phoneNumber: string;
  address: string;
  gender: string;
  dateOfBirth: string;
  createdTime: string;
  updatedTime: string;
  createdBy: string;
  updatedBy: string;
}

export interface PatientSaveDTO {
  userId: string;
  nik: number;
  phoneNumber: string;
  address: string;
  gender: string;
  dateOfBirth: string;
}

export interface PatientShowDTO {
  id: string;
  user: User;
  nik: string;
  phoneNumber: string;
  address: string;
  gender: string;
  dateOfBirth: string;
}
