import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import {
  bootstrapPeopleFill,
  bootstrapCalendar2Fill,
  bootstrapPersonFillCheck,
  bootstrapWallet2,
  bootstrapCalendar2WeekFill,
  bootstrapClockFill,
  bootstrapHouse,
  bootstrapPersonHearts,
  bootstrapDoorOpen,
} from '@ng-icons/bootstrap-icons';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { User } from '../../../models/user.model';
import { AuthService } from '../../../services/auth-service/auth.service';
@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [NgIconComponent, RouterModule],
  viewProviders: [
    provideIcons({
      bootstrapPeopleFill,
      bootstrapCalendar2Fill,
      bootstrapPersonFillCheck,
      bootstrapDoorOpen,
      bootstrapWallet2,
      bootstrapCalendar2WeekFill,
      bootstrapClockFill,
      bootstrapPersonHearts,
      bootstrapHouse,
    }),
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css',
})
export class SidebarComponent implements OnInit {
  isLoggedIn = false;

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit(): void {}

  handleLogout() {
    this.isLoggedIn = false; // Set loggedIn to false
    this.authService.logout();
    this.router.navigate(['/signin']); // Redirect to login page after logout
  }
}
