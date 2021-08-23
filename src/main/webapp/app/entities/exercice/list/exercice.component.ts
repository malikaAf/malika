import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IExercice, Exercice } from '../exercice.model';
import { ExerciceService } from '../service/exercice.service';
import { ExerciceDeleteDialogComponent } from '../delete/exercice-delete-dialog.component';
// import { ICategory, Category } from '../../category/category.model';
import { FormGroup, FormBuilder } from '@angular/forms';
// import { CategoryService } from '../../category/service/category.service';
// import { MessageService, ConfirmationService, PrimeNGConfig } from 'primeng/api';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { MessageService, ConfirmationService } from 'primeng/api';

@Component({
  selector: 'jhi-exercice',
  templateUrl: './exercice.component.html',
})

export class ExerciceComponent implements OnInit {
  exercices?: IExercice[];
  isLoading?: boolean = false;
  display?: boolean = false;
  isSaving?: boolean = false;
  exercice: IExercice = new Exercice();
  selectedExercices: IExercice[] = [];
  Delete?: any;
  editForm?: FormGroup;


  constructor(
    protected exerciceService: ExerciceService,
    protected modalService: NgbModal,
    protected exercieService: ExerciceService,
    protected messageService: MessageService,
    protected confirmationService: ConfirmationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.exerciceService.query().subscribe(
      (res: HttpResponse<IExercice[]>) => {
        this.isLoading = false;
        this.exercices = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();


  }

  trackId(index: number, item: IExercice): number {
    return item.id!;
  }

  delete(exercice: IExercice): void {
    const modalRef = this.modalService.open(ExerciceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.exercice = exercice;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }


  save(): void {
    this.isSaving = true;
    if (this.exercice.id !== undefined) {
      this.subscribeToSaveResponse(this.exerciceService.update(this.exercice));
      this.messageService.add({ severity: 'info', summary: 'Confirmation', detail: 'Modification réussi!' });
      this.loadAll();
    } else {
      this.subscribeToSaveResponse(this.exerciceService.create(this.exercice));
      this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Enregistrement réussi!' });
      this.loadAll();
    }
    this.exercice = new Exercice();
    this.display = false;
    this.loadAll();
  }




  update(exercice: IExercice): void {
    if (exercice.id !== undefined) {
      this.exercice = exercice;
    } else {
      this.exercice = new Exercice();
    }
    this.display = true;
    this.loadAll();
  }


  deleteExercice(exercice: IExercice): void {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer?',
      header: 'Confirmation de suppresion',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        exercice.deleted = true;
        this.subscribeToSaveResponse(this.exerciceService.update(exercice));
        this.messageService.add({ severity: 'error', summary: 'Confimation', detail: 'Exercice supprimé avec succès!' });
        this.loadAll();
      },
    });
  }

  deleteSelectedExercices(): void {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer les produits selectionnés?',
      header: 'Confirmation de supression',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.exerciceService.deleteAll(this.selectedExercices).subscribe((res: HttpResponse<IExercice[]>) => {
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

  onCountryChange($event: any): void {
    alert($event);
    window.console.log($event);
  }


  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExercice>>): void {
    result.pipe().subscribe(
      // () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
    this.loadAll();
  }
}