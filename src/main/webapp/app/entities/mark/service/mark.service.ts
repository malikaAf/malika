import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMark, getMarkIdentifier } from '../mark.model';

export type EntityResponseType = HttpResponse<IMark>;
export type EntityArrayResponseType = HttpResponse<IMark[]>;

@Injectable({ providedIn: 'root' })
export class MarkService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/marks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mark: IMark): Observable<EntityResponseType> {
    return this.http.post<IMark>(this.resourceUrl, mark, { observe: 'response' });
  }

  update(mark: IMark): Observable<EntityResponseType> {
    return this.http.put<IMark>(`${this.resourceUrl}/${getMarkIdentifier(mark) as number}`, mark, { observe: 'response' });
  }

  partialUpdate(mark: IMark): Observable<EntityResponseType> {
    return this.http.patch<IMark>(`${this.resourceUrl}/${getMarkIdentifier(mark) as number}`, mark, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMark>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMark[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  deleteAll(marks: IMark[]): Observable<EntityArrayResponseType> {
    return this.http.put<IMark[]>(this.resourceUrl + '/delleteAll', marks, { observe: 'response' });
  }

  addMarkToCollectionIfMissing(markCollection: IMark[], ...marksToCheck: (IMark | null | undefined)[]): IMark[] {
    const marks: IMark[] = marksToCheck.filter(isPresent);
    if (marks.length > 0) {
      const markCollectionIdentifiers = markCollection.map(markItem => getMarkIdentifier(markItem)!);
      const marksToAdd = marks.filter(markItem => {
        const markIdentifier = getMarkIdentifier(markItem);
        if (markIdentifier == null || markCollectionIdentifiers.includes(markIdentifier)) {
          return false;
        }
        markCollectionIdentifiers.push(markIdentifier);
        return true;
      });
      return [...marksToAdd, ...markCollection];
    }
    return markCollection;
  }
}
