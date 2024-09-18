import { Component, Input } from '@angular/core';
import { Patient } from '../../../../models/patient.model';
import { CommonModule } from '@angular/common';
import { PhoneFormatPipe } from '../../../../core/pipes/phone-format.pipe';

@Component({
  selector: 'app-patient-modal',
  standalone: true,
  imports: [CommonModule, PhoneFormatPipe], // Import the standalone pipe
  templateUrl: './patient-modal.component.html',
  styleUrls: ['./patient-modal.component.css'], // Fix typo: 'styleUrl' to 'styleUrls'
})
export class PatientModalComponent {
  @Input() patient!: Patient;
}
