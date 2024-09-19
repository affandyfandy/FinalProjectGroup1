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
import { CommonModule } from '@angular/common';
import { DoctorDTO } from '../../../../models/doctor.model';
import { ToastrService } from 'ngx-toastr';
import { DoctorsService } from '../../../../services/doctor-service/doctors.service';

@Component({
  selector: 'app-doctor-form',
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
  templateUrl: './doctor-form.component.html',
  styleUrl: './doctor-form.component.css'
})
export class DoctorFormComponent implements OnInit {
  @Input() isEditMode: boolean = false;
  @Input() doctorId: string | null = null;

  doctorForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private doctorService: DoctorsService,
    private toastr: ToastrService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.doctorForm = this.fb.group({
      name: ['', [Validators.required]],
      specialization: ['', [Validators.required]],
      identificationNumber: ['', [Validators.required, Validators.pattern('^[0-9]{6}$')]],
      phoneNumber: [
        '',
        [Validators.required],
      ],
      gender: ['', [Validators.required]],
      dateOfBirth: ['', [Validators.required]],
      address: ['', [Validators.required]],
      patientTotal: [''],
    });

    if (this.isEditMode && this.doctorId) {
      this.loadDoctorData(this.doctorId);
    }
  }

  loadDoctorData(doctorId: string): void {
    this.doctorService.getDoctorById(doctorId).subscribe({
      next: (doctor: DoctorDTO) => {
        this.doctorForm.patchValue({
          name: doctor.name,
          specialization: doctor.specialization,
          identificationNumber: doctor.identificationNumber,
          phoneNumber: doctor.phoneNumber,
          gender: doctor.gender,
          dateOfBirth: doctor.dateOfBirth,
          address: doctor.address,
          patientTotal: doctor.patientTotal
        });
      },
      error: (err) => {
        this.toastr.error('Failed to load doctor data');
        console.error(err);
      },
    });
  }

  onSubmit(): void {
    if (this.doctorForm.valid) {
      const formData = this.doctorForm.value as DoctorDTO;

      if (this.isEditMode && this.doctorId) {
        this.doctorService.updateDoctor(this.doctorId, formData).subscribe({
          next: () => {
            this.toastr.success('Doctor updated successfully!');
            this.router.navigate(['/admin/dashboard/doctors']);
          },
          error: (error: any) => {
            this.handleServerError(error);
            console.error('Error:', error);
          },
        });
      } else {
        this.doctorService.createDoctor(formData).subscribe({
          next: () => {
            this.toastr.success('Doctor created successfully!');
            this.router.navigate(['/admin/dashboard/doctors']);
          },
          error: (error: any) => {
            this.handleServerError(error);
          },
        });
      }
    } else {
      this.toastr.error('Form is invalid');
      this.doctorForm.markAllAsTouched();
    }
  }

  handleServerError(error: any): void {
    if (error.status === 409 ) {
      this.doctorForm.get('identificationNumber')?.setErrors({ duplicate: true });
    } else {
      this.toastr.error('Failed to create doctor.');
    }
  }

  get name() {
    return this.doctorForm.get('name');
  }

  get specialization() {
    return this.doctorForm.get('specialization');
  }

  get identificationNumber() {
    return this.doctorForm.get('identificationNumber');
  }

  get phoneNumber() {
    return this.doctorForm.get('phoneNumber');
  }

  get gender() {
    return this.doctorForm.get('gender');
  }

  get dateOfBirth() {
    return this.doctorForm.get('dateOfBirth');
  }

  get address() {
    return this.doctorForm.get('address');
  }
}
