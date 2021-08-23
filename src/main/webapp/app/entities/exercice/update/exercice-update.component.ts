import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IExercice, Exercice } from '../exercice.model';
import { ExerciceService } from '../service/exercice.service';

@Component({
  selector: 'jhi-exercice-update',
  templateUrl: './exercice-update.component.html',
})
export class ExerciceUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    annee: [null, [Validators.min(2)]],
    deleted: [null, [Validators.required]],
  });

  constructor(protected exerciceService: ExerciceService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exercice }) => {
      this.updateForm(exercice);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const exercice = this.createFromForm();
    if (exercice.id !== undefined) {
      this.subscribeToSaveResponse(this.exerciceService.update(exercice));
    } else {
      this.subscribeToSaveResponse(this.exerciceService.create(exercice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExercice>>): void {
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

  protected updateForm(exercice: IExercice): void {
    this.editForm.patchValue({
      id: exercice.id,
      annee: exercice.annee,
      deleted: exercice.deleted,
    });
  }

  protected createFromForm(): IExercice {
    return {
      ...new Exercice(),
      id: this.editForm.get(['id'])!.value,
      annee: this.editForm.get(['annee'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
    };
  }
}
