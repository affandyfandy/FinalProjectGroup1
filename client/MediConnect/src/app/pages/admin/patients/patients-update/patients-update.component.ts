import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HlmButtonDirective } from '../../../../components/ui-button-helm/src/lib/hlm-button.directive';
import { PatientsFormComponent } from '../patients-form/patients-form.component';

@Component({
  selector: 'app-patients-update',
  standalone: true,
  imports: [HlmButtonDirective, PatientsFormComponent],
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
