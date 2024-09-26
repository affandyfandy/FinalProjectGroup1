import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppointmentSpecializationPreventComponent } from './appointment-specialization-prevent.component';

describe('AppointmentSpecializationPreventComponent', () => {
  let component: AppointmentSpecializationPreventComponent;
  let fixture: ComponentFixture<AppointmentSpecializationPreventComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppointmentSpecializationPreventComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AppointmentSpecializationPreventComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
