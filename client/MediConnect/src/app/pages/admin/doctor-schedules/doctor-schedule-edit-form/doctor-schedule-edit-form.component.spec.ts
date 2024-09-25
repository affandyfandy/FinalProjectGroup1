import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorScheduleEditFormComponent } from './doctor-schedule-edit-form.component';

describe('DoctorScheduleEditFormComponent', () => {
  let component: DoctorScheduleEditFormComponent;
  let fixture: ComponentFixture<DoctorScheduleEditFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DoctorScheduleEditFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DoctorScheduleEditFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
