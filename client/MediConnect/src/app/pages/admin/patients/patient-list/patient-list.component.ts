import { Component, OnInit } from '@angular/core';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { RouterModule } from '@angular/router';
import { PatientsService } from '../../../../services/patient-service/patients.service';
import { AgGridAngular } from 'ag-grid-angular';

import {
  ColDef,
  GridApi,
  GridOptions,
  GridSizeChangedEvent,
  FirstDataRenderedEvent,
} from 'ag-grid-community';
import { CommonModule } from '@angular/common';
import { Patient } from '../../../../models/patient.model';
import { ActionCellRendererList } from './ActionCellRendererList';
import { ToastrService } from 'ngx-toastr';
import { DateFormatPipe } from '../../../../core/pipes/date.pipe';
import { DobFormatPipe } from '../../../../core/pipes/dob-format.pipe';
@Component({
  selector: 'app-patient-list',
  standalone: true,
  imports: [AgGridAngular, CommonModule, RouterModule, HlmButtonDirective],
  templateUrl: './patient-list.component.html',
  styleUrl: './patient-list.component.css',
})
export class PatientListComponent implements OnInit {
  private gridApi!: GridApi;
  patients: Patient[] = [];

  constructor(
    private patientService: PatientsService,
    private toastr: ToastrService
  ) {}

  public colDefs: ColDef[] = [
    {
      field: 'nik',
      minWidth: 150,
      headerName: 'NIK',
      valueGetter: (params) => params.data.nik ?? 'N/A',
    },
    {
      field: 'user.full_name',
      headerName: 'Full Name',
      minWidth: 150,
      valueGetter: (params) => params.data.user?.full_name ?? 'N/A',
    },
    {
      field: 'user.email',
      headerName: 'Email',
      minWidth: 150,
      valueGetter: (params) => params.data.user?.email ?? 'N/A',
    },
    {
      field: 'gender',
      headerName: 'Gender',
      minWidth: 100,
      valueGetter: (params) => params.data.gender ?? 'N/A',
    },
    {
      field: 'dateOfBirth',
      headerName: 'Date of Birth',
      minWidth: 150,
      valueFormatter: (params: any) =>
        new DobFormatPipe().transform(params.value),
    },
    {
      headerName: 'Actions',
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

  ngOnInit() {
    this.loadPatients();
  }

  onGridReady(params: any) {
    this.gridApi = params.api;
    this.gridApi.sizeColumnsToFit();
  }

  onGridSizeChanged(params: GridSizeChangedEvent) {
    params.api.sizeColumnsToFit();
  }

  onFirstDataRendered(params: FirstDataRenderedEvent) {
    params.api.sizeColumnsToFit();
  }

  loadPatients() {
    this.patients = []; // Clear the users array before fetching
    this.fetchAllPatients(0, 20); // Start fetching from page 0 with size 20
  }

  fetchAllPatients(page: number, size: number) {
    const tempUsers: Patient[] = []; // Temporary array to hold all users

    const fetchPage = (pageNum: number) => {
      this.patientService.getPatients(pageNum, size).subscribe({
        next: (response) => {
          tempUsers.push(...response.content); // Add new users to tempUsers

          if (response.totalPages > pageNum + 1) {
            // If there are more pages, fetch the next page
            fetchPage(pageNum + 1);
          } else {
            // When all pages are fetched, set the final user list and update the grid
            this.patients = tempUsers;
            this.gridApi.applyTransaction({ add: this.patients });
          }
        },
        error: (error) => console.error('Failed to load patients', error),
      });
    };

    // Start fetching from the initial page
    fetchPage(page);
  }

  deleteOnePatient(id: string): void {
    this.patientService.deletePatient(id).subscribe({
      next: () => {
        this.toastr.success('Success delete patient data');
        this.loadPatients(); // Reload products after deletion
      },
      error: (e) => console.error(e),
    });
  }
}
