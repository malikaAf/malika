import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEntree } from '../entree.model';
import { EntreeService } from '../service/entree.service';

@Component({
  templateUrl: './entree-delete-dialog.component.html',
})
export class EntreeDeleteDialogComponent {
  entree?: IEntree;

  constructor(protected entreeService: EntreeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.entreeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
