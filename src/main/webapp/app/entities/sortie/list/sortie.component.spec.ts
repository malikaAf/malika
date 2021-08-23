import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SortieService } from '../service/sortie.service';

import { SortieComponent } from './sortie.component';

describe('Component Tests', () => {
  describe('Sortie Management Component', () => {
    let comp: SortieComponent;
    let fixture: ComponentFixture<SortieComponent>;
    let service: SortieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SortieComponent],
      })
        .overrideTemplate(SortieComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SortieComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SortieService);

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
      expect(comp.sorties?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
