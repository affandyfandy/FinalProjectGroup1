import { Component } from '@angular/core';
import {
  HlmCaptionComponent,
  HlmTableComponent,
  HlmTdComponent,
  HlmThComponent,
  HlmTrowComponent,
} from '@spartan-ng/ui-table-helm';


@Component({
  selector: 'app-queue-list',
  standalone: true,
  imports: [
    HlmTableComponent,
    HlmTrowComponent,
    HlmThComponent,
    HlmTdComponent,
    HlmCaptionComponent,
  ],
  templateUrl: './queue-list.component.html',
  styleUrl: './queue-list.component.css',
})
export class QueueListComponent {}
