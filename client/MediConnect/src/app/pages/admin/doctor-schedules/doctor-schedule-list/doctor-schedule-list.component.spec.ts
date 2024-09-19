import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorScheduleListComponent } from './doctor-schedule-list.component';

describe('DoctorScheduleListComponent', () => {
  let component: DoctorScheduleListComponent;
  let fixture: ComponentFixture<DoctorScheduleListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DoctorScheduleListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DoctorScheduleListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
