import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAcheteur, Acheteur } from '../acheteur.model';
import { AcheteurService } from '../service/acheteur.service';

@Injectable({ providedIn: 'root' })
export class AcheteurRoutingResolveService implements Resolve<IAcheteur> {
  constructor(protected service: AcheteurService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAcheteur> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((acheteur: HttpResponse<Acheteur>) => {
          if (acheteur.body) {
            return of(acheteur.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Acheteur());
  }
}
