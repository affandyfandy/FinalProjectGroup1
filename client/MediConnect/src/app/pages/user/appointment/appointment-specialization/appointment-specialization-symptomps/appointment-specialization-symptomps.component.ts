import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-appointment-specialization-symptomps',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './appointment-specialization-symptomps.component.html',
  styleUrls: ['./appointment-specialization-symptomps.component.css'],
})
export class AppointmentSpecializationSymptompsComponent {
  @Input() symptomps: { name: string; description: string }[] = [];

  ngOnInit() {}
}
