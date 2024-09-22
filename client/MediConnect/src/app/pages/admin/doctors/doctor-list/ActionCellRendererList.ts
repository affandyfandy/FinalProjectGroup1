import { Component } from '@angular/core';
import { ICellRendererAngularComp } from 'ag-grid-angular';
import { ICellRendererParams } from 'ag-grid-community';
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
import {
  bootstrapTrash,
  bootstrapPencilSquare,
  bootstrapEyeFill,
} from '@ng-icons/bootstrap-icons';
import {
  BrnDialogModule,
  BrnDialogContentDirective,
  BrnDialogTriggerDirective,
} from '@spartan-ng/ui-dialog-brain';
import {
  HlmDialogComponent,
  HlmDialogContentComponent,
  HlmDialogDescriptionDirective,
  HlmDialogFooterComponent,
  HlmDialogHeaderComponent,
  HlmDialogTitleDirective,
} from '@spartan-ng/ui-dialog-helm';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { RouterModule } from '@angular/router';
import { DoctorModalComponent } from '../doctor-modal/doctor-modal.component';
import { DoctorListComponent } from './doctor-list.component';
@Component({
  selector: 'app-action-cell-renderer',
  standalone: true,
  imports: [
    BrnAlertDialogContentDirective,
    BrnAlertDialogTriggerDirective,
    HlmAlertDialogActionButtonDirective,
    HlmAlertDialogCancelButtonDirective,
    HlmAlertDialogComponent,
    HlmAlertDialogContentComponent,
    HlmAlertDialogDescriptionDirective,
    HlmAlertDialogFooterComponent,
    HlmAlertDialogHeaderComponent,
    HlmAlertDialogOverlayDirective,
    HlmAlertDialogTitleDirective,

    NgIconComponent,
    BrnDialogModule,
    BrnDialogContentDirective,
    BrnDialogTriggerDirective,
    HlmDialogComponent,
    HlmDialogContentComponent,
    HlmDialogHeaderComponent,
    HlmDialogFooterComponent,
    HlmDialogTitleDirective,
    HlmDialogDescriptionDirective,
    DoctorModalComponent,
    RouterModule,
  ],
  viewProviders: [
    provideIcons({ bootstrapTrash, bootstrapPencilSquare, bootstrapEyeFill }),
  ],
  template: `
    <hlm-dialog>
      <button
        class="bg-blue-500 text-white text-xs px-4 py-1.5 rounded-xl shadow hover:bg-blue-600 mr-1.5 disabled:bg-blue-300 disabled:cursor-not-allowed"
        brnDialogTrigger
        hlmBtn
      >
        <ng-icon name="bootstrapEyeFill"></ng-icon> View
      </button>
      <hlm-dialog-content
        class="max-w-[500px] bg-white lg:max-w-[700px] text-left h-[90vh] overflow-y-auto"
        *brnDialogContent="let ctx"
      >
        <hlm-dialog-header class="w-full text-left">
          <h3 hlmDialogTitle>Doctor Information</h3>
          <p hlmDialogDescription>
            Detailed information regarding the doctor's data
          </p>
        </hlm-dialog-header>
        <app-doctor-modal [doctor]="params.data"></app-doctor-modal>
      </hlm-dialog-content>
    </hlm-dialog>
    <a [routerLink]="['/admin/dashboard/doctors', params.data.id]">
      <button
        class="bg-gray-500 text-white text-xs px-4 py-1.5 rounded-xl shadow hover:bg-gray-600 mr-1.5 disabled:bg-gray-300 disabled:cursor-not-allowed"
      >
        <ng-icon name="bootstrapPencilSquare"></ng-icon> Update
      </button>
    </a>

    <hlm-alert-dialog>
      <button
        class="bg-red-500 text-white text-xs px-4 py-1.5 rounded-xl shadow hover:bg-red-600 mr-1.5 disabled:bg-red-300 disabled:cursor-not-allowed"
        brnAlertDialogTrigger
      >
        <ng-icon name="bootstrapTrash"></ng-icon> Trash
      </button>
      <hlm-alert-dialog-content
        *brnAlertDialogContent="let ctx"
        class="bg-white"
      >
        <hlm-alert-dialog-header>
          <h3 hlmAlertDialogTitle>Are you absolutely sure?</h3>
          <p hlmAlertDialogDescription>
            This action cannot be undone. This will permanently delete your
            account and remove your data from our servers.
          </p>
        </hlm-alert-dialog-header>
        <hlm-alert-dialog-footer>
          <button hlmAlertDialogCancel (click)="ctx.close()">Cancel</button>
          <button hlmAlertDialogAction class="bg-gray-800 text-white" (click)="onDelete()">Delete doctor</button>
        </hlm-alert-dialog-footer>
      </hlm-alert-dialog-content>
    </hlm-alert-dialog>
  `,
})
export class ActionCellRendererList implements ICellRendererAngularComp {
  params!: ICellRendererParams;

  agInit(params: ICellRendererParams): void {
    this.params = params;
  }

  refresh(params: ICellRendererParams): boolean {
    this.params = params;
    return true;
  }

  onDelete(): void {
    const parentComponent = this.params.context as DoctorListComponent;
    if (parentComponent && parentComponent.deleteOneDoctor) {
      parentComponent.deleteOneDoctor(this.params.data.id);
    } else {
      console.error('Parent component or deleteDoctor method not found.');
    }
  }
}
