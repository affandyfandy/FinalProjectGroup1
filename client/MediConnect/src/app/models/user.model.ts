export interface User {
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
