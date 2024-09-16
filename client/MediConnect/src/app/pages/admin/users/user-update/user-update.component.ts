import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserFormComponent } from '../user-form/user-form.component';

@Component({
  selector: 'app-user-update',
  standalone: true,
  imports: [UserFormComponent],
  templateUrl: './user-update.component.html',
  styleUrl: './user-update.component.css',
})
export class UserUpdateComponent implements OnInit {
  patientId: string = '';

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.patientId = params['id']; // Access the 'id' parameter from the URL
      console.log('Test ID:', this.patientId);
    });
  }
}
