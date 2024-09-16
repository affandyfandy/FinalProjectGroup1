import { Component, OnInit, ViewChild } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-patients-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HlmInputDirective,
    HlmButtonDirective,
    HlmLabelDirective,
    HlmIconComponent,
    NgIconComponent,
    RouterModule,
  ],
  templateUrl: './patients-form.component.html',
  styleUrl: './patients-form.component.css',
})
export class PatientsFormComponent implements OnInit {
  patientForm!: FormGroup;

  constructor(private fb: FormBuilder, private router: Router) {}

  ngOnInit(): void {
    this.patientForm = this.fb.group({
      full_name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['password123'],
      role: ['PATIENT'], // Since role is fixed, we hard-code it
      nik: ['', [Validators.required, Validators.pattern('^[0-9]{16}$')]], // Pattern for NIK (16-digit number)
      phoneNumber: [
        '',
        [Validators.required, Validators.pattern('^[0-9]{10,12}$')],
      ], // Allow only valid phone numbers
      address: ['', [Validators.required]],
      gender: ['', [Validators.required]], // Assuming you will have a dropdown or radio buttons for this
      dateOfBirth: ['', [Validators.required]],
    });
  }

  // Method to handle form submission
  onSubmit(): void {
    if (this.patientForm.valid) {
      const formData = this.patientForm.value;
      console.log('Patient data submitted:', formData);
      // Perform your logic here, e.g., send formData to an API.
    } else {
      console.log('Form is invalid');
      this.patientForm.markAllAsTouched(); // Mark all controls as touched to show validation messages
    }
  }

  get full_name() {
    return this.patientForm.get('full_name');
  }

  get email() {
    return this.patientForm.get('email');
  }
  get nik() {
    return this.patientForm.get('nik');
  }
  get phoneNumber() {
    return this.patientForm.get('phoneNumber');
  }
  get address() {
    return this.patientForm.get('address');
  }
  get gender() {
    return this.patientForm.get('gender');
  }
  get dateOfBirth() {
    return this.patientForm.get('dateOfBirth');
  }
}
