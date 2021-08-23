import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEntree, Entree } from '../entree.model';

import { EntreeService } from './entree.service';

describe('Service Tests', () => {
  describe('Entree Service', () => {
    let service: EntreeService;
    let httpMock: HttpTestingController;
    let elemDefault: IEntree;
    let expectedResult: IEntree | IEntree[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EntreeService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        libelle: 'AAAAAAA',
        quantite: 0,
        prixUnitaireTTC: 0,
        serie: 'AAAAAAA',
        model: 'AAAAAAA',
        caractSupplementaire: 'AAAAAAA',
        dateEntree: currentDate,
        bordereauContentType: 'image/png',
        bordereau: 'AAAAAAA',
        observation: 'AAAAAAA',
        enStock: false,
        enCommande: false,
        deleted: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateEntree: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Entree', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateEntree: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateEntree: currentDate,
          },
          returnedFromService
        );

        service.create(new Entree()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Entree', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            libelle: 'BBBBBB',
            quantite: 1,
            prixUnitaireTTC: 1,
            serie: 'BBBBBB',
            model: 'BBBBBB',
            caractSupplementaire: 'BBBBBB',
            dateEntree: currentDate.format(DATE_FORMAT),
            bordereau: 'BBBBBB',
            observation: 'BBBBBB',
            enStock: true,
            enCommande: true,
            deleted: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateEntree: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Entree', () => {
        const patchObject = Object.assign(
          {
            quantite: 1,
            prixUnitaireTTC: 1,
            serie: 'BBBBBB',
            model: 'BBBBBB',
            dateEntree: currentDate.format(DATE_FORMAT),
            observation: 'BBBBBB',
            enCommande: true,
            deleted: true,
          },
          new Entree()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateEntree: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Entree', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            libelle: 'BBBBBB',
            quantite: 1,
            prixUnitaireTTC: 1,
            serie: 'BBBBBB',
            model: 'BBBBBB',
            caractSupplementaire: 'BBBBBB',
            dateEntree: currentDate.format(DATE_FORMAT),
            bordereau: 'BBBBBB',
            observation: 'BBBBBB',
            enStock: true,
            enCommande: true,
            deleted: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateEntree: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Entree', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEntreeToCollectionIfMissing', () => {
        it('should add a Entree to an empty array', () => {
          const entree: IEntree = { id: 123 };
          expectedResult = service.addEntreeToCollectionIfMissing([], entree);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(entree);
        });

        it('should not add a Entree to an array that contains it', () => {
          const entree: IEntree = { id: 123 };
          const entreeCollection: IEntree[] = [
            {
              ...entree,
            },
            { id: 456 },
          ];
          expectedResult = service.addEntreeToCollectionIfMissing(entreeCollection, entree);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Entree to an array that doesn't contain it", () => {
          const entree: IEntree = { id: 123 };
          const entreeCollection: IEntree[] = [{ id: 456 }];
          expectedResult = service.addEntreeToCollectionIfMissing(entreeCollection, entree);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(entree);
        });

        it('should add only unique Entree to an array', () => {
          const entreeArray: IEntree[] = [{ id: 123 }, { id: 456 }, { id: 31632 }];
          const entreeCollection: IEntree[] = [{ id: 123 }];
          expectedResult = service.addEntreeToCollectionIfMissing(entreeCollection, ...entreeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const entree: IEntree = { id: 123 };
          const entree2: IEntree = { id: 456 };
          expectedResult = service.addEntreeToCollectionIfMissing([], entree, entree2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(entree);
          expect(expectedResult).toContain(entree2);
        });

        it('should accept null and undefined values', () => {
          const entree: IEntree = { id: 123 };
          expectedResult = service.addEntreeToCollectionIfMissing([], null, entree, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(entree);
        });

        it('should return initial array if no Entree is added', () => {
          const entreeCollection: IEntree[] = [{ id: 123 }];
          expectedResult = service.addEntreeToCollectionIfMissing(entreeCollection, undefined, null);
          expect(expectedResult).toEqual(entreeCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
