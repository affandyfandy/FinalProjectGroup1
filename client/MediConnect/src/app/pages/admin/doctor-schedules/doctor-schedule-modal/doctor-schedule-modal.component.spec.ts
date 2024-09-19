import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorScheduleModalComponent } from './doctor-schedule-modal.component';

describe('DoctorScheduleModalComponent', () => {
  let component: DoctorScheduleModalComponent;
  let fixture: ComponentFixture<DoctorScheduleModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DoctorScheduleModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DoctorScheduleModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
