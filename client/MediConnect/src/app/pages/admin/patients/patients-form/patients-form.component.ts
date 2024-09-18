import { Component, Input, OnInit } from '@angular/core';
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
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
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
  @Input() isEditMode: boolean = false; // Input to check if it's edit mode
  @Input() patientId: string | null = null; // Input to store patient ID for update

  patientForm!: FormGroup;
  createdUserId: string = ''; // To store the created or fetched user ID

  constructor(
    private fb: FormBuilder,
    private patientService: PatientsService,
    private userService: UserService,
    private router: Router,
    private toastr: ToastrService,
    private route: ActivatedRoute // For retrieving route parameters
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

    // If it's edit mode and patientId is provided, load the patient data
    if (this.isEditMode && this.patientId) {
      this.loadPatientData(this.patientId);
    }
  }

  loadPatientData(id: string): void {
    this.patientService.getPatientById(id).subscribe((patient) => {
      this.createdUserId = patient.user.id; // Get the associated user ID
      this.patientForm.patchValue({
        full_name: patient.user.full_name,
        email: patient.user.email,
        nik: patient.nik,
        phoneNumber: patient.phoneNumber,
        address: patient.address,
        gender: patient.gender,
        dateOfBirth: patient.dateOfBirth,
      });
    });
  }

  onSubmit(): void {
    if (this.patientForm.valid) {
      // If it's edit mode, update the patient, otherwise create a new one
      if (this.isEditMode && this.patientId) {
        this.updatePatient();
      } else {
        this.createUserAndPatient();
      }
    } else {
      console.log('Form is invalid');
      this.patientForm.markAllAsTouched();
    }
  }

  // Separate method to handle user and patient creation
  createUserAndPatient(): void {
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
        this.createPatient();
      },
      error: (error: any) => {
        alert('Failed to create user');
        console.error('Error:', error);
      },
    });
  }

  // Method to create a new patient after user creation
  createPatient(): void {
    const patientSaveDTO: PatientSaveDTO = {
      user_id: this.createdUserId,
      nik: this.patientForm.get('nik')?.value,
      phoneNumber: this.patientForm.get('phoneNumber')?.value,
      address: this.patientForm.get('address')?.value,
      gender: this.patientForm.get('gender')?.value,
      dateOfBirth: this.patientForm.get('dateOfBirth')?.value,
    };

    this.patientService.createPatient(patientSaveDTO).subscribe({
      next: () => {
        this.toastr.success('Patient created successfully!');
        this.router.navigate(['/admin/dashboard/patients']);
      },
      error: (error: any) => {
        this.toastr.error('Failed to create patient');
        console.error('Error:', error);
      },
    });
  }

  // Method to update an existing patient
  updatePatient(): void {
    const patientSaveDTO: PatientSaveDTO = {
      user_id: this.createdUserId,
      nik: this.patientForm.get('nik')?.value,
      phoneNumber: this.patientForm.get('phoneNumber')?.value,
      address: this.patientForm.get('address')?.value,
      gender: this.patientForm.get('gender')?.value,
      dateOfBirth: this.patientForm.get('dateOfBirth')?.value,
    };

    this.patientService
      .updatePatient(this.patientId!, patientSaveDTO)
      .subscribe({
        next: (response) => {
          console.log(response);
          this.toastr.success('Patient updated successfully!');
          this.router.navigate(['/admin/dashboard/patients']);
        },
        error: (error: any) => {
          this.toastr.error('Failed to update patient');
          console.error('Error:', error);
        },
      });
  }

  // Getters for form controls
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
