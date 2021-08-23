import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { Article, IArticle } from '../article.model';
import { ArticleService } from '../service/article.service';
import { ArticleDeleteDialogComponent } from '../delete/article-delete-dialog.component';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { MarkService } from 'app/entities/mark/service/mark.service';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Observable } from 'rxjs';
import { IMark, Mark } from 'app/entities/mark/mark.model';

@Component({
  selector: 'jhi-article',
  templateUrl: './article.component.html',
})
export class ArticleComponent implements OnInit {
  articles?: IArticle[];
  marks: IMark[] = [];
  isLoading = false;
  display = false;
  article: IArticle = new Article();
  Delete?: any;
  isSaving?: boolean;
  filter?: boolean = false;
  selectedArticles: IArticle[] = [];
  mark: IMark = new Mark();

  constructor(
    protected articleService: ArticleService,
    protected markService: MarkService,
    protected messageService: MessageService,
    protected confirmationService: ConfirmationService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.articleService.query().subscribe(
      (res: HttpResponse<IArticle[]>) => {
        this.isLoading = false;
        this.articles = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );

    this.markService.query().subscribe(
      (res: HttpResponse<IMark[]>) => {
        this.isLoading = false;
        this.marks = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IArticle): number {
    return item.id!;
  }

  delete(article: IArticle): void {
    const modalRef = this.modalService.open(ArticleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.article = article;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  save(): void {
    this.isSaving = true;
    if (this.article.id !== undefined) {
      this.subscribeToSaveResponse(this.articleService.update(this.article));
      this.messageService.add({ severity: 'info', summary: 'Confirmation', detail: 'Modification réussi!' });
    } else {
      this.subscribeToSaveResponse(this.articleService.create(this.article));
      this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Enregistrement réussi!' });
    }
    this.article = new Article();
    this.display = false;
    this.loadAll();
  }

  update(article: IArticle): void {
    if (article.id !== undefined) {
      this.article = article;
      this.article.mark = this.articles?.find(art => (art.id = this.article.mark?.id));
    } else {
      this.article = new Article();
    }
    this.display = true;
    this.loadAll();
  }

  deleteArticle(article: IArticle): void {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        article.deleted = true;
        this.subscribeToSaveResponse(this.articleService.update(article));
        this.messageService.add({ severity: 'error', summary: 'Confimation de suppression', detail: 'Article supprimé avec succès!' });
        this.loadAll();
      },
    });
  }

  deleteSelectedArticles(): void {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer les produits selectionnés?',
      header: 'Confirmation de supression',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.articleService.deleteAll(this.selectedArticles).subscribe((res: HttpResponse<IArticle[]>) => {
          window.console.log(res.body);
          this.loadAll();
        });
      },
    });
  }

  trackMarkById(index: number, item: IMark): number {
    return item.id!;
  }

  showDialog(): void {
    this.display = true;
  }

  hiddenDialog(): void {
    this.display = false;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArticle>>): void {
    result.pipe().subscribe(
      // () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
    this.loadAll();
  }

  // protected previousState(): any {
  //   throw new Error('Method not implemented.');
  // }
  //  onSaveSuccess(): void {
  //   this.previousState();
  // }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }
}
