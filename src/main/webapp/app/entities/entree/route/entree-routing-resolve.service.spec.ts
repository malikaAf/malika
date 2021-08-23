jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEntree, Entree } from '../entree.model';
import { EntreeService } from '../service/entree.service';

import { EntreeRoutingResolveService } from './entree-routing-resolve.service';

describe('Service Tests', () => {
  describe('Entree routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EntreeRoutingResolveService;
    let service: EntreeService;
    let resultEntree: IEntree | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EntreeRoutingResolveService);
      service = TestBed.inject(EntreeService);
      resultEntree = undefined;
    });

    describe('resolve', () => {
      it('should return IEntree returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEntree = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEntree).toEqual({ id: 123 });
      });

      it('should return new IEntree if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEntree = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEntree).toEqual(new Entree());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Entree })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEntree = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEntree).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
