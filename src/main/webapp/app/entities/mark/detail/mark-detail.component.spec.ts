import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MarkDetailComponent } from './mark-detail.component';

describe('Component Tests', () => {
  describe('Mark Management Detail Component', () => {
    let comp: MarkDetailComponent;
    let fixture: ComponentFixture<MarkDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MarkDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ mark: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MarkDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MarkDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mark on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mark).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
