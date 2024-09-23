import { Component, Input } from '@angular/core';
import { User } from '../../../../models/user.model';
import { CommonModule } from '@angular/common';
import { TimeAgoPipe } from '../../../../core/pipes/time-ago.pipe';
@Component({
  selector: 'app-user-modal',
  standalone: true,
  imports: [CommonModule, TimeAgoPipe],
  templateUrl: './user-modal.component.html',
  styleUrl: './user-modal.component.css',
})
export class UserModalComponent {
  @Input() user!: User;

  // Boolean to toggle between formats
  showDate = false;

  // Function to toggle the value based on checkbox state
  onToggleFormat(event: any) {
    this.showDate = event.target.checked;
  }
}
