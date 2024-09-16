import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-patients-update',
  standalone: true,
  imports: [],
  templateUrl: './patients-update.component.html',
  styleUrl: './patients-update.component.css',
})
export class PatientsUpdateComponent implements OnInit {
  patientId: string = '';

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.patientId = params['id']; // Access the 'id' parameter from the URL
      console.log('Test ID:', this.patientId);
    });
  }
}
