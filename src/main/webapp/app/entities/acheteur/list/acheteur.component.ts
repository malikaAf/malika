import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { Acheteur, IAcheteur } from '../acheteur.model';
import { AcheteurService } from '../service/acheteur.service';
import { AcheteurDeleteDialogComponent } from '../delete/acheteur-delete-dialog.component';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-acheteur',
  templateUrl: './acheteur.component.html',
})
export class AcheteurComponent implements OnInit {
  acheteurs?: IAcheteur[];
  isLoading = false;
  acheteur: IAcheteur = new Acheteur();
  selectedAcheteurs: IAcheteur[] = [];
  display = false;
  Delete?: any;
  isSaving?: boolean;
  filter?: boolean = false;

  

  constructor(
    protected acheteurService: AcheteurService,
    protected modalService: NgbModal,
    protected messageService: MessageService,
    protected confirmationService: ConfirmationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder) {          
    
  }

  loadAll(): void {
    this.isLoading = true;

    this.acheteurService.query().subscribe(
      (res: HttpResponse<IAcheteur[]>) => {
        this.isLoading = false;
        this.acheteurs = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAcheteur): number {
    return item.id!;
  }

  delete(acheteur: IAcheteur): void {
    const modalRef = this.modalService.open(AcheteurDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.acheteur = acheteur;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  save(): void {
    this.isSaving = true;
    if (this.acheteur.id !== undefined) {
      this.subscribeToSaveResponse(this.acheteurService.update(this.acheteur));
      this.messageService.add({ severity: 'info', summary: 'Confirmation', detail: 'Modification réussi!' });
      this.loadAll();
    } else {
      this.subscribeToSaveResponse(this.acheteurService.create(this.acheteur));
      this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Enregistrement réussi!' });
      this.loadAll();
    }
    this.acheteur = new Acheteur();
    this.display = false;
    this.loadAll();
  }

  update(acheteur: IAcheteur): void {
    if (this.acheteur.id !== undefined) {
      this.acheteur = acheteur;
    } else {
      this.acheteur = new Acheteur();
    }
    this.display = true;
    this.loadAll();
  }

  deleteAcheteur(acheteur: IAcheteur): void {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        acheteur.deleted = true;
        this.subscribeToSaveResponse(this.acheteurService.update(acheteur));
        this.messageService.add({ severity: 'error', summary: 'Confimation de suppression', detail: 'Acheteur supprimé avec succès!' });
        this.loadAll();
      },
    });
  }
  deleteSelectedAcheteurs(): void {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer les acheteurs selectionnés?',
      header: 'Confirmation de supression',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.acheteurService.deleteAll(this.selectedAcheteurs).subscribe((res: HttpResponse<IAcheteur[]>) => {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAcheteur>>): void {
    result.pipe().subscribe(
      // () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
    this.loadAll();
  }



}
