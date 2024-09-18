import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientsCreateComponent } from './patients-create.component';

describe('PatientsCreateComponent', () => {
  let component: PatientsCreateComponent;
  let fixture: ComponentFixture<PatientsCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PatientsCreateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PatientsCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
