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
import { AuthService } from '../../../services/auth-service/auth.service';
@Component({
  selector: 'app-sidebar-user',
  standalone: true,
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
  imports: [NgIconComponent, RouterModule],
  templateUrl: './sidebar-user.component.html',
  styleUrl: './sidebar-user.component.css',
})
export class SidebarUserComponent implements OnInit {
  isLoggedIn = false;

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit(): void {}

  handleLogout() {
    this.isLoggedIn = false; // Set loggedIn to false
    this.authService.logout();
    this.router.navigate(['/signin']); // Redirect to login page after logout
  }
}
