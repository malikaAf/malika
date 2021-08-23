import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEntree, getEntreeIdentifier } from '../entree.model';

export type EntityResponseType = HttpResponse<IEntree>;
export type EntityArrayResponseType = HttpResponse<IEntree[]>;

@Injectable({ providedIn: 'root' })
export class EntreeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/entrees');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(entree: IEntree): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entree);
    return this.http
      .post<IEntree>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(entree: IEntree): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entree);
    return this.http
      .put<IEntree>(`${this.resourceUrl}/${getEntreeIdentifier(entree) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update2(entree: IEntree): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient2(entree);
    return this.http
      .put<IEntree>(`${this.resourceUrl}/${getEntreeIdentifier(entree) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(entree: IEntree): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entree);
    return this.http
      .patch<IEntree>(`${this.resourceUrl}/${getEntreeIdentifier(entree) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEntree>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEntree[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  deleteAll(entrees: IEntree[]): Observable<EntityArrayResponseType> {
    return this.http.put<IEntree[]>(this.resourceUrl + '/delleteAll', entrees, { observe: 'response' });
  }

  getAllDeleted(): Observable<IEntree[]> {
    return this.http.get<IEntree[]>(this.resourceUrl + '/allDeletedList');
  }

  addEntreeToCollectionIfMissing(entreeCollection: IEntree[], ...entreesToCheck: (IEntree | null | undefined)[]): IEntree[] {
    const entrees: IEntree[] = entreesToCheck.filter(isPresent);
    if (entrees.length > 0) {
      const entreeCollectionIdentifiers = entreeCollection.map(entreeItem => getEntreeIdentifier(entreeItem)!);
      const entreesToAdd = entrees.filter(entreeItem => {
        const entreeIdentifier = getEntreeIdentifier(entreeItem);
        if (entreeIdentifier == null || entreeCollectionIdentifiers.includes(entreeIdentifier)) {
          return false;
        }
        entreeCollectionIdentifiers.push(entreeIdentifier);
        return true;
      });
      return [...entreesToAdd, ...entreeCollection];
    }
    return entreeCollection;
  }

  protected convertDateFromClient(entree: IEntree): IEntree {
    return Object.assign({}, entree, {
      dateEntree: entree.dateEntree?.isValid() ? entree.dateEntree.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateEntree = res.body.dateEntree ? dayjs(res.body.dateEntree) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((entree: IEntree) => {
        entree.dateEntree = entree.dateEntree ? dayjs(entree.dateEntree) : undefined;
      });
    }
    return res;
  }

  protected convertDateFromClient2(entree: IEntree): IEntree {
    return Object.assign({}, entree, {
      dateEntree: entree.dateEntree?.toDate ? entree.dateEntree.format(DATE_FORMAT) : undefined,
    });
  }
}
