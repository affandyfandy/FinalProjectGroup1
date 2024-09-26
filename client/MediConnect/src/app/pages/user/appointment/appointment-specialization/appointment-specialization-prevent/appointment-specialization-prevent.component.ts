import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-appointment-specialization-prevent',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './appointment-specialization-prevent.component.html',
  styleUrl: './appointment-specialization-prevent.component.css',
})
export class AppointmentSpecializationPreventComponent {
  @Input() prevents: { title: string; description: string }[] = [];

  ngOnInit() {}
}
