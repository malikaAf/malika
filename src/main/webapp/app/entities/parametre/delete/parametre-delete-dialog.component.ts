import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IParametre } from '../parametre.model';
import { ParametreService } from '../service/parametre.service';

@Component({
  templateUrl: './parametre-delete-dialog.component.html',
})


export class ParametreDeleteDialogComponent {
  parametre?: IParametre;

  constructor(protected parametreService: ParametreService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.parametreService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
