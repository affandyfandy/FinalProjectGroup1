import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DoctorFormComponent } from '../doctor-form/doctor-form.component';

@Component({
  selector: 'app-doctor-update',
  standalone: true,
  imports: [DoctorFormComponent],
  templateUrl: './doctor-update.component.html',
  styleUrl: './doctor-update.component.css'
})
export class DoctorUpdateComponent implements OnInit {
  doctorId: string | null = null;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.doctorId = params['id'];
    });
  }
}
