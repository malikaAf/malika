import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEntree, Entree } from '../entree.model';
import { EntreeService } from '../service/entree.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IArticle } from 'app/entities/article/article.model';
import { ArticleService } from 'app/entities/article/service/article.service';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';
import { IExercice } from 'app/entities/exercice/exercice.model';
import { ExerciceService } from 'app/entities/exercice/service/exercice.service';
import { IParametre } from 'app/entities/parametre/parametre.model';
import { ParametreService } from 'app/entities/parametre/service/parametre.service';

@Component({
  selector: 'jhi-entree-update',
  templateUrl: './entree-update.component.html',
})
export class EntreeUpdateComponent implements OnInit {
  isSaving = false;

  articlesSharedCollection: IArticle[] = [];
  fournisseursSharedCollection: IFournisseur[] = [];
  exercicesSharedCollection: IExercice[] = [];
  parametresSharedCollection: IParametre[] = [];

  editForm = this.fb.group({
    id: [],
    libelle: [],
    quantite: [null, [Validators.required, Validators.min(1)]],
    prixUnitaireTTC: [null, [Validators.required, Validators.min(1)]],
    serie: [null, [Validators.required]],
    model: [null, [Validators.required]],
    caractSupplementaire: [],
    dateEntree: [],
    bordereau: [],
    bordereauContentType: [],
    observation: [null, [Validators.required, Validators.minLength(10), Validators.maxLength(1024)]],
    enStock: [],
    enCommande: [],
    deleted: [null, [Validators.required]],
    article: [],
    fournisseur: [],
    exercice: [],
    parametre: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected entreeService: EntreeService,
    protected articleService: ArticleService,
    protected fournisseurService: FournisseurService,
    protected exerciceService: ExerciceService,
    protected parametreService: ParametreService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entree }) => {
      this.updateForm(entree);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('gestockApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const entree = this.createFromForm();
    if (entree.id !== undefined) {
      this.subscribeToSaveResponse(this.entreeService.update(entree));
    } else {
      this.subscribeToSaveResponse(this.entreeService.create(entree));
    }
  }

  trackArticleById(index: number, item: IArticle): number {
    return item.id!;
  }

  trackFournisseurById(index: number, item: IFournisseur): number {
    return item.id!;
  }

  trackExerciceById(index: number, item: IExercice): number {
    return item.id!;
  }

  trackParametreById(index: number, item: IParametre): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEntree>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(entree: IEntree): void {
    this.editForm.patchValue({
      id: entree.id,
      libelle: entree.libelle,
      quantite: entree.quantite,
      prixUnitaireTTC: entree.prixUnitaireTTC,
      serie: entree.serie,
      model: entree.model,
      caractSupplementaire: entree.caractSupplementaire,
      dateEntree: entree.dateEntree,
      bordereau: entree.bordereau,
      bordereauContentType: entree.bordereauContentType,
      observation: entree.observation,
      enStock: entree.enStock,
      enCommande: entree.enCommande,
      deleted: entree.deleted,
      article: entree.article,
      fournisseur: entree.fournisseur,
      exercice: entree.exercice,
      parametre: entree.parametre,
    });

    this.articlesSharedCollection = this.articleService.addArticleToCollectionIfMissing(this.articlesSharedCollection, entree.article);
    this.fournisseursSharedCollection = this.fournisseurService.addFournisseurToCollectionIfMissing(
      this.fournisseursSharedCollection,
      entree.fournisseur
    );
    this.exercicesSharedCollection = this.exerciceService.addExerciceToCollectionIfMissing(this.exercicesSharedCollection, entree.exercice);
    this.parametresSharedCollection = this.parametreService.addParametreToCollectionIfMissing(
      this.parametresSharedCollection,
      entree.parametre
    );
  }

  protected loadRelationshipsOptions(): void {
    this.articleService
      .query()
      .pipe(map((res: HttpResponse<IArticle[]>) => res.body ?? []))
      .pipe(
        map((articles: IArticle[]) => this.articleService.addArticleToCollectionIfMissing(articles, this.editForm.get('article')!.value))
      )
      .subscribe((articles: IArticle[]) => (this.articlesSharedCollection = articles));

    this.fournisseurService
      .query()
      .pipe(map((res: HttpResponse<IFournisseur[]>) => res.body ?? []))
      .pipe(
        map((fournisseurs: IFournisseur[]) =>
          this.fournisseurService.addFournisseurToCollectionIfMissing(fournisseurs, this.editForm.get('fournisseur')!.value)
        )
      )
      .subscribe((fournisseurs: IFournisseur[]) => (this.fournisseursSharedCollection = fournisseurs));

    this.exerciceService
      .query()
      .pipe(map((res: HttpResponse<IExercice[]>) => res.body ?? []))
      .pipe(
        map((exercices: IExercice[]) =>
          this.exerciceService.addExerciceToCollectionIfMissing(exercices, this.editForm.get('exercice')!.value)
        )
      )
      .subscribe((exercices: IExercice[]) => (this.exercicesSharedCollection = exercices));

    this.parametreService
      .query()
      .pipe(map((res: HttpResponse<IParametre[]>) => res.body ?? []))
      .pipe(
        map((parametres: IParametre[]) =>
          this.parametreService.addParametreToCollectionIfMissing(parametres, this.editForm.get('parametre')!.value)
        )
      )
      .subscribe((parametres: IParametre[]) => (this.parametresSharedCollection = parametres));
  }

  protected createFromForm(): IEntree {
    return {
      ...new Entree(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      prixUnitaireTTC: this.editForm.get(['prixUnitaireTTC'])!.value,
      serie: this.editForm.get(['serie'])!.value,
      model: this.editForm.get(['model'])!.value,
      caractSupplementaire: this.editForm.get(['caractSupplementaire'])!.value,
      dateEntree: this.editForm.get(['dateEntree'])!.value,
      bordereauContentType: this.editForm.get(['bordereauContentType'])!.value,
      bordereau: this.editForm.get(['bordereau'])!.value,
      observation: this.editForm.get(['observation'])!.value,
      enStock: this.editForm.get(['enStock'])!.value,
      enCommande: this.editForm.get(['enCommande'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
      article: this.editForm.get(['article'])!.value,
      fournisseur: this.editForm.get(['fournisseur'])!.value,
      exercice: this.editForm.get(['exercice'])!.value,
      parametre: this.editForm.get(['parametre'])!.value,
    };
  }
}
