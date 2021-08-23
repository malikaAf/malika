import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMark, Mark } from '../mark.model';
import { MarkService } from '../service/mark.service';
import { ConfirmationService } from 'primeng/api';
import { MessageService } from 'primeng/api';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { CategoryService } from 'app/entities/category/service/category.service';
import { Category, ICategory } from 'app/entities/category/category.model';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-mark',
  templateUrl: './mark.component.html',
})
export class MarkComponent implements OnInit {
  marks?: IMark[];
  categories: ICategory[] = [];
  cat: ICategory = new Category();
  isLoading = false;
  display = false;
  mark: IMark = new Mark();
  Delete?: any;
  isSaving?: boolean;
  filter?: boolean = false;
  selectedMarks: IMark[] = [];

  constructor(
    protected markService: MarkService,
    protected messageService: MessageService,
    protected confirmationService: ConfirmationService,
    protected modalService: NgbModal,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.markService.query().subscribe(
      (res: HttpResponse<IMark[]>) => {
        this.isLoading = false;
        this.marks = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );

    this.categoryService.query().subscribe((res: HttpResponse<ICategory[]>) => {
      this.categories = res.body ?? [];
    });
  }
  ngOnInit(): void {
    this.loadAll();
  }
  trackId(index: number, item: IMark): number {
    return item.id!;
  }
  trackCategoryById(index: number, item: ICategory): number {
    return item.id!;
  }
  save(): void {
    this.isSaving = true;
    if (this.mark.id !== undefined) {
      this.subscribeToSaveResponse(this.markService.update(this.mark));
      this.messageService.add({ severity: 'info', summary: 'Confirmation', detail: 'Modification réussi!' });
      this.loadAll();
    } else {
      this.subscribeToSaveResponse(this.markService.create(this.mark));
      this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Enregistrement réussi!' });
      this.loadAll();
    }
    this.mark = new Mark();
    this.display = false;
    this.loadAll();
  }

  update(mark: IMark): void {
    if (mark.id !== undefined) {
      this.mark = mark;
      this.mark.category = this.categories.find(cat => (cat.id = this.mark.category?.id));
    } else {
      this.mark = new Mark();
    }
    this.display = true;
    this.loadAll();
  }

  delete(mark: IMark): void {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        mark.deleted = true;
        this.subscribeToSaveResponse(this.markService.update(mark));
        this.messageService.add({ severity: 'error', summary: 'Confimation de suppression', detail: 'marque supprimé avec succès!' });
        this.loadAll();
      },
    });
  }
  deleteSelectedMarks(): void {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer les produits selectionnés?',
      header: 'Confirmation de supression',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.markService.deleteAll(this.selectedMarks).subscribe((res: HttpResponse<IMark[]>) => {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMark>>): void {
    result.pipe().subscribe(
      // () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
    this.loadAll();
  }
}
