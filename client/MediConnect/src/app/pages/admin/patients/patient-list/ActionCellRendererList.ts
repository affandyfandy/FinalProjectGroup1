import { Component } from '@angular/core';
import { ICellRendererAngularComp } from 'ag-grid-angular';
import { ICellRendererParams } from 'ag-grid-community';
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
import { PatientModalComponent } from '../patient-modal/patient-modal.component';
import { RouterModule } from '@angular/router';
@Component({
  selector: 'app-action-cell-renderer',
  standalone: true,
  imports: [
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
    PatientModalComponent,
    RouterModule,
  ],
  viewProviders: [
    provideIcons({ bootstrapTrash, bootstrapPencilSquare, bootstrapEyeFill }),
  ],
  template: `
    <!-- Dialog Content for Viewing Invoice -->
    <hlm-dialog>
      <button
        class="bg-blue-500 text-white text-xs px-4 py-1.5 rounded-xl shadow hover:bg-blue-600 mr-1.5 disabled:bg-blue-300 disabled:cursor-not-allowed"
        brnDialogTrigger
        hlmBtn
      >
        <ng-icon name="bootstrapEyeFill"></ng-icon> View
      </button>
      <hlm-dialog-content
        class="max-w-[300px] bg-white lg:max-w-[700px] text-left h-[70vh]"
        *brnDialogContent="let ctx"
      >
        <hlm-dialog-header class="w-full text-left">
          <h3 hlmDialogTitle>Patient Information</h3>
          <p hlmDialogDescription>
            Detailed information regarding the patient's data
          </p>
        </hlm-dialog-header>
        <app-patient-modal [patient]="params.data"></app-patient-modal>
        <hlm-dialog-footer>
          <button hlmButton hlmDialogClose>Close</button>
        </hlm-dialog-footer>
      </hlm-dialog-content>
    </hlm-dialog>
    <a [routerLink]="['/admin/dashboard/patients', params.data.id]">
      <button
        class="bg-gray-500 text-white text-xs px-4 py-1.5 rounded-xl shadow hover:bg-gray-600 mr-1.5 disabled:bg-gray-300 disabled:cursor-not-allowed"
      >
        <ng-icon name="bootstrapPencilSquare"></ng-icon> Update
      </button>
    </a>

    <button
      class="bg-red-500 text-white text-xs px-4 py-1.5 rounded-xl shadow hover:bg-red-600 mr-1.5 disabled:bg-red-300 disabled:cursor-not-allowed"
      brnDialogTrigger
      hlmBtn
      [disabled]="!params.data"
    >
      <ng-icon name="bootstrapTrash"></ng-icon> Trash
    </button>
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
}
