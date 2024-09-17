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
import { AlertDialogPreviewComponent } from './ButtonSample';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [
    AgGridAngular,
    CommonModule,
    RouterModule,
    HlmButtonDirective,
    AlertDialogPreviewComponent,
  ],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css',
})
export class UserListComponent implements OnInit {
  private gridApi!: GridApi;
  users: User[] = [];

  constructor(private userService: UserService) {}

  public colDefs: ColDef[] = [
    { field: 'full_name', headerName: 'Full Name', minWidth: 150 },
    { field: 'email', headerName: 'Email', minWidth: 150 },
    { field: 'role', headerName: 'Role', minWidth: 150 },
    {
      field: 'createdTime',
      headerName: 'Created Time',
      minWidth: 150,
      valueFormatter: (params) => new Date(params.value).toLocaleString(),
    },
    {
      field: 'updatedTime',
      headerName: 'Updated Time',
      minWidth: 150,
      valueFormatter: (params) => new Date(params.value).toLocaleString(),
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
  }

  loadUsers() {
    this.userService.getUsers().subscribe((response) => {
      this.users = response.content;
    });
  }

  deleteOneUser(id: string): void {
    this.userService.deleteUser(id).subscribe({
      next: () => {
        this.loadUsers(); // Reload products after deletion
      },
      error: (e) => console.error(e),
    });
  }
}
