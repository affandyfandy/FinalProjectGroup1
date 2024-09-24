import { Component, Input } from '@angular/core';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import {
  BrnAlertDialogContentDirective,
  BrnAlertDialogTriggerDirective,
} from '@spartan-ng/ui-alertdialog-brain';
import {
  HlmAlertDialogActionButtonDirective,
  HlmAlertDialogCancelButtonDirective,
  HlmAlertDialogComponent,
  HlmAlertDialogContentComponent,
  HlmAlertDialogDescriptionDirective,
  HlmAlertDialogFooterComponent,
  HlmAlertDialogHeaderComponent,
  HlmAlertDialogOverlayDirective,
  HlmAlertDialogTitleDirective,
} from '@spartan-ng/ui-alertdialog-helm';
import { MessageErrorRequiredField } from '../../../../utils/message';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-appointment-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    BrnAlertDialogTriggerDirective,
    BrnAlertDialogContentDirective,
    HlmAlertDialogComponent,
    HlmAlertDialogOverlayDirective,
    HlmAlertDialogHeaderComponent,
    HlmAlertDialogFooterComponent,
    HlmAlertDialogTitleDirective,
    HlmAlertDialogDescriptionDirective,
    HlmAlertDialogCancelButtonDirective,
    HlmAlertDialogActionButtonDirective,
    HlmAlertDialogContentComponent,
  ],
  templateUrl: './appointment-form.component.html',
  styleUrl: './appointment-form.component.css',
})
export class AppointmentFormComponent {
  appointmentForm: FormGroup;
  messageErrorRequiredField = MessageErrorRequiredField;
  @Input() dialogCtx: any;

  constructor(private fb: FormBuilder) {
    this.appointmentForm = this.fb.group({
      date: ['', Validators.required],
      hour: ['', Validators.required],
      description: ['', [Validators.required, Validators.maxLength(250)]],
    });
  }

  // Method to handle the form submission
  submitForm() {
    if (this.appointmentForm.valid) {
      console.log('Form Data:', this.appointmentForm.value);
      return true;
    }
    this.appointmentForm.markAllAsTouched();
    return false;
  }

  // Method to handle the confirmation dialog and close the parent dialog
  confirmAndSubmit(ctx: any, dialogCtx: any) {
    if (this.submitForm()) {
      ctx.close();
      dialogCtx?.close();
    }
  }

  get date() {
    return this.appointmentForm.get('date');
  }
  get hour() {
    return this.appointmentForm.get('hour');
  }
  get description() {
    return this.appointmentForm.get('description');
  }
}
