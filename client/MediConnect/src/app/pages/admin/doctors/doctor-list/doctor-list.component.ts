import { Component, OnInit } from '@angular/core';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { RouterModule } from '@angular/router';
import { DoctorsService } from '../../../../services/doctor-service/doctors.service';
import { AgGridAngular } from 'ag-grid-angular';

import {
  ColDef,
  GridApi,
  GridOptions,
  GridSizeChangedEvent,
  FirstDataRenderedEvent,
} from 'ag-grid-community';
import { CommonModule } from '@angular/common';
import { Doctor } from '../../../../models/doctor.model';
import { ActionCellRendererList } from './ActionCellRendererList';
import { ToastrService } from 'ngx-toastr';
@Component({
  selector: 'app-doctor-list',
  standalone: true,
  imports: [AgGridAngular, CommonModule, RouterModule, HlmButtonDirective],
  templateUrl: './doctor-list.component.html',
  styleUrl: './doctor-list.component.css'
})
export class DoctorListComponent implements OnInit {
  private gridApi!: GridApi;
  doctors: Doctor[] = [];

  constructor(
    private doctorService: DoctorsService,
    private toastr: ToastrService
  ) {}

  public colDefs: ColDef[] = [
    {
      field: 'identificationNumber',
      headerName: 'SIP',
      minWidth: 100,
      valueGetter: (params) => params.data.identificationNumber ?? 'N/A',
    },
    {
      field: 'name',
      headerName: 'Name',
      minWidth: 150,
      valueGetter: (params) => params.data.name ?? 'N/A',
    },
    {
      field: 'specialization',
      headerName: 'Specialization',
      minWidth: 150,
      valueGetter: (params) => params.data.specialization ?? 'N/A',
    },
    {
      field: 'gender',
      headerName: 'Gender',
      minWidth: 100,
      valueGetter: (params) => params.data.gender ?? 'N/A',
    },
    {
      field: 'patientTotal',
      headerName: 'Patient Total',
      minWidth: 100,
      valueGetter: (params) => params.data.patientTotal ?? 'N/A',
    },
    {
      headerName: 'Actions',
      cellRenderer: ActionCellRendererList,
      cellRendererParams: {
        context: this,
      },
      headerClass: 'text-center',
      minWidth: 300,
      sortable: false,
      filter: false,
      floatingFilter: false,
      cellClass: 'text-center',
    }
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
    this.loadDoctors();
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

  loadDoctors() {
    this.doctorService.getDoctors().subscribe({
      next: (response) => {
        this.doctors = response;
        console.log('Doctors loaded:', this.doctors);
      },
      error: (error) => {
        console.error('Error loading doctors:', error);
      },
    });
  }

  deleteOneDoctor(id: string): void {
    this.doctorService.deleteDoctor(id).subscribe({
      next: () => {
        this.toastr.success('Success delete doctor data');
        this.loadDoctors();
      },
      error: (e) => console.error(e),
    });
  }
}

