import { Component, OnInit } from '@angular/core';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { RouterModule, Router } from '@angular/router';
import { UserService } from '../../../../services/user-service/users-service.service';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [RouterModule, HlmButtonDirective],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css',
})
export class UserListComponent implements OnInit {
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
