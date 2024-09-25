export interface AppointmentSaveDTO {
  patientId: string;
  doctorId: string;
  initialComplaint: string;
  date: string;
  startTime: string;
  endTime: string;
}

export interface AppointmentShowDTO {
  appointmentId: string;
  patientId: string;
  doctorId: string;
  date: string;
  status: string;
  initialComplaint: string;
  startTime: string;
  endTime: string;
  queueNumber: number;
}
