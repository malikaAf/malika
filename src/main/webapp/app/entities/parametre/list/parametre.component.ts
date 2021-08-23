import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IParametre, Parametre } from '../parametre.model';
import { ParametreService } from '../service/parametre.service';
import { ParametreDeleteDialogComponent } from '../delete/parametre-delete-dialog.component';
import { ICategory } from '../../category/category.model';
import { MessageService, ConfirmationService } from 'primeng/api';
import { CategoryService } from '../../category/service/category.service';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-parametre',
  templateUrl: './parametre.component.html',
})
export class ParametreComponent implements OnInit {
  
  parametres?: IParametre[];
  isLoading = false;
  categories: ICategory[] = [];
  display = false;
  parametre: IParametre = new Parametre();
  Delete?: any;
  isSaving?: boolean;
  filter?: boolean = false;
  selectedParametres: IParametre[] = [];

  constructor(
    protected parametreService: ParametreService,
    protected modalService: NgbModal,
    protected messageService: MessageService,
    protected confirmationService: ConfirmationService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder   
  ) {} 

  loadAll(): void {
    this.isLoading = true;

    this.parametreService.query().subscribe(
      (res: HttpResponse<IParametre[]>) => {
        this.isLoading = false;
        this.parametres = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IParametre): number {
    return item.id!;
  }

  

  
  save(): void {
    this.isSaving = true;
    if (this.parametre.id !== undefined) {
      this.subscribeToSaveResponse(this.parametreService.update(this.parametre));
      this.messageService.add({ severity: 'info', summary: 'Confirmation', detail: 'Modification réussi!' });
      this.loadAll();
    } else {
      this.subscribeToSaveResponse(this.parametreService.create(this.parametre));
      this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Enregistrement réussi!' });
      this.loadAll();
    }
    this.parametre = new Parametre();
    this.display = false;
    this.loadAll();
  }

  update(parametre: IParametre): void {
    if (parametre.id !== undefined) {
      this.parametre =parametre;
     
    } else {
      this.parametre = new Parametre();
    }
    this.display = true;
    this.loadAll();
  }

  deleteParametre(parametre: IParametre): void {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer?',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        parametre.deleted = true;
        this.subscribeToSaveResponse(this.parametreService.update(this.parametre));
        this.messageService.add({ severity: 'error', summary: 'Confimation de suppression', detail: 'parametre supprimé avec succès!' });
        this.loadAll();
      },
    });
  }
  deleteSelectedParametres(): void {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer les produits selectionnés?',
      header: 'Confirmation de supression',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.parametreService.deleteAll(this.selectedParametres).subscribe((res: HttpResponse<IParametre[]>) => {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParametre>>): void {
    result.pipe().subscribe(
      // () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
    this.loadAll();
  }
}
