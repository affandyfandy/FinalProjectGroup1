import { Component } from '@angular/core';
import { HlmButtonDirective } from '../../../../components/ui-button-helm/src/lib/hlm-button.directive';
import { PatientsFormComponent } from '../patients-form/patients-form.component';

@Component({
  selector: 'app-patients-create',
  standalone: true,
  imports: [HlmButtonDirective, PatientsFormComponent],
  templateUrl: './patients-create.component.html',
  styleUrl: './patients-create.component.css',
})
export class PatientsCreateComponent {}
