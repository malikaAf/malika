jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AcheteurService } from '../service/acheteur.service';
import { IAcheteur, Acheteur } from '../acheteur.model';

import { AcheteurUpdateComponent } from './acheteur-update.component';

describe('Component Tests', () => {
  describe('Acheteur Management Update Component', () => {
    let comp: AcheteurUpdateComponent;
    let fixture: ComponentFixture<AcheteurUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let acheteurService: AcheteurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AcheteurUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AcheteurUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AcheteurUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      acheteurService = TestBed.inject(AcheteurService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const acheteur: IAcheteur = { id: 456 };

        activatedRoute.data = of({ acheteur });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(acheteur));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Acheteur>>();
        const acheteur = { id: 123 };
        jest.spyOn(acheteurService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ acheteur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: acheteur }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(acheteurService.update).toHaveBeenCalledWith(acheteur);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Acheteur>>();
        const acheteur = new Acheteur();
        jest.spyOn(acheteurService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ acheteur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: acheteur }));
        saveSubject.complete();

        // THEN
        expect(acheteurService.create).toHaveBeenCalledWith(acheteur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Acheteur>>();
        const acheteur = { id: 123 };
        jest.spyOn(acheteurService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ acheteur });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(acheteurService.update).toHaveBeenCalledWith(acheteur);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
