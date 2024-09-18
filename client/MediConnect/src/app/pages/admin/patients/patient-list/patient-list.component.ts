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
      valueFormatter: (params) => {
        if (
          !params.value ||
          !Array.isArray(params.value) ||
          params.value.length !== 3
        ) {
          return 'N/A';
        }
        const [year, month, day] = params.value;
        return new Date(year, month - 1, day).toLocaleDateString();
      },
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
    this.patientService.getPatients().subscribe({
      next: (response) => {
        this.patients = response.content;
        console.log('Patients loaded:', this.patients);
      },
      error: (error) => {
        console.error('Error loading patients:', error);
      },
    });
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
