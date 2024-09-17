import { Component, Input } from '@angular/core';
import { User } from '../../../../models/user.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-modal.component.html',
  styleUrl: './user-modal.component.css',
})
export class UserModalComponent {
  @Input() user!: User;
}
