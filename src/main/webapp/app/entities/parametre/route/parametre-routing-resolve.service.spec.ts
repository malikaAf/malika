jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IParametre, Parametre } from '../parametre.model';
import { ParametreService } from '../service/parametre.service';

import { ParametreRoutingResolveService } from './parametre-routing-resolve.service';

describe('Service Tests', () => {
  describe('Parametre routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ParametreRoutingResolveService;
    let service: ParametreService;
    let resultParametre: IParametre | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ParametreRoutingResolveService);
      service = TestBed.inject(ParametreService);
      resultParametre = undefined;
    });

    describe('resolve', () => {
      it('should return IParametre returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultParametre = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultParametre).toEqual({ id: 123 });
      });

      it('should return new IParametre if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultParametre = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultParametre).toEqual(new Parametre());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Parametre })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultParametre = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultParametre).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
