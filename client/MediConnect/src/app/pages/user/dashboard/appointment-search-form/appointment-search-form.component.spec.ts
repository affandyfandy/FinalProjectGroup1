import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppointmentSearchFormComponent } from './appointment-search-form.component';

describe('AppointmentSearchFormComponent', () => {
  let component: AppointmentSearchFormComponent;
  let fixture: ComponentFixture<AppointmentSearchFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppointmentSearchFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AppointmentSearchFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
