import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { Category, ICategory } from '../category.model';
import { CategoryService } from '../service/category.service';
import { CategoryDeleteDialogComponent } from '../delete/category-delete-dialog.component';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { MessageService, ConfirmationService } from 'primeng/api';
import { Observable } from 'rxjs';
import { PrimeNGConfig } from 'primeng/api';

@Component({
  selector: 'jhi-category',
  templateUrl: './category.component.html',
})
export class CategoryComponent implements OnInit {
  categories?: ICategory[];
  isLoading?: boolean = false;
  display?: boolean = false;
  isSaving?: boolean = false;
  category: ICategory = new Category();
  selectedCategories: ICategory[] = [];
  Delete?: any;
  editForm?: FormGroup;

  constructor(
    protected categoryService: CategoryService,
    protected modalService: NgbModal,
    protected messageService: MessageService,
    protected confirmationService: ConfirmationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private primengConfig: PrimeNGConfig
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.categoryService.query().subscribe(
      (res: HttpResponse<ICategory[]>) => {
        this.isLoading = false;
        this.categories = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
    this.primengConfig.ripple = true;
  }

  trackId(index: number, item: ICategory): number {
    return item.id!;
  }

  save(): void {
    this.isSaving = true;
    if (this.category.id !== undefined) {
      this.subscribeToSaveResponse(this.categoryService.update(this.category));
      this.messageService.add({ severity: 'info', summary: 'Confirmation', detail: 'Modification réussi!' });
      this.loadAll();
    } else {
      this.subscribeToSaveResponse(this.categoryService.create(this.category));
      this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Enregistrement réussi!' });
      this.loadAll();
    }
    this.category = new Category();
    this.display = false;
    this.loadAll();
  }

  update(category: Category): void {
    if (category.id !== undefined) {
      this.category = category;
    } else {
      this.category = new Category();
    }
    this.display = true;
    this.loadAll();
  }

  delete(category: ICategory): void {
    const modalRef = this.modalService.open(CategoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.category = category;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  showDialog(): void {
    this.display = true;
  }

  hiddenDialog(): void {
    this.display = false;
  }

  deleteSelectedCategories(): void {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer les produits selectionnés?',
      header: 'Confirmation de supression',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.categoryService.deleteAll(this.selectedCategories).subscribe((res: HttpResponse<ICategory[]>) => {
          window.console.log(res.body);
          this.loadAll();
        });
      },
    });
  }

  deleteCategory(category: ICategory): void {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer?',
      header: 'Confirmation de suppresion',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        category.deleted = true;
        this.subscribeToSaveResponse(this.categoryService.update(category));
        this.messageService.add({ severity: 'error', summary: 'Confimation', detail: 'Catégorie supprimé avec succès!' });
        this.loadAll();
      },
    });
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategory>>): void {
    result.pipe().subscribe(
      // () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
    this.loadAll();
  }
}
