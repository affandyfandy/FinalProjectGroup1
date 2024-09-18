import { Component, Input } from '@angular/core';
import { Patient } from '../../../../models/patient.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-patient-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './patient-modal.component.html',
  styleUrl: './patient-modal.component.css',
})
export class PatientModalComponent {
  @Input() patient!: Patient;

  // In your component class:
getFormattedDate(dateArray: number[]): string {
  if (dateArray.length === 3) {
    const [year, month, day] = dateArray;
    return new Date(year, month - 1, day).toLocaleDateString();
  }
  return 'N/A';
}

}
