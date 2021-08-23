jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISortie, Sortie } from '../sortie.model';
import { SortieService } from '../service/sortie.service';

import { SortieRoutingResolveService } from './sortie-routing-resolve.service';

describe('Service Tests', () => {
  describe('Sortie routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SortieRoutingResolveService;
    let service: SortieService;
    let resultSortie: ISortie | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SortieRoutingResolveService);
      service = TestBed.inject(SortieService);
      resultSortie = undefined;
    });

    describe('resolve', () => {
      it('should return ISortie returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSortie = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSortie).toEqual({ id: 123 });
      });

      it('should return new ISortie if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSortie = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSortie).toEqual(new Sortie());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Sortie })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSortie = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSortie).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
