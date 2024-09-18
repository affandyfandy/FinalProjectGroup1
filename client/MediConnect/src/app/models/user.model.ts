export interface User {
  id: string;
  full_name: string;
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
  full_name: string;
  email: string;
  password: string;
  role: string;
}

export interface UserRequestAdmin {
  full_name: string;
  email: string;
  password: string;
  role: string;
}

export interface UserSaveDTO {
  full_name: string;
  email: string;
  password: string;
  role: string;
}

export interface UserShowDTO {
  id: string;
  full_name: string;
  email: string;
  role: string;
  createdTime: Date;
}
