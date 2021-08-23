import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISortie, Sortie } from '../sortie.model';
import { SortieService } from '../service/sortie.service';
import { SortieDeleteDialogComponent } from '../delete/sortie-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { IEntree } from '../../entree/entree.model';
import { EntreeService } from '../../entree/service/entree.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { Table } from 'primeng/table';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { AcheteurService } from '../../acheteur/service/acheteur.service';
import { IAcheteur } from '../../acheteur/acheteur.model';

@Component({
  selector: 'jhi-sortie',
  templateUrl: './sortie.component.html',
})
export class SortieComponent implements OnInit {
  sorties?: ISortie[];
  sortie: ISortie = new Sortie();
  isLoading = false;
  entrees: IEntree[] = [];
  selectedSorties: ISortie[] = [];
  acheteurs: IAcheteur[] = [];
  displayDeletedListe = false;
  display = false;
  deleteDataVar: ISortie[] = [];

  displayDetail = false;
  Delete?: any;

  filter?: boolean = false;

  loading = false;

  isSaving = false;

  constructor(
    protected sortieService: SortieService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    protected entreeService: EntreeService,
    protected messageService: MessageService,
    protected confirmationService: ConfirmationService,
    protected acheteurService: AcheteurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.sortieService.query().subscribe(
      (res: HttpResponse<ISortie[]>) => {
        this.isLoading = false;
        this.sorties = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );

    this.entreeService.query().subscribe(
      (res: HttpResponse<IEntree[]>) => {
        this.isLoading = false;
        this.entrees = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );

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

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(sortie: ISortie): void {
    const modalRef = this.modalService.open(SortieDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sortie = sortie;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  clear(table: Table): void {
    table.clear();
  }

  trackId(index: number, item: IEntree): number {
    return item.id!;
  }

  save(): void {
    this.isSaving = true;
    if (this.sortie.id !== undefined) {
      this.subscribeToSaveResponse(this.sortieService.update(this.sortie));
      this.messageService.add({ severity: 'info', summary: 'Confirmation', detail: 'Modification réussi!' });
      this.loadAll();
    } else {
      this.subscribeToSaveResponse(this.sortieService.create(this.sortie));
      this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Enregistrement réussi!' });
      this.loadAll();
    }
    this.sortie = new Sortie();
    this.display = false;
  }

  update(sortie: ISortie): void {
    if (sortie.id !== undefined) {
      this.sortie = sortie;
      this.sortie.entree = this.entrees.find(entree => (entree.id = this.sortie.entree?.id));
      this.sortie.acheteur = this.acheteurs.find(acheteur => (acheteur.id = this.sortie.acheteur?.id));
    } else {
      this.sortie = new Sortie();
      this.loadAll();
    }
    this.display = true;
  }
  detail(sortie: ISortie): void {
    if (sortie.id !== undefined) {
      this.sortie = sortie;
      this.sortie.entree = this.entrees.find(entree => (entree.id = this.sortie.entree?.id));
      this.sortie.acheteur = this.acheteurs.find(acheteur => (acheteur.id = this.sortie.acheteur?.id));
    } else {
      this.sortie = new Sortie();
    }
    this.displayDetail = true;
  }

  deleteSortie(sortie: ISortie): void {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        sortie.deleted = true;
        this.subscribeToSaveResponse(this.sortieService.update(sortie));
        this.messageService.add({ severity: 'error', summary: 'Confimation de suppression', detail: 'Sortie supprimé avec succès!' });
        this.loadAll();
      },
    });
  }

  deleteSelectedSorties(): void {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer les sorties selectionnés?',
      header: 'Confirmation de supression',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.sortieService.deleteAll(this.selectedSorties).subscribe((res: HttpResponse<ISortie[]>) => {
          window.console.log(res.body);
          this.loadAll();
        });
      },
    });
  }

  getAllDeleted(): void {
    this.sortieService.getAllDeleted().subscribe(deleteData => {
      this.deleteDataVar = deleteData;
      this.displayDeletedListe = true;
      this.display = false;
    });
  }

  restoreEntree(sortie: ISortie): void {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir restorer?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        sortie.deleted = false;
        this.entreeService.update2(sortie).subscribe();
        this.messageService.add({ severity: 'success', summary: 'Confimation de restauration', detail: ' Restauration réussie!' });
        this.loadAll();
      },
    });
    this.displayDeletedListe = false;
  }

  showDialog(): void {
    this.display = true;
  }

  hiddenDialog(): void {
    this.display = false;
  }

  hiddenDialogDetail(): void {
    this.displayDetail = false;
  }

  /*
      previousState(): void {
        window.history.back();
      }
    */

  /*  protected onSaveSuccess(): void {
      this.previousState();
    }*/

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }
  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISortie>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      // () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
    this.loadAll();
  }

  // detailSortie(sortie: ISortie): void {
  //   // to do
  // }
}
