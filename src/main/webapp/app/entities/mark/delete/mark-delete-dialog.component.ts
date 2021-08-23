import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMark } from '../mark.model';
import { MarkService } from '../service/mark.service';

@Component({
  templateUrl: './mark-delete-dialog.component.html',
})
export class MarkDeleteDialogComponent {
  mark?: IMark;

  constructor(protected markService: MarkService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.markService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
