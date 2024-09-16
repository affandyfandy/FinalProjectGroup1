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

@Component({
  selector: 'app-user-form',
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
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.css',
})
export class UserFormComponent implements OnInit {
  userForm!: FormGroup;

  constructor(private fb: FormBuilder, private router: Router) {}

  ngOnInit(): void {
    this.userForm = this.fb.group({
      full_name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['password123'],
      role: ['ADMIN'], // Since role is fixed, we hard-code it
    });
  }

  // Method to handle form submission
  onSubmit(): void {
    if (this.userForm.valid) {
      const formData = this.userForm.value;
      console.log('User data submitted:', formData);
      // Perform your logic here, e.g., send formData to an API.
    } else {
      console.log('Form is invalid');
      this.userForm.markAllAsTouched(); // Mark all controls as touched to show validation messages
    }
  }

  get full_name() {
    return this.userForm.get('full_name');
  }

  get email() {
    return this.userForm.get('email');
  }
}
