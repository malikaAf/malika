import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EntreeService } from '../service/entree.service';

import { EntreeComponent } from './entree.component';

describe('Component Tests', () => {
  describe('Entree Management Component', () => {
    let comp: EntreeComponent;
    let fixture: ComponentFixture<EntreeComponent>;
    let service: EntreeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EntreeComponent],
      })
        .overrideTemplate(EntreeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EntreeComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(EntreeService);

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
      expect(comp.entrees?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
