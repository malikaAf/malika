import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MarkService } from '../service/mark.service';

import { MarkComponent } from './mark.component';

describe('Component Tests', () => {
  describe('Mark Management Component', () => {
    let comp: MarkComponent;
    let fixture: ComponentFixture<MarkComponent>;
    let service: MarkService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MarkComponent],
      })
        .overrideTemplate(MarkComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MarkComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(MarkService);

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
      expect(comp.marks?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
