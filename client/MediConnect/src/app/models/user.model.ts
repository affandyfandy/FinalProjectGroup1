export interface User {
  id: string;
  fullName: string;
  email: string;
  password: string;
  role: string;
  createdBy: string;
  createdTime: Date;
  updatedBy: string;
  updatedTime: Date;
}

export interface UserRequestLogin {
  email: string;
  password: string;
}

export interface UserRequestRegister {
  nik: number;
  fullName: string;
  email: string;
  password: string;
  role: string;
}

export interface UserRequestAdmin {
  fullName: string;
  email: string;
  password: string;
  role: string;
}

export interface UserSaveDTO {
  fullName: string;
  email: string;
  password?: string;
  role: string;
}

export interface UserShowDTO {
  id: string;
  fullName: string;
  email: string;
  role: string;
  createdTime: Date;
}
