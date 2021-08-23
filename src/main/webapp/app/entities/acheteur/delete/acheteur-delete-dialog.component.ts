import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAcheteur } from '../acheteur.model';
import { AcheteurService } from '../service/acheteur.service';

@Component({
  templateUrl: './acheteur-delete-dialog.component.html',
})
export class AcheteurDeleteDialogComponent {
  acheteur?: IAcheteur;

  constructor(protected acheteurService: AcheteurService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.acheteurService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
