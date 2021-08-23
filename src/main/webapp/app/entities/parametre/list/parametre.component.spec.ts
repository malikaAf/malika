import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ParametreService } from '../service/parametre.service';

import { ParametreComponent } from './parametre.component';

describe('Component Tests', () => {
  describe('Parametre Management Component', () => {
    let comp: ParametreComponent;
    let fixture: ComponentFixture<ParametreComponent>;
    let service: ParametreService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ParametreComponent],
      })
        .overrideTemplate(ParametreComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ParametreComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ParametreService);

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
      expect(comp.parametres?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
