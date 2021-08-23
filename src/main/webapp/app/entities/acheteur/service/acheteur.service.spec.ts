import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TypeAcheteur } from 'app/entities/enumerations/type-acheteur.model';
import { IAcheteur, Acheteur } from '../acheteur.model';

import { AcheteurService } from './acheteur.service';

describe('Service Tests', () => {
  describe('Acheteur Service', () => {
    let service: AcheteurService;
    let httpMock: HttpTestingController;
    let elemDefault: IAcheteur;
    let expectedResult: IAcheteur | IAcheteur[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AcheteurService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        typeClient: TypeAcheteur.ACHETEUR,
        nom: 'AAAAAAA',
        prenom: 'AAAAAAA',
        tel: 'AAAAAAA',
        cnib: 'AAAAAAA',
        email: 'AAAAAAA',
        adresse: 'AAAAAAA',
        numroBanquaire: 'AAAAAAA',
        deleted: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Acheteur', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Acheteur()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Acheteur', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            typeClient: 'BBBBBB',
            nom: 'BBBBBB',
            prenom: 'BBBBBB',
            tel: 'BBBBBB',
            cnib: 'BBBBBB',
            email: 'BBBBBB',
            adresse: 'BBBBBB',
            numroBanquaire: 'BBBBBB',
            deleted: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Acheteur', () => {
        const patchObject = Object.assign(
          {
            typeClient: 'BBBBBB',
            prenom: 'BBBBBB',
            email: 'BBBBBB',
            deleted: true,
          },
          new Acheteur()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Acheteur', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            typeClient: 'BBBBBB',
            nom: 'BBBBBB',
            prenom: 'BBBBBB',
            tel: 'BBBBBB',
            cnib: 'BBBBBB',
            email: 'BBBBBB',
            adresse: 'BBBBBB',
            numroBanquaire: 'BBBBBB',
            deleted: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Acheteur', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAcheteurToCollectionIfMissing', () => {
        it('should add a Acheteur to an empty array', () => {
          const acheteur: IAcheteur = { id: 123 };
          expectedResult = service.addAcheteurToCollectionIfMissing([], acheteur);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(acheteur);
        });

        it('should not add a Acheteur to an array that contains it', () => {
          const acheteur: IAcheteur = { id: 123 };
          const acheteurCollection: IAcheteur[] = [
            {
              ...acheteur,
            },
            { id: 456 },
          ];
          expectedResult = service.addAcheteurToCollectionIfMissing(acheteurCollection, acheteur);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Acheteur to an array that doesn't contain it", () => {
          const acheteur: IAcheteur = { id: 123 };
          const acheteurCollection: IAcheteur[] = [{ id: 456 }];
          expectedResult = service.addAcheteurToCollectionIfMissing(acheteurCollection, acheteur);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(acheteur);
        });

        it('should add only unique Acheteur to an array', () => {
          const acheteurArray: IAcheteur[] = [{ id: 123 }, { id: 456 }, { id: 56579 }];
          const acheteurCollection: IAcheteur[] = [{ id: 123 }];
          expectedResult = service.addAcheteurToCollectionIfMissing(acheteurCollection, ...acheteurArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const acheteur: IAcheteur = { id: 123 };
          const acheteur2: IAcheteur = { id: 456 };
          expectedResult = service.addAcheteurToCollectionIfMissing([], acheteur, acheteur2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(acheteur);
          expect(expectedResult).toContain(acheteur2);
        });

        it('should accept null and undefined values', () => {
          const acheteur: IAcheteur = { id: 123 };
          expectedResult = service.addAcheteurToCollectionIfMissing([], null, acheteur, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(acheteur);
        });

        it('should return initial array if no Acheteur is added', () => {
          const acheteurCollection: IAcheteur[] = [{ id: 123 }];
          expectedResult = service.addAcheteurToCollectionIfMissing(acheteurCollection, undefined, null);
          expect(expectedResult).toEqual(acheteurCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
