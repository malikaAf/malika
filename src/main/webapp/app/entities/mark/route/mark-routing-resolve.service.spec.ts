jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMark, Mark } from '../mark.model';
import { MarkService } from '../service/mark.service';

import { MarkRoutingResolveService } from './mark-routing-resolve.service';

describe('Service Tests', () => {
  describe('Mark routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MarkRoutingResolveService;
    let service: MarkService;
    let resultMark: IMark | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MarkRoutingResolveService);
      service = TestBed.inject(MarkService);
      resultMark = undefined;
    });

    describe('resolve', () => {
      it('should return IMark returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMark = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMark).toEqual({ id: 123 });
      });

      it('should return new IMark if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMark = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMark).toEqual(new Mark());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Mark })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMark = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMark).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
