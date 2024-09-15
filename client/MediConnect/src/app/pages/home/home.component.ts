import { Component } from '@angular/core';
import { ServiceBoxComponent } from './service-box/service-box.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [ServiceBoxComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {}
