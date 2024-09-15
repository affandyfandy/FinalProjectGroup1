import { Component, OnInit } from '@angular/core';
import { SigninFormComponent } from '../signin-form/signin-form.component';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../../services/auth-service/auth.service';
import { User, UserRequestLogin } from '../../../../models/user.model';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [SigninFormComponent, RouterModule],
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.css',
})
export class SigninComponent {
  constructor(private authService: AuthService, private router: Router) {}
  user: UserRequestLogin = { email: '', password: '' };

  login() {
    this.authService.login(this.user).subscribe({
      next: (response) => {
        if (response) {
          console.log(response);
          this.authService.setToken(response);
          alert('Login successful!');
          // this.router.navigate(['/']);
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
