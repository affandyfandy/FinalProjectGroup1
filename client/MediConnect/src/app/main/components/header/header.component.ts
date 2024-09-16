import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { AuthService } from '../../../services/auth-service/auth.service';
import { User } from '../../../models/user.model';
import {
  HlmAvatarImageDirective,
  HlmAvatarComponent,
  HlmAvatarFallbackDirective,
} from '@spartan-ng/ui-avatar-helm';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule,
    HlmButtonDirective,
    RouterModule,
    HlmAvatarImageDirective,
    HlmAvatarComponent,
    HlmAvatarFallbackDirective,
    HlmButtonDirective,
  ], // Import necessary modules
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  username = ''; // Store the username
  users: User | null = null; // Store the user data
  isMenuOpen = false;
  showLogout = false;
  isOpen = false;
  isLoggedIn = false;
  isAdmin = true;

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit(): void {
    this.loadProfile(); // Load user profile on init
    this.checkAdminRole();
  }

  checkAdminRole(): void {
    if (this.authService.getRole() === 'PATIENT') {
      this.isAdmin = false;
    }
  }

  loadProfile() {
    this.authService.profile().subscribe(
      (response) => {
        this.users = response;
        if (this.users) {
          this.username = this.users.full_name;
          this.isLoggedIn = true; // Set isLoggedIn to true
        }
      },
      (error) => {
        console.error('Error loading profile', error);
      }
    );
  }

  toggleMenu() {
    this.isOpen = !this.isOpen; // Toggle menu visibility
  }

  handleLogout() {
    this.isLoggedIn = false; // Set loggedIn to false
    this.authService.logout();
    this.router.navigate(['/signin']); // Redirect to login page after logout
  }
}
