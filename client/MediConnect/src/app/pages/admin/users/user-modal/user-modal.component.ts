import { Component, Input } from '@angular/core';
import { User } from '../../../../models/user.model';

@Component({
  selector: 'app-user-modal',
  standalone: true,
  imports: [],
  templateUrl: './user-modal.component.html',
  styleUrl: './user-modal.component.css',
})
export class UserModalComponent {
  @Input() user!: User;
}
