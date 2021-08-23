import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { Entree, IEntree } from '../entree.model';
import { EntreeService } from '../service/entree.service';
import { EntreeDeleteDialogComponent } from '../delete/entree-delete-dialog.component';
import { Table } from 'primeng/table';
import { FormBuilder } from '@angular/forms';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { ArticleService } from 'app/entities/article/service/article.service';
import { ExerciceService } from 'app/entities/exercice/service/exercice.service';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';
import { ParametreService } from 'app/entities/parametre/service/parametre.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { IArticle } from '../../article/article.model';
import { IFournisseur } from '../../fournisseur/fournisseur.model';
import { IExercice } from '../../exercice/exercice.model';
import { IParametre } from '../../parametre/parametre.model';
import { SortieService } from 'app/entities/sortie/service/sortie.service';

@Component({
  selector: 'jhi-entree',
  templateUrl: './entree.component.html',
})
export class EntreeComponent implements OnInit {
  entrees?: IEntree[];
  isLoading = false;
  selectedEntrees: IEntree[] = [];
  entree: IEntree = new Entree();
  articles: IArticle[] = [];
  fournisseurs: IFournisseur[] = [];
  exercices: IExercice[] = [];
  parametres: IParametre[] = [];
  displayDetail = false;
  displayDeletedListe = false;
  reste: any;

  display = false;
  Delete?: any;

  filter?: boolean = false;

  loading = false;

  isSaving = false;

  deleteDataVar: IEntree[] = [];

  constructor(
    protected entreeService: EntreeService,
    protected messageService: MessageService,
    protected confirmationService: ConfirmationService,
    protected modalService: NgbModal,
    protected articleService: ArticleService,
    protected fournisseurService: FournisseurService,
    protected sortieService: SortieService,
    protected exerciceService: ExerciceService,
    protected parametreService: ParametreService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.entreeService.query().subscribe(
      (res: HttpResponse<IEntree[]>) => {
        this.isLoading = false;
        this.entrees = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );

    this.articleService.query().subscribe(
      (res: HttpResponse<IArticle[]>) => {
        this.isLoading = false;
        this.articles = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );

    this.parametreService.query().subscribe(
      (res: HttpResponse<IParametre[]>) => {
        this.isLoading = false;
        this.parametres = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );

    this.exerciceService.query().subscribe(
      (res: HttpResponse<IExercice[]>) => {
        this.isLoading = false;
        this.exercices = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );

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

  clear(table: Table): void {
    table.clear();
  }

  trackId(index: number, item: IEntree): number {
    return item.id!;
  }

  save(): void {
    this.isSaving = true;
    if (this.entree.id !== undefined) {
      this.subscribeToSaveResponse(this.entreeService.update(this.entree));
      this.messageService.add({ severity: 'info', summary: 'Confirmation', detail: 'Modification réussi!' });
    } else {
      this.subscribeToSaveResponse(this.entreeService.create(this.entree));
      this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Enregistrement réussi!' });
    }
    this.entree = new Entree();
    this.display = false;
    this.loadAll();
  }

  update(entree: IEntree): void {
    if (entree.id !== undefined) {
      this.entree = entree;
      this.entree.article = this.articles.find(article => (article.id = this.entree.article?.id));
      this.entree.fournisseur = this.fournisseurs.find(fournisseur => (fournisseur.id = this.entree.fournisseur?.id));
      this.entree.exercice = this.exercices.find(exercice => (exercice.id = this.entree.exercice?.id));
      this.entree.parametre = this.parametres.find(parametre => (parametre.id = this.entree.parametre?.id));
      this.loadAll();
    } else {
      this.entree = new Entree();
      this.loadAll();
    }
    this.display = true;
    this.loadAll();
  }

  detail(entree: IEntree): void {
    if (entree.id !== undefined) {
      this.entree = entree;
      this.entree.article = this.articles.find(article => (article.id = this.entree.article?.id));
      this.entree.fournisseur = this.fournisseurs.find(fournisseur => (fournisseur.id = this.entree.fournisseur?.id));
      this.entree.exercice = this.exercices.find(exercice => (exercice.id = this.entree.exercice?.id));
      this.entree.parametre = this.parametres.find(parametre => (parametre.id = this.entree.parametre?.id));
      this.loadAll();
    } else {
      this.entree = new Entree();
      this.loadAll();
    }
    this.displayDetail = true;
    this.loadAll();
  }

  delete(entree: IEntree): void {
    const modalRef = this.modalService.open(EntreeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.entree = entree;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  // rest(reste: any): void {
  //     this.sortieService.reste(reste).subscribe(
  //       data => {
  //         this.reste = data;
  //       }
  //     )
  // }

  deleteEntree(entree: IEntree): void {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        entree.deleted = true;
        this.subscribeToSaveResponse(this.entreeService.update(entree));
        this.messageService.add({ severity: 'error', summary: 'Confimation de suppression', detail: 'Entrée supprimé avec succès!' });
        this.loadAll();
      },
    });
  }

  deleteSelectedEntrees(): void {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer les entrées selectionnés?',
      header: 'Confirmation de supression',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.entreeService.deleteAll(this.selectedEntrees).subscribe((res: HttpResponse<IEntree[]>) => {
          window.console.log(res.body);
          this.loadAll();
        });
      },
    });
  }

  getAllDeleted(): void {
    this.entreeService.getAllDeleted().subscribe(deleteData => {
      window.console.log(deleteData);
      this.deleteDataVar = deleteData;
      this.displayDeletedListe = true;
      this.display = false;
    });
  }

  restoreEntree(entree: IEntree): void {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir restorer?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        entree.deleted = false;
        this.entreeService.update2(entree).subscribe();
        this.messageService.add({ severity: 'success', summary: 'Confimation de restauration', detail: 'Entrée restorée avec succès!' });
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

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEntree>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      // () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
    this.loadAll();
  }
}
