import { Component } from '@angular/core';
import { SigninFormComponent } from '../signin-form/signin-form.component';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [SigninFormComponent],
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.css',
})
export class SigninComponent {}
