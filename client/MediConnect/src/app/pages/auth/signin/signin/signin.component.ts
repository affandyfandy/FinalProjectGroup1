import { Component } from '@angular/core';
import { SigninFormComponent } from '../signin-form/signin-form.component';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [SigninFormComponent, RouterModule],
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.css',
})
export class SigninComponent {}
