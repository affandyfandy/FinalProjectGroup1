import { Component, OnInit } from '@angular/core';
import { ServiceBoxComponent } from './service-box/service-box.component';
import { UserService } from '../../services/user-service/users-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [ServiceBoxComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit {
  constructor(private userService: UserService, private router: Router) {}
  users = [];
  ngOnInit() {
    this.loadPublicUser();
    this.loadPublicUserRestricted();
  }

  loadPublicUser() {
    this.userService.getUsers().subscribe((response) => {
      this.users = response.content;
      console.log(response);
    });
  }

  loadPublicUserRestricted() {
    this.userService.getUsersRestricted().subscribe((response) => {
      this.users = response.content;
      console.log(response);
    });
  }
}
