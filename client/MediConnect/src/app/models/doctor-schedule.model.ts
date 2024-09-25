export interface DoctorSchedule {
  id: string;
  doctorId: string;
  day: string;
  scheduleTimes: ScheduleTime[];
}

export interface ScheduleTime {
  startWorkingHour: string;
  endWorkingHour: string;
  maxPatient: number;
  createdBy: string;
  createdTime: Date;
  updatedBy: string;
  updatedTime: Date;
}

export interface DoctorScheduleDTO {
  id: string;
  doctorId: string;
  day: string;
  scheduleTimes: ScheduleTimeDTO[];
}

export interface ScheduleTimeDTO {
  startWorkingHour: string;
  endWorkingHour: string;
  maxPatient: number;
  createdBy: string;
  createdTime: Date;
  updatedBy: string;
  updatedTime: Date;
}

export interface ListDoctorSchedule {
  id: string;
  doctorId: string;
  day: string;
  name: string;
  startWorkingHour: string;
  endWorkingHour: string;
  maxPatient: number;
  createdBy: string;
  createdTime: Date;
  updatedBy: string;
  updatedTime: Date;
}
