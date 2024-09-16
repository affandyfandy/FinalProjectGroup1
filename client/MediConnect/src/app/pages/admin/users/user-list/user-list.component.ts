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

  constructor(private userService: UserService) {}

  public colDefs: ColDef[] = [
    { field: 'full_name', headerName: 'Full Name' },
    { field: 'email', headerName: 'Email' },
    { field: 'role', headerName: 'Role' },
    {
      field: 'createdTime',
      headerName: 'Created Time',
      valueFormatter: (params) => new Date(params.value).toLocaleString(),
    },
    {
      field: 'updatedTime',
      headerName: 'Updated Time',
      valueFormatter: (params) => new Date(params.value).toLocaleString(),
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
}
