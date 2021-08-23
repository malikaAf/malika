import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ExerciceService } from '../service/exercice.service';

import { ExerciceComponent } from './exercice.component';

describe('Component Tests', () => {
  describe('Exercice Management Component', () => {
    let comp: ExerciceComponent;
    let fixture: ComponentFixture<ExerciceComponent>;
    let service: ExerciceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExerciceComponent],
      })
        .overrideTemplate(ExerciceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExerciceComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ExerciceService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.exercices?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
