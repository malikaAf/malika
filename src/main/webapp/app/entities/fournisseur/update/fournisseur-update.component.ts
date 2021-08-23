import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFournisseur, Fournisseur } from '../fournisseur.model';
import { FournisseurService } from '../service/fournisseur.service';

@Component({
  selector: 'jhi-fournisseur-update',
  templateUrl: './fournisseur-update.component.html',
})
export class FournisseurUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    typeClient: [],
    nom: [null, [Validators.required, Validators.minLength(2)]],
    prenom: [null, [Validators.minLength(2)]],
    tel: [null, [Validators.required, Validators.minLength(6)]],
    cnib: [null, [Validators.required, Validators.minLength(4)]],
    email: [null, [Validators.required, Validators.minLength(4)]],
    adresse: [null, [Validators.minLength(2)]],
    numroBanquaire: [null, [Validators.minLength(4)]],
    deleted: [null, [Validators.required]],
  });

  constructor(protected fournisseurService: FournisseurService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fournisseur }) => {
      this.updateForm(fournisseur);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fournisseur = this.createFromForm();
    if (fournisseur.id !== undefined) {
      this.subscribeToSaveResponse(this.fournisseurService.update(fournisseur));
    } else {
      this.subscribeToSaveResponse(this.fournisseurService.create(fournisseur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFournisseur>>): void {
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

  protected updateForm(fournisseur: IFournisseur): void {
    this.editForm.patchValue({
      id: fournisseur.id,
      typeClient: fournisseur.typeClient,
      nom: fournisseur.nom,
      prenom: fournisseur.prenom,
      tel: fournisseur.tel,
      cnib: fournisseur.cnib,
      email: fournisseur.email,
      adresse: fournisseur.adresse,
      numroBanquaire: fournisseur.numroBanquaire,
      deleted: fournisseur.deleted,
    });
  }

  protected createFromForm(): IFournisseur {
    return {
      ...new Fournisseur(),
      id: this.editForm.get(['id'])!.value,
      typeClient: this.editForm.get(['typeClient'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      tel: this.editForm.get(['tel'])!.value,
      cnib: this.editForm.get(['cnib'])!.value,
      email: this.editForm.get(['email'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      numroBanquaire: this.editForm.get(['numroBanquaire'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
    };
  }
}
