import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAcheteur, getAcheteurIdentifier } from '../acheteur.model';

export type EntityResponseType = HttpResponse<IAcheteur>;
export type EntityArrayResponseType = HttpResponse<IAcheteur[]>;

@Injectable({ providedIn: 'root' })
export class AcheteurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/acheteurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(acheteur: IAcheteur): Observable<EntityResponseType> {
    return this.http.post<IAcheteur>(this.resourceUrl, acheteur, { observe: 'response' });
  }

  update(acheteur: IAcheteur): Observable<EntityResponseType> {
    return this.http.put<IAcheteur>(`${this.resourceUrl}/${getAcheteurIdentifier(acheteur) as number}`, acheteur, { observe: 'response' });
  }

  partialUpdate(acheteur: IAcheteur): Observable<EntityResponseType> {
    return this.http.patch<IAcheteur>(`${this.resourceUrl}/${getAcheteurIdentifier(acheteur) as number}`, acheteur, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAcheteur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAcheteur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  deleteAll(acheteurs: IAcheteur[]): Observable<EntityArrayResponseType> {
    return this.http.put<IAcheteur[]>(this.resourceUrl + '/delleteAll', acheteurs, { observe: 'response' });
  }

  addAcheteurToCollectionIfMissing(acheteurCollection: IAcheteur[], ...acheteursToCheck: (IAcheteur | null | undefined)[]): IAcheteur[] {
    const acheteurs: IAcheteur[] = acheteursToCheck.filter(isPresent);
    if (acheteurs.length > 0) {
      const acheteurCollectionIdentifiers = acheteurCollection.map(acheteurItem => getAcheteurIdentifier(acheteurItem)!);
      const acheteursToAdd = acheteurs.filter(acheteurItem => {
        const acheteurIdentifier = getAcheteurIdentifier(acheteurItem);
        if (acheteurIdentifier == null || acheteurCollectionIdentifiers.includes(acheteurIdentifier)) {
          return false;
        }
        acheteurCollectionIdentifiers.push(acheteurIdentifier);
        return true;
      });
      return [...acheteursToAdd, ...acheteurCollection];
    }
    return acheteurCollection;
  }
}
