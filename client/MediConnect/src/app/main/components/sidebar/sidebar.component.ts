import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import {
  bootstrapPeopleFill,
  bootstrapCalendar2Fill,
  bootstrapPersonFillCheck,
  bootstrapWallet2,
  bootstrapCalendar2WeekFill,
  bootstrapClockFill,
  bootstrapHouse,
  bootstrapPersonHearts,
  bootstrapDoorOpen
} from '@ng-icons/bootstrap-icons';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [NgIconComponent,RouterModule],
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
export class SidebarComponent {}
