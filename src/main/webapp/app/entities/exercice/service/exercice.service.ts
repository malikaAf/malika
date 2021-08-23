import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExercice, getExerciceIdentifier } from '../exercice.model';

export type EntityResponseType = HttpResponse<IExercice>;
export type EntityArrayResponseType = HttpResponse<IExercice[]>;

@Injectable({ providedIn: 'root' })
export class ExerciceService {
  
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/exercices');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(exercice: IExercice): Observable<EntityResponseType> {
    return this.http.post<IExercice>(this.resourceUrl, exercice, { observe: 'response' });
  }

  update(exercice: IExercice): Observable<EntityResponseType> {
    return this.http.put<IExercice>(`${this.resourceUrl}/${getExerciceIdentifier(exercice) as number}`, exercice, { observe: 'response' });
  }

  partialUpdate(exercice: IExercice): Observable<EntityResponseType> {
    return this.http.patch<IExercice>(`${this.resourceUrl}/${getExerciceIdentifier(exercice) as number}`, exercice, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExercice>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExercice[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  deleteAll(exercices: IExercice[]): Observable<EntityArrayResponseType> {
    return this.http.put<IExercice[]>(this.resourceUrl + '/delleteAll', exercices, { observe: 'response' });
  }


  addExerciceToCollectionIfMissing(exerciceCollection: IExercice[], ...exercicesToCheck: (IExercice | null | undefined)[]): IExercice[] {
    const exercices: IExercice[] = exercicesToCheck.filter(isPresent);
    if (exercices.length > 0) {
      const exerciceCollectionIdentifiers = exerciceCollection.map(exerciceItem => getExerciceIdentifier(exerciceItem)!);
      const exercicesToAdd = exercices.filter(exerciceItem => {
        const exerciceIdentifier = getExerciceIdentifier(exerciceItem);
        if (exerciceIdentifier == null || exerciceCollectionIdentifiers.includes(exerciceIdentifier)) {
          return false;
        }
        exerciceCollectionIdentifiers.push(exerciceIdentifier);
        return true;
      });
      return [...exercicesToAdd, ...exerciceCollection];
    }
    return exerciceCollection;
  }
}
