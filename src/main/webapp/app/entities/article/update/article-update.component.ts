import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IArticle, Article } from '../article.model';
import { ArticleService } from '../service/article.service';
import { IMark } from 'app/entities/mark/mark.model';
import { MarkService } from 'app/entities/mark/service/mark.service';

@Component({
  selector: 'jhi-article-update',
  templateUrl: './article-update.component.html',
})
export class ArticleUpdateComponent implements OnInit {
  isSaving = false;

  marksSharedCollection: IMark[] = [];

  editForm = this.fb.group({
    id: [],
    libelle: [null, [Validators.required, Validators.minLength(2)]],
    deleted: [null, [Validators.required]],
    mark: [],
  });

  constructor(
    protected articleService: ArticleService,
    protected markService: MarkService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ article }) => {
      this.updateForm(article);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const article = this.createFromForm();
    if (article.id !== undefined) {
      this.subscribeToSaveResponse(this.articleService.update(article));
    } else {
      this.subscribeToSaveResponse(this.articleService.create(article));
    }
  }

  trackMarkById(index: number, item: IMark): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArticle>>): void {
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

  protected updateForm(article: IArticle): void {
    this.editForm.patchValue({
      id: article.id,
      libelle: article.libelle,
      deleted: article.deleted,
      mark: article.mark,
    });

    this.marksSharedCollection = this.markService.addMarkToCollectionIfMissing(this.marksSharedCollection, article.mark);
  }

  protected loadRelationshipsOptions(): void {
    this.markService
      .query()
      .pipe(map((res: HttpResponse<IMark[]>) => res.body ?? []))
      .pipe(map((marks: IMark[]) => this.markService.addMarkToCollectionIfMissing(marks, this.editForm.get('mark')!.value)))
      .subscribe((marks: IMark[]) => (this.marksSharedCollection = marks));
  }

  protected createFromForm(): IArticle {
    return {
      ...new Article(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      deleted: this.editForm.get(['deleted'])!.value,
      mark: this.editForm.get(['mark'])!.value,
    };
  }
}
