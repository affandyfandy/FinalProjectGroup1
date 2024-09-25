import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppointmentMyComponent } from './appointment-my.component';

describe('AppointmentMyComponent', () => {
  let component: AppointmentMyComponent;
  let fixture: ComponentFixture<AppointmentMyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppointmentMyComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AppointmentMyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
