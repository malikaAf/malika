import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { Fournisseur, IFournisseur } from '../fournisseur.model';
import { FournisseurService } from '../service/fournisseur.service';
import { FournisseurDeleteDialogComponent } from '../delete/fournisseur-delete-dialog.component';
import { Observable } from 'rxjs';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'jhi-fournisseur',
  templateUrl: './fournisseur.component.html',
})
export class FournisseurComponent implements OnInit {
  fournisseurs?: IFournisseur[];
  fournisseur: IFournisseur = new Fournisseur();
  selectedFournisseurs: IFournisseur[] = [];
  isLoading = false;
  display = false;
  Delete?: any;
  isSaving?: boolean;
  filter?: boolean = false;

  constructor(
    protected fournisseurService: FournisseurService,
    protected modalService: NgbModal,
    protected messageService: MessageService,
    protected confirmationService: ConfirmationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.fournisseurService.query().subscribe(
      (res: HttpResponse<IFournisseur[]>) => {
        this.isLoading = false;
        this.fournisseurs = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFournisseur): number {
    return item.id!;
  }

  delete(fournisseur: IFournisseur): void {
    const modalRef = this.modalService.open(FournisseurDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fournisseur = fournisseur;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  save(): void {
    this.isSaving = true;
    if (this.fournisseur.id !== undefined) {
      this.subscribeToSaveResponse(this.fournisseurService.update(this.fournisseur));
      this.messageService.add({ severity: 'info', summary: 'Confirmation', detail: 'Modification r??ussi!' });
      this.loadAll();
    } else {
      this.subscribeToSaveResponse(this.fournisseurService.create(this.fournisseur));
      this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Enregistrement r??ussi!' });
      this.loadAll();
    }
    this.fournisseur = new Fournisseur();
    this.display = false;
    this.loadAll();
  }

  update(fournisseur: IFournisseur): void {
    if (fournisseur.id !== undefined) {
      this.fournisseur = fournisseur;
    } else {
      this.fournisseur = new Fournisseur();
    }
    this.display = true;
    this.loadAll();
  }

  deleteFournisseur(fournisseur: IFournisseur): void {
    this.confirmationService.confirm({
      message: '??tes-vous s??r de vouloir supprimer?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        fournisseur.deleted = true;
        this.subscribeToSaveResponse(this.fournisseurService.update(fournisseur));
        this.messageService.add({ severity: 'error', summary: 'Confimation de suppression', detail: 'Fournisseur supprim?? avec succ??s!' });
        this.loadAll();
      },
    });
  }
  deleteSelectedFournisseurs(): void {
    this.confirmationService.confirm({
      message: '??tes-vous s??r de vouloir supprimer les fournisseurs selectionn??s?',
      header: 'Confirmation de supression',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.fournisseurService.deleteAll(this.selectedFournisseurs).subscribe((res: HttpResponse<IFournisseur[]>) => {
          window.console.log(res.body);
          this.loadAll();
        });
      },
    });
  }

  showDialog(): void {
    this.display = true;
  }

  hiddenDialog(): void {
    this.display = false;
  }

  onCountryChange($event: any): void {
    alert($event);
    window.console.log($event);
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFournisseur>>): void {
    result.pipe().subscribe(
      // () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
    this.loadAll();
  }
}
