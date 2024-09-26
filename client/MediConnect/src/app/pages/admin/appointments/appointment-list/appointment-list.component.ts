import { Component, OnInit } from '@angular/core';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { RouterModule } from '@angular/router';
import { AppointmentService } from '../../../../services/appointment-service/appoinment.service';
import { DoctorsService } from '../../../../services/doctor-service/doctors.service';
import { PatientsService } from '../../../../services/patient-service/patients.service';
import { AppointmentShowDTO } from '../../../../models/appointment.model';
import { AgGridAngular } from 'ag-grid-angular';
import { ActionCellRendererList } from './ActionCellRendererList';

import {
  ColDef,
  GridApi,
  GridOptions,
  GridSizeChangedEvent,
  FirstDataRenderedEvent,
} from 'ag-grid-community';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { TimeOnlyPipe } from '../../../../core/pipes/time-only.pipe';
import { DateFormatPipe } from '../../../../core/pipes/date.pipe';

@Component({
  selector: 'app-appointment-list',
  standalone: true,
  imports: [AgGridAngular, CommonModule, RouterModule, HlmButtonDirective],
  templateUrl: './appointment-list.component.html',
  styleUrl: './appointment-list.component.css',
})
export class AppointmentListComponent implements OnInit {
  private gridApi!: GridApi;

  appointments: AppointmentShowDTO[] = [];
  patientNameMap: Map<string, string> = new Map();
  doctorNameMap: Map<string, string> = new Map();

  constructor(
    private appointmentService: AppointmentService,
    private patientService: PatientsService,
    private doctorsService: DoctorsService,
    private toastr: ToastrService
  ) {}

  public colDefs: ColDef[] = [
    {
      field: 'queueNumber',
      headerName: 'Queue Number',
      minWidth: 100,
      valueGetter: (params) => params.data.queueNumber ?? 'N/A',
    },
    {
      field: 'patientName',
      headerName: 'Patient Name',
      minWidth: 150,
      valueGetter: (params) =>
        this.getPatientNameById(params.data.patientId) ?? 'N/A',
    },

    {
      field: 'doctorName',
      headerName: 'Doctor Name',
      minWidth: 150,
      valueGetter: (params) =>
        this.getDoctorNameById(params.data.doctorId) ?? 'N/A',
    },
    {
      field: 'startWorkingHour',
      headerName: 'Start Hour',
      minWidth: 100,
      valueGetter: (params) =>
        new TimeOnlyPipe().transform(params.data.startTime) ?? 'N/A',
    },
    {
      field: 'endWorkingHour',
      headerName: 'End Hour',
      minWidth: 100,
      valueGetter: (params) =>
        new TimeOnlyPipe().transform(params.data.endTime) ?? 'N/A',
    },

    {
      field: 'date',
      headerName: 'Date',
      minWidth: 100,
      valueGetter: (params) =>
        new DateFormatPipe().transform(params.data.date) ?? 'N/A',
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
    },
  ];

  public gridOptions: GridOptions = {
    pagination: true,
    paginationPageSize: 10,
    paginationPageSizeSelector: [10, 25, 50],
    rowClassRules: {
      'bg-gray-500': (params) => params.data.status === 'CANCEL', // Apply gray background if status is 'CANCEL'
    },
    isRowSelectable: (rowNode) => rowNode.data.status !== 'CANCEL', // Disable row selection if status is 'CANCEL'
  };

  public defaultColDef: ColDef = {
    floatingFilter: true,
    flex: 1,
    sortable: true,
    filter: true,
  };

  ngOnInit() {
    Promise.all([this.loadPatientNames(), this.loadDoctorNames()])
      .then(() => this.loadAppointments())
      .catch((error) => console.error('Error loading data:', error));
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

  loadPatientNames() {
    return new Promise((resolve, reject) => {
      this.patientService.getPatients().subscribe({
        next: (patients) => {
          console.log(patients);
          this.patientNameMap = new Map(
            patients.content.map((patient: any) => [
              patient.id,
              patient.user ? patient.user.fullName : 'Unknown Patient',
            ])
          );
          console.log(this.patientNameMap);
          resolve(true);
        },
        error: (error) => {
          console.error('Error loading patient names:', error);
          reject(error);
        },
      });
    });
  }

  loadDoctorNames() {
    return new Promise((resolve, reject) => {
      this.doctorsService.getDoctors().subscribe({
        next: (doctors) => {
          this.doctorNameMap = new Map(
            doctors.map((doctor) => [doctor.id, doctor.name])
          );
          resolve(true);
        },
        error: (error) => {
          console.error('Error loading doctor names:', error);
          reject(error);
        },
      });
    });
  }

  loadAppointments() {
    this.appointmentService.getAppointments().subscribe({
      next: (response) => {
        this.appointments = response;
        console.log('List doctor schedules loaded:', this.appointments);
      },
      error: (error) => {
        console.error('Error loading doctor schedules:', error);
      },
    });
  }

  deleteOneAppointment(id: string): void {
    this.appointmentService.deleteAppointment(id).subscribe({
      next: () => {
        this.toastr.success('Success delete appointment data');
        this.loadAppointments();
      },
      error: (e) => console.error(e),
    });
  }

  getPatientNameById(patientId: string): string {
    return this.patientNameMap.get(patientId) || 'Unknown Patient';
  }

  getDoctorNameById(doctorId: string): string {
    return this.doctorNameMap.get(doctorId) || 'Unknown Doctor';
  }
}
