import { Component } from '@angular/core';
import { SignupFormComponent } from '../signup-form/signup-form.component';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [SignupFormComponent, RouterModule],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css',
})
export class SignupComponent {}
