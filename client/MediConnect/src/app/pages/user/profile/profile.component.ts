import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { NgIconComponent } from '@ng-icons/core';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth-service/auth.service';
import { PatientsService } from '../../../services/patient-service/patients.service';
import { UserService } from '../../../services/user-service/users.service';
import { jwtDecode } from 'jwt-decode';
import { CustomJwtPayload } from '../../../models/user.model';
import { PatientShowDTO, PatientSaveDTO } from '../../../models/patient.model';
import { UserSaveDTO } from '../../../models/user.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-profile',
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
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent implements OnInit {
  profileForm: FormGroup;
  token: string = '';
  userId: string = '';
  role: string = '';
  patientId: string = '';
  patient: PatientShowDTO | null = null;
  isUserEditing: boolean = false;
  isPatientEditing: boolean = false;

  constructor(
    private authService: AuthService,
    private patientsService: PatientsService,
    private userService: UserService,
    private formBuilder: FormBuilder,
    private toastr: ToastrService,
    private router: Router
  ) {
    this.profileForm = this.formBuilder.group({
      fullName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: [
        '',
        [Validators.required, Validators.pattern(/^\d{10,20}$/)],
      ],
      gender: ['', Validators.required],
      nik: ['', [Validators.required, Validators.pattern(/^\d{16}$/)]],
      address: ['', Validators.required],
      dateOfBirth: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile() {
    const token = this.authService.getToken();
    if (token) {
      this.token = token;
      const decoded = jwtDecode<CustomJwtPayload>(token);
      this.userId = decoded.id;
      this.role = decoded.role;
      if (this.userId) {
        this.patientsService.getPatientByUserId(this.userId).subscribe(
          (response) => {
            this.patient = response;
            this.patientId = this.patient.id;
            this.loadPatientData(this.patientId);
          },
          (error) => {
            console.error('Error loading profile', error);
          }
        );
      }
    } else {
      console.error('Token is null');
    }
  }

  loadPatientData(id: string): void {
    this.patientsService.getPatientById(id).subscribe((patient) => {
      this.profileForm.patchValue({
        fullName: patient.user.fullName,
        email: patient.user.email,
        nik: patient.nik,
        phoneNumber: patient.phoneNumber,
        address: patient.address,
        gender: patient.gender,
        dateOfBirth: patient.dateOfBirth,
      });
      this.disableForm();
    });
  }

  toggleEdit(section: 'user' | 'patient'): void {
    if (section === 'user') {
      this.isUserEditing = !this.isUserEditing;
      this.isUserEditing ? this.enableUserForm() : this.disableUserForm();
    } else if (section === 'patient') {
      this.isPatientEditing = !this.isPatientEditing;
      this.isPatientEditing
        ? this.enablePatientForm()
        : this.disablePatientForm();
    }
  }

  enableUserForm(): void {
    this.profileForm.get('fullName')?.enable();
    this.profileForm.get('email')?.enable();
  }

  disableUserForm(): void {
    this.profileForm.get('fullName')?.disable();
    this.profileForm.get('email')?.disable();
  }

  enablePatientForm(): void {
    this.profileForm.get('phoneNumber')?.enable();
    this.profileForm.get('gender')?.enable();
    this.profileForm.get('nik')?.enable();
    this.profileForm.get('address')?.enable();
    this.profileForm.get('dateOfBirth')?.enable();
  }

  disablePatientForm(): void {
    this.profileForm.get('phoneNumber')?.disable();
    this.profileForm.get('gender')?.disable();
    this.profileForm.get('nik')?.disable();
    this.profileForm.get('address')?.disable();
    this.profileForm.get('dateOfBirth')?.disable();
  }

  updateUser(): void {
    const userUpdateDTO: UserSaveDTO = {
      fullName: this.profileForm.get('fullName')?.value,
      email: this.profileForm.get('email')?.value,
      role: this.role,
      password: 'Password123!@',
    };

    this.userService.updateUserPatient(this.userId, userUpdateDTO).subscribe({
      next: (response: any) => {
        console.log(response);

        this.toastr.success('User updated successfully!');
      },
      error: (error: any) => {
        this.toastr.error('Failed to update user');
        console.error('Error:', error);
      },
    });
  }

  onSubmit(): void {
    if (this.isUserEditing) {
      this.updateUser();
    }

    if (this.isPatientEditing) {
      const updatedPatient: PatientSaveDTO = {
        userId: this.userId,
        phoneNumber: this.profileForm.get('phoneNumber')?.value,
        gender: this.profileForm.get('gender')?.value,
        nik: this.profileForm.get('nik')?.value,
        address: this.profileForm.get('address')?.value,
        dateOfBirth: this.profileForm.get('dateOfBirth')?.value,
      };

      this.patientsService
        .updatePatient(this.patientId, updatedPatient)
        .subscribe(
          (response) => {
            this.toastr.success('Patient updated successfully!');
            this.isPatientEditing = false;
            this.disablePatientForm();
          },
          (error) => {
            console.error('Error updating profile', error);
            this.toastr.error('Patient error updating');
          }
        );
    }
  }

  disableForm(): void {
    this.disableUserForm();
    this.disablePatientForm();
  }
}
