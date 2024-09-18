import { Component, OnInit } from '@angular/core';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { RouterModule } from '@angular/router';
import { UserService } from '../../../../services/user-service/users.service';
import { AgGridAngular } from 'ag-grid-angular';

import {
  ColDef,
  GridApi,
  GridOptions,
  GridSizeChangedEvent,
  FirstDataRenderedEvent,
} from 'ag-grid-community';
import { CommonModule } from '@angular/common';
import { User } from '../../../../models/user.model';
import { ActionCellRendererList } from './ActionCellRendererList';
import { ToastrService } from 'ngx-toastr';
import { DateFormatPipe } from '../../../../core/pipes/date.pipe';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [AgGridAngular, CommonModule, RouterModule, HlmButtonDirective],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css',
})
export class UserListComponent implements OnInit {
  private gridApi!: GridApi;
  users: User[] = [];

  constructor(
    private userService: UserService,
    private toastr: ToastrService
  ) {}

  public colDefs: ColDef[] = [
    { field: 'fullName', headerName: 'Full Name', minWidth: 150 },
    { field: 'email', headerName: 'Email', minWidth: 150 },
    { field: 'role', headerName: 'Role', minWidth: 150 },
    {
      field: 'createdTime',
      headerName: 'Created Time',
      minWidth: 150,
      valueFormatter: (params: any) =>
        new DateFormatPipe().transform(params.value),
    },
    {
      field: 'updatedTime',
      headerName: 'Updated Time',
      minWidth: 150,
      valueFormatter: (params: any) =>
        new DateFormatPipe().transform(params.value),
    },
    {
      headerName: 'Actions',
      field: 'action',
      cellRenderer: ActionCellRendererList,
      cellRendererParams: {
        context: this, // Pass 'this' to provide access to UserListComponent
      },
      headerClass: 'text-center',
      minWidth: 300,
      sortable: false,
      filter: false,
      floatingFilter: false,
      cellClass: 'text-center',
    },
  ];

  public gridOptions: GridOptions = {
    pagination: true,
    paginationPageSize: 10,
    paginationPageSizeSelector: [10, 25, 50],
  };

  public defaultColDef: ColDef = {
    floatingFilter: true,
    flex: 1,
    sortable: true,
    filter: true,
  };

  onGridSizeChanged(params: GridSizeChangedEvent) {
    params.api.sizeColumnsToFit(); // Ensure columns fit the grid width
  }

  onFirstDataRendered(params: FirstDataRenderedEvent) {
    params.api.sizeColumnsToFit(); // Fit columns on initial render
  }

  adjustGridForScreenSize() {
    if (this.gridApi) {
      this.gridApi.sizeColumnsToFit(); // Adjust columns for screen size
    }
  }

  ngOnInit() {
    this.loadUsers();
  }

  onGridReady(params: any) {
    this.gridApi = params.api;
    this.gridApi.sizeColumnsToFit();
    this.loadUsers(); // Load users when the grid is ready
  }

  loadUsers() {
    this.users = []; // Clear the users array before fetching
    this.fetchAllUsers(0, 20); // Start fetching from page 0 with size 20
  }

  fetchAllUsers(page: number, size: number) {
    const tempUsers: User[] = []; // Temporary array to hold all users

    const fetchPage = (pageNum: number) => {
      this.userService.getUsers(pageNum, size).subscribe({
        next: (response) => {
          tempUsers.push(...response.content); // Add new users to tempUsers

          if (response.totalPages > pageNum + 1) {
            // If there are more pages, fetch the next page
            fetchPage(pageNum + 1);
          } else {
            // When all pages are fetched, set the final user list and update the grid
            this.users = tempUsers;
            this.gridApi.applyTransaction({ add: this.users });
          }
        },
        error: (error) => console.error('Failed to load users', error),
      });
    };

    // Start fetching from the initial page
    fetchPage(page);
  }

  deleteOneUser(id: string): void {
    this.userService.deleteUser(id).subscribe({
      next: () => {
        this.toastr.success('Success delete user data');
        this.loadUsers(); // Reload products after deletion
      },
      error: (e) => console.error(e),
    });
  }
}
