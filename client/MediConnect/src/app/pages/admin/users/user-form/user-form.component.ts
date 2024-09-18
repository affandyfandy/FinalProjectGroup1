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
import { UserSaveDTO, UserShowDTO } from '../../../../models/user.model';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../../../../services/user-service/users.service';

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
  @Input() isEditMode: boolean = false; // Input to check if it's edit mode
  @Input() userId: string | null = null; // Input to store user ID for update

  userForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private userService: UserService,
    private toastr: ToastrService,
    private route: ActivatedRoute // Activated route to read params
  ) {}

  ngOnInit(): void {
    // Initialize the form
    this.userForm = this.fb.group({
      fullName: [
        '',
        [Validators.required, Validators.pattern('^[a-zA-Z\\s]+$')],
      ],
      email: ['', [Validators.required, Validators.email]],
      password: ['Password123!!'],
      role: ['ADMIN'],
    });

    // Check if we are in edit mode
    if (this.isEditMode && this.userId) {
      this.loadUserData(this.userId); // Load user data for edit mode
    }
  }

  // Load existing user data in case of edit
  loadUserData(userId: string): void {
    this.userService.getUserById(userId).subscribe({
      next: (user: UserShowDTO) => {
        this.userForm.patchValue({
          fullName: user.fullName,
          email: user.email,
          role: user.role,
        });
        // We don't patch password as it is usually not exposed
      },
      error: (err) => {
        this.toastr.error('Failed to load user data');
        console.error(err);
      },
    });
  }

  // Method to handle form submission
  onSubmit(): void {
    if (this.userForm.valid) {
      const formData = this.userForm.value as UserSaveDTO;

      if (this.isEditMode && this.userId) {
        // If in edit mode, update the user
        this.userService.updateUser(this.userId, formData).subscribe({
          next: () => {
            this.toastr.success('User updated successfully!');
            this.router.navigate(['/admin/dashboard/users']);
          },
          error: (error: any) => {
            this.toastr.error('Failed to update user');
            console.error('Error:', error);
          },
        });
      } else {
        // Otherwise, create a new user
        this.userService.createUser(formData).subscribe({
          next: () => {
            this.toastr.success('User created successfully!');
            this.router.navigate(['/admin/dashboard/users']);
          },
          error: (error: any) => {
            this.toastr.error('Failed to create user');
            console.error('Error:', error);
          },
        });
      }
    } else {
      this.toastr.error('Form is invalid');
      this.userForm.markAllAsTouched();
    }
  }

  get fullName() {
    return this.userForm.get('fullName');
  }

  get email() {
    return this.userForm.get('email');
  }
}
