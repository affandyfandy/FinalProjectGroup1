import { Component, OnInit } from '@angular/core';
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm';
import { RouterModule } from '@angular/router';
import { DoctorSchedulesService } from '../../../../services/doctor-schedule-service/doctor-schedules.service';
import { DoctorsService } from '../../../../services/doctor-service/doctors.service';
import { ListDoctorSchedule } from '../../../../models/doctor-schedule.model';
import { AgGridAngular } from 'ag-grid-angular';
import { ActionCellRendererList } from '../../users/user-list/ActionCellRendererList';

import {
  ColDef,
  GridApi,
  GridOptions,
  GridSizeChangedEvent,
  FirstDataRenderedEvent,
} from 'ag-grid-community';
import { CommonModule } from '@angular/common';
import { DoctorSchedule } from '../../../../models/doctor-schedule.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-doctor-schedule-list',
  standalone: true,
  imports: [AgGridAngular, CommonModule, RouterModule, HlmButtonDirective],
  templateUrl: './doctor-schedule-list.component.html',
  styleUrl: './doctor-schedule-list.component.css'
})
export class DoctorScheduleListComponent implements OnInit {
  private gridApi!: GridApi;

  doctorSchedules: DoctorSchedule[] = [];
  listSchedules: ListDoctorSchedule[] = [];
  doctorNameMap: Map<string, string> = new Map();

  constructor(
    private doctorScheduleService: DoctorSchedulesService,
    private doctorsService: DoctorsService,
    private toastr: ToastrService
  ) {}

  public colDefs: ColDef[] = [
    {
      field: 'day',
      headerName: 'Day',
      minWidth: 100,
      valueGetter: (params) => params.data.day ?? 'N/A',
    },
    {
      field: 'name',
      headerName: 'Doctor Name',
      minWidth: 150,
      valueGetter: (params) => params.data.name ?? 'N/A',
    },
    {
      field: 'startWorkingHour',
      headerName: 'Start Shift',
      minWidth: 150,
      valueGetter: (params) => params.data.startWorkingHour ?? 'N/A',
    },
    {
      field: 'endWorkingHour',
      headerName: 'End Shift',
      minWidth: 100,
      valueGetter: (params) => params.data.endWorkingHour ?? 'N/A',
    },
    {
      field: 'maxPatient',
      headerName: 'Max Patients',
      minWidth: 100,
      valueGetter: (params) => params.data.maxPatient ?? 'N/A',
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
    this.loadDoctorNames();
    this.loadDoctorSchedules();
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

  loadDoctorNames() {
    this.doctorsService.getDoctors().subscribe({
      next: (doctors) => {
        this.doctorNameMap = new Map(
          doctors.map(doctor => [doctor.id, doctor.name])
        );
      },
      error: (error) => {
        console.error('Error loading doctor names:', error);
      }
    });
  }

  loadDoctorSchedules() {
    this.doctorScheduleService.getSchedules().subscribe({
      next: (response) => {
        this.doctorSchedules = response;
        this.listSchedules = response.flatMap((schedule) =>
          schedule.scheduleTimes.map((time) => ({
            id: schedule.id,
            doctorId: schedule.doctorId,
            day: schedule.day,
            name: this.getDoctorNameById(schedule.doctorId),
            startWorkingHour: time.startWorkingHour,
            endWorkingHour: time.endWorkingHour,
            maxPatient: time.maxPatient
          }))
        );
        console.log('List doctor schedules loaded:', this.listSchedules);
      },
      error: (error) => {
        console.error('Error loading doctor schedules:', error);
      }
    });
  }

  getDoctorNameById(doctorId: string): string {
    return this.doctorNameMap.get(doctorId) || 'Unknown Doctor';
  }

  deleteOneSchedule(id: string): void {
    this.doctorScheduleService.deleteSchedule(id).subscribe({
      next: () => {
        this.toastr.success('Success delete schedule data');
        this.loadDoctorSchedules();
      },
      error: (e) => console.error(e),
    });
  }
}
