import { Component } from '@angular/core';
import { UserFormComponent } from '../user-form/user-form.component';

@Component({
  selector: 'app-user-create',
  standalone: true,
  imports: [UserFormComponent],
  templateUrl: './user-create.component.html',
  styleUrl: './user-create.component.css',
})
export class UserCreateComponent {}
