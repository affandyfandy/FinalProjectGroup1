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
  @Input() isEditMode: boolean = false;
  @Input() patientId: string | null = null;

  patientForm!: FormGroup;
  createdUserId: string = '';

  constructor(
    private fb: FormBuilder,
    private patientService: PatientsService,
    private userService: UserService,
    private router: Router,
    private toastr: ToastrService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.patientForm = this.fb.group({
      editUserAccount: [false],
      fullName: [
        '',
        [Validators.required, Validators.pattern('^[a-zA-Z\\s]+$')],
      ],
      email: ['', [Validators.required, Validators.email]],
      password: ['Password123!'],
      role: ['PATIENT'],
      nik: ['', [Validators.required, Validators.pattern('^[0-9]{16}$')]],
      phoneNumber: [
        '',
        [Validators.required, Validators.pattern('^0[0-9]{9,19}$')],
      ],
      address: ['', [Validators.required]],
      gender: ['', [Validators.required]],
      dateOfBirth: ['', [Validators.required]],
    });

    if (this.isEditMode && this.patientId) {
      this.loadPatientData(this.patientId);
    }

    this.patientForm.get('editUserAccount')?.valueChanges.subscribe((value) => {
      if (value) {
        this.patientForm.get('fullName')?.enable();
        this.patientForm.get('email')?.enable();
      } else {
        this.patientForm.get('fullName')?.disable();
        this.patientForm.get('email')?.disable();
      }
    });
  }

  loadPatientData(id: string): void {
    this.patientService.getPatientById(id).subscribe((patient) => {
      this.createdUserId = patient.user.id;
      this.patientForm.patchValue({
        fullName: patient.user.fullName,
        email: patient.user.email,
        nik: patient.nik,
        phoneNumber: patient.phoneNumber,
        address: patient.address,
        gender: patient.gender,
        dateOfBirth: patient.dateOfBirth,
      });
      this.patientForm.get('fullName')?.disable();
      this.patientForm.get('email')?.disable();
    });
  }

  onSubmit(): void {
    if (this.patientForm.valid) {
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

  createUserAndPatient(): void {
    const userSaveDTO: UserSaveDTO = {
      fullName: this.patientForm.get('fullName')?.value,
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
        this.toastr.error('Failed to create user');
        console.error('Error:', error);
      },
    });
  }

  createPatient(): void {
    const patientSaveDTO: PatientSaveDTO = {
      userId: this.createdUserId,
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

  updatePatient(): void {
    const patientSaveDTO: PatientSaveDTO = {
      userId: this.createdUserId,
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

  updateUser(): void {
    const userUpdateDTO: UserSaveDTO = {
      fullName: this.patientForm.get('fullName')?.value,
      email: this.patientForm.get('email')?.value,
      role: this.patientForm.get('role')?.value,
      password: this.patientForm.get('password')?.value,
    };

    this.userService.updateUserPatient(this.createdUserId, userUpdateDTO).subscribe({
      next: (response) => {
        console.log(response);
        this.toastr.success('User updated successfully!');
      },
      error: (error: any) => {
        this.toastr.error('Failed to update user');
        console.error('Error:', error);
      },
    });
  }

  // Getters for form controls
  get editUserAccount() {
    return this.patientForm.get('editUserAccount');
  }

  get fullName() {
    return this.patientForm.get('fullName');
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
