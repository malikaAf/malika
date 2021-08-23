import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMark, Mark } from '../mark.model';

import { MarkService } from './mark.service';

describe('Service Tests', () => {
  describe('Mark Service', () => {
    let service: MarkService;
    let httpMock: HttpTestingController;
    let elemDefault: IMark;
    let expectedResult: IMark | IMark[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MarkService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        libelle: 'AAAAAAA',
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

      it('should create a Mark', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Mark()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Mark', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            libelle: 'BBBBBB',
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

      it('should partial update a Mark', () => {
        const patchObject = Object.assign(
          {
            libelle: 'BBBBBB',
            deleted: true,
          },
          new Mark()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Mark', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            libelle: 'BBBBBB',
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

      it('should delete a Mark', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMarkToCollectionIfMissing', () => {
        it('should add a Mark to an empty array', () => {
          const mark: IMark = { id: 123 };
          expectedResult = service.addMarkToCollectionIfMissing([], mark);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mark);
        });

        it('should not add a Mark to an array that contains it', () => {
          const mark: IMark = { id: 123 };
          const markCollection: IMark[] = [
            {
              ...mark,
            },
            { id: 456 },
          ];
          expectedResult = service.addMarkToCollectionIfMissing(markCollection, mark);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Mark to an array that doesn't contain it", () => {
          const mark: IMark = { id: 123 };
          const markCollection: IMark[] = [{ id: 456 }];
          expectedResult = service.addMarkToCollectionIfMissing(markCollection, mark);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mark);
        });

        it('should add only unique Mark to an array', () => {
          const markArray: IMark[] = [{ id: 123 }, { id: 456 }, { id: 19500 }];
          const markCollection: IMark[] = [{ id: 123 }];
          expectedResult = service.addMarkToCollectionIfMissing(markCollection, ...markArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const mark: IMark = { id: 123 };
          const mark2: IMark = { id: 456 };
          expectedResult = service.addMarkToCollectionIfMissing([], mark, mark2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mark);
          expect(expectedResult).toContain(mark2);
        });

        it('should accept null and undefined values', () => {
          const mark: IMark = { id: 123 };
          expectedResult = service.addMarkToCollectionIfMissing([], null, mark, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mark);
        });

        it('should return initial array if no Mark is added', () => {
          const markCollection: IMark[] = [{ id: 123 }];
          expectedResult = service.addMarkToCollectionIfMissing(markCollection, undefined, null);
          expect(expectedResult).toEqual(markCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
