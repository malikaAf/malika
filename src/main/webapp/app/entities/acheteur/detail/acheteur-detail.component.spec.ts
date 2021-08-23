import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AcheteurDetailComponent } from './acheteur-detail.component';

describe('Component Tests', () => {
  describe('Acheteur Management Detail Component', () => {
    let comp: AcheteurDetailComponent;
    let fixture: ComponentFixture<AcheteurDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AcheteurDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ acheteur: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AcheteurDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AcheteurDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load acheteur on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.acheteur).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
