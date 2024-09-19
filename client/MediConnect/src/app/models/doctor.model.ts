export interface Doctor {
  id: string;
  name: string;
  specialization: string;
  identificationNumber: string;
  phoneNumber: string;
  gender: string;
  dateOfBirth: string;
  address: string;
  patientTotal: number;
  createdBy: string;
  createdTime: Date;
  updatedBy: string;
  updatedTime: Date;
}

export interface DoctorDTO {
  id: string;
  name: string;
  specialization: string;
  identificationNumber: string;
  phoneNumber: string;
  gender: string;
  dateOfBirth: string;
  address: string;
  patientTotal: number;
  createdBy: string;
  createdTime: Date;
  updatedBy: string;
  updatedTime: Date;
}
