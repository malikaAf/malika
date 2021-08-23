jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SortieService } from '../service/sortie.service';
import { ISortie, Sortie } from '../sortie.model';
import { IEntree } from 'app/entities/entree/entree.model';
import { EntreeService } from 'app/entities/entree/service/entree.service';
import { IAcheteur } from 'app/entities/acheteur/acheteur.model';
import { AcheteurService } from 'app/entities/acheteur/service/acheteur.service';

import { SortieUpdateComponent } from './sortie-update.component';

describe('Component Tests', () => {
  describe('Sortie Management Update Component', () => {
    let comp: SortieUpdateComponent;
    let fixture: ComponentFixture<SortieUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let sortieService: SortieService;
    let entreeService: EntreeService;
    let acheteurService: AcheteurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SortieUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SortieUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SortieUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      sortieService = TestBed.inject(SortieService);
      entreeService = TestBed.inject(EntreeService);
      acheteurService = TestBed.inject(AcheteurService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Entree query and add missing value', () => {
        const sortie: ISortie = { id: 456 };
        const entree: IEntree = { id: 25594 };
        sortie.entree = entree;

        const entreeCollection: IEntree[] = [{ id: 15574 }];
        jest.spyOn(entreeService, 'query').mockReturnValue(of(new HttpResponse({ body: entreeCollection })));
        const additionalEntrees = [entree];
        const expectedCollection: IEntree[] = [...additionalEntrees, ...entreeCollection];
        jest.spyOn(entreeService, 'addEntreeToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ sortie });
        comp.ngOnInit();

        expect(entreeService.query).toHaveBeenCalled();
        expect(entreeService.addEntreeToCollectionIfMissing).toHaveBeenCalledWith(entreeCollection, ...additionalEntrees);
        expect(comp.entreesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Acheteur query and add missing value', () => {
        const sortie: ISortie = { id: 456 };
        const acheteur: IAcheteur = { id: 64448 };
        sortie.acheteur = acheteur;

        const acheteurCollection: IAcheteur[] = [{ id: 84600 }];
        jest.spyOn(acheteurService, 'query').mockReturnValue(of(new HttpResponse({ body: acheteurCollection })));
        const additionalAcheteurs = [acheteur];
        const expectedCollection: IAcheteur[] = [...additionalAcheteurs, ...acheteurCollection];
        jest.spyOn(acheteurService, 'addAcheteurToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ sortie });
        comp.ngOnInit();

        expect(acheteurService.query).toHaveBeenCalled();
        expect(acheteurService.addAcheteurToCollectionIfMissing).toHaveBeenCalledWith(acheteurCollection, ...additionalAcheteurs);
        expect(comp.acheteursSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const sortie: ISortie = { id: 456 };
        const entree: IEntree = { id: 44934 };
        sortie.entree = entree;
        const acheteur: IAcheteur = { id: 36678 };
        sortie.acheteur = acheteur;

        activatedRoute.data = of({ sortie });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(sortie));
        expect(comp.entreesSharedCollection).toContain(entree);
        expect(comp.acheteursSharedCollection).toContain(acheteur);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Sortie>>();
        const sortie = { id: 123 };
        jest.spyOn(sortieService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ sortie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sortie }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(sortieService.update).toHaveBeenCalledWith(sortie);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Sortie>>();
        const sortie = new Sortie();
        jest.spyOn(sortieService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ sortie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sortie }));
        saveSubject.complete();

        // THEN
        expect(sortieService.create).toHaveBeenCalledWith(sortie);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Sortie>>();
        const sortie = { id: 123 };
        jest.spyOn(sortieService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ sortie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(sortieService.update).toHaveBeenCalledWith(sortie);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackEntreeById', () => {
        it('Should return tracked Entree primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEntreeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackAcheteurById', () => {
        it('Should return tracked Acheteur primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAcheteurById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
