import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import {
  User,
  UserRequestRegister,
  UserSaveDTO,
} from '../../../../models/user.model';
import { PatientSaveDTO } from '../../../../models/patient.model';
import { PatientsService } from '../../../../services/patient-service/patients.service';
import { UserService } from '../../../../services/user-service/users.service';
@Component({
  selector: 'app-patients-form',
  templateUrl: './patients-form.component.html',
  styleUrl: './patients-form.component.css',
})
export class PatientsFormComponent implements OnInit {
  patientForm!: FormGroup;
  createdUserId: string = ''; // To store the created user ID

  constructor(
    private fb: FormBuilder,
    private patientService: PatientsService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.patientForm = this.fb.group({
      full_name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['password123'],
      role: ['PATIENT'], // Fixed role
      nik: ['', [Validators.required, Validators.pattern('^[0-9]{16}$')]],
      phoneNumber: [
        '',
        [Validators.required, Validators.pattern('^[0-9]{10,12}$')],
      ],
      address: ['', [Validators.required]],
      gender: ['', [Validators.required]],
      dateOfBirth: ['', [Validators.required]],
    });
  }

  onSubmit(): void {
    if (this.patientForm.valid) {
      // Create the user first
      const userSaveDTO: UserSaveDTO = {
        full_name: this.patientForm.get('full_name')?.value,
        email: this.patientForm.get('email')?.value,
        password: this.patientForm.get('password')?.value,
        role: this.patientForm.get('role')?.value,
      };

      this.userService.createUser(userSaveDTO).subscribe({
        next: (user: User) => {
          this.createdUserId = user.id;
          console.log('User created successfully:');

          // Now create the patient using the user ID
          this.createPatient();
        },
        error: (error: any) => {
          alert('Failed to update user');
          console.error('Error:', error);
        },
      });
    } else {
      console.log('Form is invalid');
      this.patientForm.markAllAsTouched();
    }
  }

  // Separate method to create the patient once the user is created
  createPatient(): void {
    const patientSaveDTO: PatientSaveDTO = {
      user_id: this.createdUserId, // Use the user ID from the created user
      nik: this.patientForm.get('nik')?.value,
      phoneNumber: this.patientForm.get('phoneNumber')?.value,
      address: this.patientForm.get('address')?.value,
      gender: this.patientForm.get('gender')?.value,
      dateOfBirth: this.patientForm.get('dateOfBirth')?.value,
    };

    this.patientService.createPatient(patientSaveDTO).subscribe({
      next: () => {
        alert('User updated successfully!');
        this.router.navigate(['/admin/dashboard/users']);
      },
      error: (error: any) => {
        alert('Failed to update user');
        console.error('Error:', error);
      },
    });
  }
}
