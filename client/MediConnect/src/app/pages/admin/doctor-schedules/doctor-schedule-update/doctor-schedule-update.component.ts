import { Component, Input, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { HlmInputDirective } from '@spartan-ng/ui-input-helm';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { HlmLabelDirective } from '@spartan-ng/ui-label-helm';
import { HlmIconComponent } from '@spartan-ng/ui-icon-helm';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { DoctorScheduleEditFormComponent } from '../doctor-schedule-edit-form/doctor-schedule-edit-form.component';

@Component({
  selector: 'app-doctor-schedule-update',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HlmInputDirective,
    HlmButtonDirective,
    HlmLabelDirective,
    HlmIconComponent,
    NgIconComponent,
    RouterModule,
    DoctorScheduleEditFormComponent
  ],
  templateUrl: './doctor-schedule-update.component.html',
  styleUrl: './doctor-schedule-update.component.css'
})
export class DoctorScheduleUpdateComponent {

}
