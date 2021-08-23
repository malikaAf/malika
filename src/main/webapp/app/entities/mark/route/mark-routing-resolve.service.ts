import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMark, Mark } from '../mark.model';
import { MarkService } from '../service/mark.service';

@Injectable({ providedIn: 'root' })
export class MarkRoutingResolveService implements Resolve<IMark> {
  constructor(protected service: MarkService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMark> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mark: HttpResponse<Mark>) => {
          if (mark.body) {
            return of(mark.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Mark());
  }
}
