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
import { HlmButtonDirective } from '@spartan-ng/ui-button-helm'
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
        class="max-w-[300px] lg:max-w-[700px] text-left h-[90vh]"
        *brnDialogContent="let ctx"
      >
        <hlm-dialog-header class="w-full text-left">
          <h3 hlmDialogTitle>Invoice Details</h3>
          <p hlmDialogDescription>Invoice Details</p>
        </hlm-dialog-header>
        <!-- <app-invoice-modal [invoice]="params.data"></app-invoice-modal> -->
        <hlm-dialog-footer>
          <button hlmButton hlmDialogClose>Close</button>
        </hlm-dialog-footer>
      </hlm-dialog-content>
    </hlm-dialog>
    <button
      class="bg-gray-500 text-white text-xs px-4 py-1.5 rounded-xl shadow hover:bg-gray-600 mr-1.5 disabled:bg-gray-300 disabled:cursor-not-allowed"
      brnDialogTrigger
      hlmBtn
    >
      <ng-icon name="bootstrapPencilSquare"></ng-icon> Update
    </button>
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
