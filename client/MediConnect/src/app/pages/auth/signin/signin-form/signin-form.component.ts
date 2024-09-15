import { Component } from '@angular/core';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { bootstrapEyeSlash } from '@ng-icons/bootstrap-icons';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../../services/auth-service/auth.service';
import { User, UserRequestLogin } from '../../../../models/user.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-signin-form',
  standalone: true,
  imports: [
    HlmInputDirective,
    HlmButtonDirective,
    HlmLabelDirective,
    HlmIconComponent,
    NgIconComponent,
    RouterModule,
    FormsModule,
    CommonModule,
  ],
  viewProviders: [provideIcons({ bootstrapEyeSlash })],
  templateUrl: './signin-form.component.html',
  styleUrl: './signin-form.component.css',
})
export class SigninFormComponent {
  constructor(private authService: AuthService, private router: Router) {}
  user: UserRequestLogin = { email: '', password: '' };

  login() {
    this.authService.login(this.user).subscribe({
      next: (response) => {
        if (response) {
          console.log(response);
          this.authService.setToken(response.token);
          alert('Login successful!');
          this.router.navigate(['/']);
        } else {
          alert(response.error || 'Unexpected error occurred.');
        }
      },
      error: (error) => {
        if (error.status === 401) {
          alert(error.error.error);
        }
      },
    });
  }
}
