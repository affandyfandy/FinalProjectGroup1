import { Component, OnInit } from '@angular/core';
import { Router, RouterOutlet, NavigationEnd } from '@angular/router';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators';
import { RouterConfig } from '../../../config/app.constants';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { HeaderAdminComponent } from '../header-admin/header-admin.component';
import { SidebarUserComponent } from '../sidebar-user/sidebar-user.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    HeaderComponent,
    FooterComponent,
    SidebarComponent,
    HeaderAdminComponent,
    SidebarUserComponent,
  ],
  templateUrl: './app.component.html',
  styleUrls: [],
})
export class AppComponent implements OnInit {
  showNavAndFooter = true;
  showSidebarAdmin = false;
  showSidebarUser = false;
  showSidebar = false;
  title = 'MediConnect';

  constructor(private router: Router) {}

  ngOnInit() {
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        const currentUrl = event.urlAfterRedirects.slice(1); // Remove leading slash

        // Check if the current URL starts with '/admin'
        const isAdminRoute = currentUrl.startsWith('admin');
        const isDashboardRoute = currentUrl.startsWith('dashboard');

        if (isAdminRoute) {
          this.showNavAndFooter = false;
          this.showSidebarAdmin = true;
          this.showSidebarUser = false; // Ensure user sidebar is hidden
          this.showSidebar = true;
        } else if (isDashboardRoute) {
          this.showNavAndFooter = false;
          this.showSidebarAdmin = false; // Ensure admin sidebar is hidden
          this.showSidebarUser = true; // Show user sidebar
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

          // Reset sidebar visibility for other routes
          this.showSidebarAdmin = false;
          this.showSidebarUser = false;
          this.showSidebar = false;
        }
      });
  }
}
