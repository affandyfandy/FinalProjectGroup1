import { Component, OnInit } from '@angular/core';
import { Router, RouterOutlet, NavigationEnd } from '@angular/router';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, HeaderComponent, FooterComponent],
  templateUrl: './app.component.html',
  styleUrls: [],
})
export class AppComponent implements OnInit {
  showNavAndFooter = true;
  title = 'MediConnect';

  constructor(private router: Router) {}

  ngOnInit() {
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        const currentUrl = event.urlAfterRedirects;

        // Check if the current URL matches any defined routes
        this.router.config.forEach((route) => {
          if (route.children) {
            route.children.forEach((child) => {
              if (
                currentUrl === child.path ||
                currentUrl.startsWith(`${child.path}/`)
              ) {
                this.showNavAndFooter = true;
              }
            });
          } else if (currentUrl === route.path) {
            this.showNavAndFooter = true;
          }
        });

        // If not found in any route, hide nav and footer
        this.showNavAndFooter = this.showNavAndFooter !== true;
      });
  }
}
