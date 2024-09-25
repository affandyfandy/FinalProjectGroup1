import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorScheduleCreateComponent } from './doctor-schedule-create.component';

describe('DoctorScheduleCreateComponent', () => {
  let component: DoctorScheduleCreateComponent;
  let fixture: ComponentFixture<DoctorScheduleCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DoctorScheduleCreateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DoctorScheduleCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
