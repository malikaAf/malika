import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEntree, Entree } from '../entree.model';
import { EntreeService } from '../service/entree.service';

@Injectable({ providedIn: 'root' })
export class EntreeRoutingResolveService implements Resolve<IEntree> {
  constructor(protected service: EntreeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEntree> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((entree: HttpResponse<Entree>) => {
          if (entree.body) {
            return of(entree.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Entree());
  }
}
