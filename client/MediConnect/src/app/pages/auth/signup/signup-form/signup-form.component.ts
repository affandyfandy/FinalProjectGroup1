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
import { bootstrapEye, bootstrapEyeSlash } from '@ng-icons/bootstrap-icons';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UserRequestRegister } from '../../../../models/user.model';
import { AuthService } from '../../../../services/auth-service/auth.service';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import {
  MessageInvalidValidation,
  MessageError,
  MessageValidRegister,
  MessageValidationPasswordSize,
  MessageValidationPasswordPattern,
  MessageErrorRequiredField,
  MessageValidationNIKPattern,
  MessageValidationFullnamePattern,
  MessageValidationEmailPattern,
} from '../../../../utils/message';
@Component({
  selector: 'app-signup-form',
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
  viewProviders: [provideIcons({ bootstrapEye, bootstrapEyeSlash })],
  templateUrl: './signup-form.component.html',
  styleUrls: ['./signup-form.component.css'],
})
export class SignupFormComponent implements OnInit {
  signupForm!: FormGroup;
  showPassword: boolean = false;
  showConfirmPassword: boolean = false;
  
  messageInvalidValidation = MessageInvalidValidation;
  messageErrorGeneral = MessageError;
  messageSuccesRegister = MessageValidRegister;
  messageValidationPasswordSize = MessageValidationPasswordSize;
  messageValidationPasswordPattern = MessageValidationPasswordPattern;
  messageErrorField = MessageErrorRequiredField;
  messageValidationNIKPattern = MessageValidationNIKPattern;
  messageValidationFullnamePattern = MessageValidationFullnamePattern;
  messageValidationEmailPattern = MessageValidationEmailPattern;

  @ViewChild(ToastContainerDirective, { static: true })
  toastContainer!: ToastContainerDirective;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toastrService: ToastrService
  ) {}

  ngOnInit(): void {
    this.toastrService.overlayContainer = this.toastContainer;

    this.signupForm = this.fb.group(
      {
        nik: ['', [Validators.required, Validators.pattern('^[0-9]{16}$')]],
        fullName: [
          '',
          [Validators.required, Validators.pattern('^[a-zA-Z\\s]+$')],
        ],
        email: ['', [Validators.required, Validators.email]],
        password: [
          '',
          [
            Validators.required,
            Validators.minLength(12),
            Validators.pattern(
              /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$%^&+=!]).+$/
            ),
          ],
        ],
        confirmPassword: ['', Validators.required],
      },
      { validators: this.passwordsMatchValidator } // Synchronous validator
    );
  }

  // Custom validator to check if passwords match
  passwordsMatchValidator(form: FormGroup) {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordsMismatch: true };
  }

  register() {
    if (this.signupForm.valid) {
      const registerData = this.signupForm.value;
      const saveData: UserRequestRegister = {
        nik: registerData.nik,
        fullName: registerData.fullName,
        email: registerData.email,
        password: registerData.password,
        role: 'PATIENT',
      };
      this.authService.register(saveData).subscribe({
        next: (response) => {
          if (response) {
            this.toastrService.success(this.messageSuccesRegister);
            this.router.navigate(['/signin']);
          }
        },
        error: (error) => {
          // Improved error handling
          if (error.status === 409) {
            this.toastrService.error(
              'Try Again!!',
              error.error || this.messageErrorGeneral
            );
          } else {
            this.toastrService.error(this.messageErrorGeneral);
          }
          console.log('Error:', error);
        },
      });
    }
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPasswordVisibility() {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  // Getters for easy access in template
  get nik() {
    return this.signupForm.get('nik');
  }

  get fullName() {
    return this.signupForm.get('fullName');
  }

  get email() {
    return this.signupForm.get('email');
  }

  get password() {
    return this.signupForm.get('password');
  }

  get confirmPassword() {
    return this.signupForm.get('confirmPassword');
  }
}
