import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppointmentSpecializationSymptompsComponent } from './appointment-specialization-symptomps.component';

describe('AppointmentSpecializationSymptompsComponent', () => {
  let component: AppointmentSpecializationSymptompsComponent;
  let fixture: ComponentFixture<AppointmentSpecializationSymptompsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppointmentSpecializationSymptompsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AppointmentSpecializationSymptompsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
