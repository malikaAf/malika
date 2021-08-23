jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AcheteurService } from '../service/acheteur.service';

import { AcheteurDeleteDialogComponent } from './acheteur-delete-dialog.component';

describe('Component Tests', () => {
  describe('Acheteur Management Delete Component', () => {
    let comp: AcheteurDeleteDialogComponent;
    let fixture: ComponentFixture<AcheteurDeleteDialogComponent>;
    let service: AcheteurService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AcheteurDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(AcheteurDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AcheteurDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AcheteurService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
