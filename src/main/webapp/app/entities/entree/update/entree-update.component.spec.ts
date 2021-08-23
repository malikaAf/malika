jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EntreeService } from '../service/entree.service';
import { IEntree, Entree } from '../entree.model';
import { IArticle } from 'app/entities/article/article.model';
import { ArticleService } from 'app/entities/article/service/article.service';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';
import { IExercice } from 'app/entities/exercice/exercice.model';
import { ExerciceService } from 'app/entities/exercice/service/exercice.service';
import { IParametre } from 'app/entities/parametre/parametre.model';
import { ParametreService } from 'app/entities/parametre/service/parametre.service';

import { EntreeUpdateComponent } from './entree-update.component';

describe('Component Tests', () => {
  describe('Entree Management Update Component', () => {
    let comp: EntreeUpdateComponent;
    let fixture: ComponentFixture<EntreeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let entreeService: EntreeService;
    let articleService: ArticleService;
    let fournisseurService: FournisseurService;
    let exerciceService: ExerciceService;
    let parametreService: ParametreService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EntreeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EntreeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EntreeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      entreeService = TestBed.inject(EntreeService);
      articleService = TestBed.inject(ArticleService);
      fournisseurService = TestBed.inject(FournisseurService);
      exerciceService = TestBed.inject(ExerciceService);
      parametreService = TestBed.inject(ParametreService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Article query and add missing value', () => {
        const entree: IEntree = { id: 456 };
        const article: IArticle = { id: 77042 };
        entree.article = article;

        const articleCollection: IArticle[] = [{ id: 39691 }];
        jest.spyOn(articleService, 'query').mockReturnValue(of(new HttpResponse({ body: articleCollection })));
        const additionalArticles = [article];
        const expectedCollection: IArticle[] = [...additionalArticles, ...articleCollection];
        jest.spyOn(articleService, 'addArticleToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ entree });
        comp.ngOnInit();

        expect(articleService.query).toHaveBeenCalled();
        expect(articleService.addArticleToCollectionIfMissing).toHaveBeenCalledWith(articleCollection, ...additionalArticles);
        expect(comp.articlesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Fournisseur query and add missing value', () => {
        const entree: IEntree = { id: 456 };
        const fournisseur: IFournisseur = { id: 14307 };
        entree.fournisseur = fournisseur;

        const fournisseurCollection: IFournisseur[] = [{ id: 10693 }];
        jest.spyOn(fournisseurService, 'query').mockReturnValue(of(new HttpResponse({ body: fournisseurCollection })));
        const additionalFournisseurs = [fournisseur];
        const expectedCollection: IFournisseur[] = [...additionalFournisseurs, ...fournisseurCollection];
        jest.spyOn(fournisseurService, 'addFournisseurToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ entree });
        comp.ngOnInit();

        expect(fournisseurService.query).toHaveBeenCalled();
        expect(fournisseurService.addFournisseurToCollectionIfMissing).toHaveBeenCalledWith(
          fournisseurCollection,
          ...additionalFournisseurs
        );
        expect(comp.fournisseursSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Exercice query and add missing value', () => {
        const entree: IEntree = { id: 456 };
        const exercice: IExercice = { id: 1741 };
        entree.exercice = exercice;

        const exerciceCollection: IExercice[] = [{ id: 17657 }];
        jest.spyOn(exerciceService, 'query').mockReturnValue(of(new HttpResponse({ body: exerciceCollection })));
        const additionalExercices = [exercice];
        const expectedCollection: IExercice[] = [...additionalExercices, ...exerciceCollection];
        jest.spyOn(exerciceService, 'addExerciceToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ entree });
        comp.ngOnInit();

        expect(exerciceService.query).toHaveBeenCalled();
        expect(exerciceService.addExerciceToCollectionIfMissing).toHaveBeenCalledWith(exerciceCollection, ...additionalExercices);
        expect(comp.exercicesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Parametre query and add missing value', () => {
        const entree: IEntree = { id: 456 };
        const parametre: IParametre = { id: 10514 };
        entree.parametre = parametre;

        const parametreCollection: IParametre[] = [{ id: 89820 }];
        jest.spyOn(parametreService, 'query').mockReturnValue(of(new HttpResponse({ body: parametreCollection })));
        const additionalParametres = [parametre];
        const expectedCollection: IParametre[] = [...additionalParametres, ...parametreCollection];
        jest.spyOn(parametreService, 'addParametreToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ entree });
        comp.ngOnInit();

        expect(parametreService.query).toHaveBeenCalled();
        expect(parametreService.addParametreToCollectionIfMissing).toHaveBeenCalledWith(parametreCollection, ...additionalParametres);
        expect(comp.parametresSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const entree: IEntree = { id: 456 };
        const article: IArticle = { id: 55256 };
        entree.article = article;
        const fournisseur: IFournisseur = { id: 20131 };
        entree.fournisseur = fournisseur;
        const exercice: IExercice = { id: 42600 };
        entree.exercice = exercice;
        const parametre: IParametre = { id: 86476 };
        entree.parametre = parametre;

        activatedRoute.data = of({ entree });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(entree));
        expect(comp.articlesSharedCollection).toContain(article);
        expect(comp.fournisseursSharedCollection).toContain(fournisseur);
        expect(comp.exercicesSharedCollection).toContain(exercice);
        expect(comp.parametresSharedCollection).toContain(parametre);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Entree>>();
        const entree = { id: 123 };
        jest.spyOn(entreeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ entree });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: entree }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(entreeService.update).toHaveBeenCalledWith(entree);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Entree>>();
        const entree = new Entree();
        jest.spyOn(entreeService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ entree });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: entree }));
        saveSubject.complete();

        // THEN
        expect(entreeService.create).toHaveBeenCalledWith(entree);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Entree>>();
        const entree = { id: 123 };
        jest.spyOn(entreeService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ entree });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(entreeService.update).toHaveBeenCalledWith(entree);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackArticleById', () => {
        it('Should return tracked Article primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackArticleById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackFournisseurById', () => {
        it('Should return tracked Fournisseur primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFournisseurById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackExerciceById', () => {
        it('Should return tracked Exercice primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackExerciceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackParametreById', () => {
        it('Should return tracked Parametre primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackParametreById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
