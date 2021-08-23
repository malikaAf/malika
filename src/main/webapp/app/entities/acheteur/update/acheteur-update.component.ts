import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAcheteur, Acheteur } from '../acheteur.model';
import { AcheteurService } from '../service/acheteur.service';

@Component({
  selector: 'jhi-acheteur-update',
  templateUrl: './acheteur-update.component.html',
})
export class AcheteurUpdateComponent implements OnInit {
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

  constructor(protected acheteurService: AcheteurService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ acheteur }) => {
      this.updateForm(acheteur);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const acheteur = this.createFromForm();
    if (acheteur.id !== undefined) {
      this.subscribeToSaveResponse(this.acheteurService.update(acheteur));
    } else {
      this.subscribeToSaveResponse(this.acheteurService.create(acheteur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAcheteur>>): void {
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

  protected updateForm(acheteur: IAcheteur): void {
    this.editForm.patchValue({
      id: acheteur.id,
      typeClient: acheteur.typeClient,
      nom: acheteur.nom,
      prenom: acheteur.prenom,
      tel: acheteur.tel,
      cnib: acheteur.cnib,
      email: acheteur.email,
      adresse: acheteur.adresse,
      numroBanquaire: acheteur.numroBanquaire,
      deleted: acheteur.deleted,
    });
  }

  protected createFromForm(): IAcheteur {
    return {
      ...new Acheteur(),
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
