jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ArticleService } from '../service/article.service';
import { IArticle, Article } from '../article.model';
import { IMark } from 'app/entities/mark/mark.model';
import { MarkService } from 'app/entities/mark/service/mark.service';

import { ArticleUpdateComponent } from './article-update.component';

describe('Component Tests', () => {
  describe('Article Management Update Component', () => {
    let comp: ArticleUpdateComponent;
    let fixture: ComponentFixture<ArticleUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let articleService: ArticleService;
    let markService: MarkService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ArticleUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ArticleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ArticleUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      articleService = TestBed.inject(ArticleService);
      markService = TestBed.inject(MarkService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Mark query and add missing value', () => {
        const article: IArticle = { id: 456 };
        const mark: IMark = { id: 21910 };
        article.mark = mark;

        const markCollection: IMark[] = [{ id: 22438 }];
        jest.spyOn(markService, 'query').mockReturnValue(of(new HttpResponse({ body: markCollection })));
        const additionalMarks = [mark];
        const expectedCollection: IMark[] = [...additionalMarks, ...markCollection];
        jest.spyOn(markService, 'addMarkToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ article });
        comp.ngOnInit();

        expect(markService.query).toHaveBeenCalled();
        expect(markService.addMarkToCollectionIfMissing).toHaveBeenCalledWith(markCollection, ...additionalMarks);
        expect(comp.marksSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const article: IArticle = { id: 456 };
        const mark: IMark = { id: 2639 };
        article.mark = mark;

        activatedRoute.data = of({ article });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(article));
        expect(comp.marksSharedCollection).toContain(mark);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Article>>();
        const article = { id: 123 };
        jest.spyOn(articleService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ article });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: article }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(articleService.update).toHaveBeenCalledWith(article);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Article>>();
        const article = new Article();
        jest.spyOn(articleService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ article });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: article }));
        saveSubject.complete();

        // THEN
        expect(articleService.create).toHaveBeenCalledWith(article);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Article>>();
        const article = { id: 123 };
        jest.spyOn(articleService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ article });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(articleService.update).toHaveBeenCalledWith(article);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackMarkById', () => {
        it('Should return tracked Mark primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMarkById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
