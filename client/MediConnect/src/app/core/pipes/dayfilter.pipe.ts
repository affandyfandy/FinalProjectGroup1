import { Pipe, PipeTransform } from '@angular/core';
import { DoctorScheduleList } from '../../models/doctor-schedule.model';

@Pipe({
  name: 'dayFilter',
  standalone: true,  // Add this line to make the pipe standalone
})
export class DayFilterPipe implements PipeTransform {
  transform(schedules: DoctorScheduleList[], day: string): DoctorScheduleList[] {
    return schedules.filter(schedule => schedule.day === day);
  }
}
