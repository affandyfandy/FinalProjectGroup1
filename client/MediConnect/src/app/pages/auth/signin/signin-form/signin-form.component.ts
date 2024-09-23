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
import { UserRequestLogin } from '../../../../models/user.model';
import { AuthService } from '../../../../services/auth-service/auth.service';
import { ToastContainerDirective, ToastrService } from 'ngx-toastr';
import {
  MessageInvalidLogin,
  MessageValidLogin,
} from '../../../../utils/message';
import {
  MessageInvalidValidation,
  MessageError,
  MessageValidationPasswordSize,
  MessageValidationPasswordPattern,
  MessageErrorRequiredField,
  MessageValidationEmailPattern,
} from '../../../../utils/message';

@Component({
  selector: 'app-signin-form',
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
  templateUrl: './signin-form.component.html',
  styleUrl: './signin-form.component.css',
})
export class SigninFormComponent implements OnInit {
  user: UserRequestLogin = { email: '', password: '' };
  showPassword: boolean = false;

  signinForm!: FormGroup;
  showConfirmPassword: boolean = false;

  messageInvalidValidation = MessageInvalidValidation;
  messageErrorGeneral = MessageError;
  messageSuccesLogin = MessageValidLogin;
  messageInvalidLogin = MessageInvalidLogin;
  messageValidationPasswordSize = MessageValidationPasswordSize;
  messageValidationPasswordPattern = MessageValidationPasswordPattern;
  messageErrorField = MessageErrorRequiredField;
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

    this.signinForm = this.fb.group({
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
    });
  }

  login() {
    if (this.signinForm.valid) {
      const loginData = this.signinForm.value;
      const loginUser: UserRequestLogin = {
        email: loginData.email,
        password: loginData.password,
      };
      this.authService.login(loginUser).subscribe({
        next: (response) => {
          if (response) {
            this.authService.setToken(response.token);
            this.authService.profile().subscribe({
              next: (profileResponse) => {
                const userRole = profileResponse.role;
                this.authService.setRole(userRole);
                this.toastrService.success(
                  'Welcome Back!!',
                  this.messageSuccesLogin
                );
                if (userRole === 'PATIENT') {
                  this.router.navigate(['/dashboard']);
                } else if (userRole === 'ADMIN' || userRole === 'SUPERADMIN') {
                  this.router.navigate(['/admin/dashboard']);
                } else {
                  this.toastrService.error('Invalid user role');
                }
              },
              error: (profileError) => {
                this.toastrService.error(this.messageErrorGeneral);
              },
            });
          }
        },
        error: (error) => {
          console.log('ERROR : ', error);
          if (error.status === 401) {
            this.toastrService.error(
              'Try Again!!',
              error.error || this.messageInvalidValidation
            );
          } else {
            this.toastrService.error(this.messageErrorGeneral);
          }
        },
      });
    }
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  get email() {
    return this.signinForm.get('email');
  }

  get password() {
    return this.signinForm.get('password');
  }
}
