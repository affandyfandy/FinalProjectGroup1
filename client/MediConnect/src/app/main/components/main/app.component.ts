import { Component, OnInit } from '@angular/core';
import { Router, RouterOutlet, NavigationEnd } from '@angular/router';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators';
import { RouterConfig } from '../../../config/app.constants';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { HeaderAdminComponent } from '../header-admin/header-admin.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    HeaderComponent,
    FooterComponent,
    SidebarComponent,
    HeaderAdminComponent
  ],
  templateUrl: './app.component.html',
  styleUrls: [],
})
export class AppComponent implements OnInit {
  showNavAndFooter = true;
  showSidebar = false;
  title = 'MediConnect';

  constructor(private router: Router) {}

  ngOnInit() {
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        const currentUrl = event.urlAfterRedirects.slice(1); // Remove leading slash

        // Check if the current URL is an admin route
        const isAdminRoute = currentUrl.startsWith('admin');

        if (isAdminRoute) {
          this.showNavAndFooter = false;
          this.showSidebar = true;
        } else {
          // Check if the current URL is in RouterConfig
          const matchedRoute = Object.values(RouterConfig).find(
            (route) =>
              currentUrl === route.path ||
              currentUrl.startsWith(`${route.path}/`)
          );

          if (matchedRoute) {
            // Route is in RouterConfig
            this.showNavAndFooter = ![
              RouterConfig.SIGNIN.path,
              RouterConfig.SIGNUP.path,
              'unauthorized',
            ].includes(matchedRoute.path);
          } else {
            // Route is not in RouterConfig
            this.showNavAndFooter = false;
          }

          this.showSidebar = false;
        }
      });
  }
}
