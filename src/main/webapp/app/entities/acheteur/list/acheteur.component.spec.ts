import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AcheteurService } from '../service/acheteur.service';

import { AcheteurComponent } from './acheteur.component';

describe('Component Tests', () => {
  describe('Acheteur Management Component', () => {
    let comp: AcheteurComponent;
    let fixture: ComponentFixture<AcheteurComponent>;
    let service: AcheteurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AcheteurComponent],
      })
        .overrideTemplate(AcheteurComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AcheteurComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AcheteurService);

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
      expect(comp.acheteurs?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
