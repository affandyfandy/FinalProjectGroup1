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
import { jwtDecode } from 'jwt-decode';
import { CustomJwtPayload } from '../../../models/user.model';
import { PatientShowDTO } from '../../../models/patient.model';

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
  profileForm: FormGroup; // Form group for profile data
  token: string = '';
  userId: string = '';
  patientId: string = '';
  patient: PatientShowDTO | null = null;

  constructor(
    private authService: AuthService,
    private patientsService: PatientsService,
    private formBuilder: FormBuilder // Using FormBuilder to create the form
  ) {
    // Initialize the form with empty and disabled controls
    this.profileForm = this.formBuilder.group({
      fullName: [{ value: '', disabled: true }, Validators.required],
      email: [
        { value: '', disabled: true },
        [Validators.required, Validators.email],
      ],
      phoneNumber: [{ value: '', disabled: true }, Validators.required],
      gender: [{ value: '', disabled: true }, Validators.required],
      nik: [
        { value: '', disabled: true },
        [Validators.required, Validators.pattern('^[0-9]{16}$')],
      ],
      address: [{ value: '', disabled: true }, Validators.required],
      dateOfBirth: [{ value: '', disabled: true }, Validators.required],
    });
  }

  ngOnInit(): void {
    // Initialize the form group with default values and validators
    this.profileForm = this.formBuilder.group({
      fullName: [{ value: '', disabled: true }, [Validators.required]],
      email: [
        { value: '', disabled: true },
        [Validators.required, Validators.email],
      ],
      phoneNumber: [
        { value: '', disabled: true },
        [Validators.required, Validators.pattern(/^\d{10,20}$/)],
      ],
      gender: [{ value: '', disabled: true }, [Validators.required]],
      nik: [
        { value: '', disabled: true },
        [Validators.required, Validators.pattern(/^\d{16}$/)],
      ],
      address: [{ value: '', disabled: true }, [Validators.required]],
      dateOfBirth: [{ value: '', disabled: true }, [Validators.required]],
    });
    this.loadProfile();
  }

  // Function to load profile data
  loadProfile() {
    const token = this.authService.getToken();
    if (token) {
      this.token = token;
      const decoded = jwtDecode<CustomJwtPayload>(token);
      this.userId = decoded.id;

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

  // Function to load patient data into the form
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
    });
  }
}
