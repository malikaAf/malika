import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISortie, Sortie } from '../sortie.model';
import { SortieService } from '../service/sortie.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IEntree } from 'app/entities/entree/entree.model';
import { EntreeService } from 'app/entities/entree/service/entree.service';
import { IAcheteur } from 'app/entities/acheteur/acheteur.model';
import { AcheteurService } from 'app/entities/acheteur/service/acheteur.service';

@Component({
  selector: 'jhi-sortie-update',
  templateUrl: './sortie-update.component.html',
})
export class SortieUpdateComponent implements OnInit {
  isSaving = false;

  entreesSharedCollection: IEntree[] = [];
  acheteursSharedCollection: IAcheteur[] = [];

  editForm = this.fb.group({
    id: [],
    libelle: [],
    quantite: [null, [Validators.required, Validators.min(1)]],
    prixUnitaireTTC: [null, [Validators.required, Validators.min(1)]],
    dateSortie: [],
    bordereau: [],
    bordereauContentType: [],
    observation: [null, [Validators.required, Validators.minLength(10), Validators.maxLength(1024)]],
    deleted: [null, [Validators.required]],
    entree: [],
    acheteur: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected sortieService: SortieService,
    protected entreeService: EntreeService,
    protected acheteurService: AcheteurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sortie }) => {
      this.updateForm(sortie);

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
    const sortie = this.createFromForm();
    if (sortie.id !== undefined) {
      this.subscribeToSaveResponse(this.sortieService.update(sortie));
    } else {
      this.subscribeToSaveResponse(this.sortieService.create(sortie));
    }
  }

  trackEntreeById(index: number, item: IEntree): number {
    return item.id!;
  }

  trackAcheteurById(index: number, item: IAcheteur): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISortie>>): void {
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

  protected updateForm(sortie: ISortie): void {
    this.editForm.patchValue({
      id: sortie.id,
      libelle: sortie.libelle,
      quantite: sortie.quantite,
      prixUnitaireTTC: sortie.prixUnitaireTTC,
      dateSortie: sortie.dateSortie,
      bordereau: sortie.bordereau,
      bordereauContentType: sortie.bordereauContentType,
      observation: sortie.observation,
      deleted: sortie.deleted,
      entree: sortie.entree,
      acheteur: sortie.acheteur,
    });

    this.entreesSharedCollection = this.entreeService.addEntreeToCollectionIfMissing(this.entreesSharedCollection, sortie.entree);
    this.acheteursSharedCollection = this.acheteurService.addAcheteurToCollectionIfMissing(this.acheteursSharedCollection, sortie.acheteur);
  }

  protected loadRelationshipsOptions(): void {
    this.entreeService
      .query()
      .pipe(map((res: HttpResponse<IEntree[]>) => res.body ?? []))
      .pipe(map((entrees: IEntree[]) => this.entreeService.addEntreeToCollectionIfMissing(entrees, this.editForm.get('entree')!.value)))
      .subscribe((entrees: IEntree[]) => (this.entreesSharedCollection = entrees));

    this.acheteurService
      .query()
      .pipe(map((res: HttpResponse<IAcheteur[]>) => res.body ?? []))
      .pipe(
        map((acheteurs: IAcheteur[]) =>
          this.acheteurService.addAcheteurToCollectionIfMissing(acheteurs, this.editForm.get('acheteur')!.value)
        )
      )
      .subscribe((acheteurs: IAcheteur[]) => (this.acheteursSharedCollection = acheteurs));
  }

  protected createFromForm(): ISortie {
    return {
      ...new Sortie(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      prixUnitaireTTC: this.editForm.get(['prixUnitaireTTC'])!.value,
      dateSortie: this.editForm.get(['dateSortie'])!.value,
      bordereauContentType: this.editForm.get(['bordereauContentType'])!.value,
      bordereau: this.editForm.get(['bordereau'])!.value,
      observation: this.editForm.get(['observation'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
      entree: this.editForm.get(['entree'])!.value,
      acheteur: this.editForm.get(['acheteur'])!.value,
    };
  }
}
