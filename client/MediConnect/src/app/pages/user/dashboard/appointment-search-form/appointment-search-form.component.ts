import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { bootstrapEye, bootstrapEyeSlash } from '@ng-icons/bootstrap-icons';

@Component({
  selector: 'app-appointment-search-form',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    HlmInputDirective,
    HlmButtonDirective,
    HlmLabelDirective,
    HlmIconComponent,
    NgIconComponent,
    RouterModule,
  ],
  templateUrl: './appointment-search-form.component.html',
  styleUrls: ['./appointment-search-form.component.css'],
})
export class AppointmentSearchFormComponent {
  doctorName: string = '';
  appointmentDate: string = '';
  today: string;

  constructor(private router: Router) {
    const todayDate = new Date();
    const dd = String(todayDate.getDate()).padStart(2, '0');
    const mm = String(todayDate.getMonth() + 1).padStart(2, '0'); // January is 0!
    const yyyy = todayDate.getFullYear();

    this.today = `${yyyy}-${mm}-${dd}`;
  }

  onSearch() {
    let queryParams: any = {};

    if (this.doctorName) {
      queryParams['doctorName'] = this.doctorName;
    }

    if (this.appointmentDate) {
      queryParams['date'] = this.appointmentDate;
    }

    if (Object.keys(queryParams).length > 0) {
      this.router.navigate(['/dashboard/appointment/search'], { queryParams });
    }
  }
}
