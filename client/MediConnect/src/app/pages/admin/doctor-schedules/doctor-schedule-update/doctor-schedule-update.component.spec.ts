import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorScheduleUpdateComponent } from './doctor-schedule-update.component';

describe('DoctorScheduleUpdateComponent', () => {
  let component: DoctorScheduleUpdateComponent;
  let fixture: ComponentFixture<DoctorScheduleUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DoctorScheduleUpdateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DoctorScheduleUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
