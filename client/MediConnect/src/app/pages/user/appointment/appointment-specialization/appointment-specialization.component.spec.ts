import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppointmentSpecializationComponent } from './appointment-specialization.component';

describe('AppointmentSpecializationComponent', () => {
  let component: AppointmentSpecializationComponent;
  let fixture: ComponentFixture<AppointmentSpecializationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppointmentSpecializationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AppointmentSpecializationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
