import { Component, Input } from '@angular/core';
import { Doctor } from '../../../../models/doctor.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-doctor-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './doctor-modal.component.html',
  styleUrl: './doctor-modal.component.css'
})
export class DoctorModalComponent {
  @Input() doctor !: Doctor;

  getFormattedDate(dateArray: number[]): string {
    if (dateArray.length === 3) {
      const [year, month, day] = dateArray;
      return new Date(year, month - 1, day).toLocaleDateString();
    }
    return 'N/A';
  }
}
