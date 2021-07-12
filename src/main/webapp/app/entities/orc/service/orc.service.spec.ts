import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrc, Orc } from '../orc.model';

import { OrcService } from './orc.service';

describe('Service Tests', () => {
  describe('Orc Service', () => {
    let service: OrcService;
    let httpMock: HttpTestingController;
    let elemDefault: IOrc;
    let expectedResult: IOrc | IOrc[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(OrcService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        numero: 'AAAAAAA',
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

      it('should create a Orc', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Orc()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Orc', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            numero: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Orc', () => {
        const patchObject = Object.assign(
          {
            numero: 'BBBBBB',
          },
          new Orc()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Orc', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            numero: 'BBBBBB',
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

      it('should delete a Orc', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addOrcToCollectionIfMissing', () => {
        it('should add a Orc to an empty array', () => {
          const orc: IOrc = { id: 123 };
          expectedResult = service.addOrcToCollectionIfMissing([], orc);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(orc);
        });

        it('should not add a Orc to an array that contains it', () => {
          const orc: IOrc = { id: 123 };
          const orcCollection: IOrc[] = [
            {
              ...orc,
            },
            { id: 456 },
          ];
          expectedResult = service.addOrcToCollectionIfMissing(orcCollection, orc);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Orc to an array that doesn't contain it", () => {
          const orc: IOrc = { id: 123 };
          const orcCollection: IOrc[] = [{ id: 456 }];
          expectedResult = service.addOrcToCollectionIfMissing(orcCollection, orc);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(orc);
        });

        it('should add only unique Orc to an array', () => {
          const orcArray: IOrc[] = [{ id: 123 }, { id: 456 }, { id: 98258 }];
          const orcCollection: IOrc[] = [{ id: 123 }];
          expectedResult = service.addOrcToCollectionIfMissing(orcCollection, ...orcArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const orc: IOrc = { id: 123 };
          const orc2: IOrc = { id: 456 };
          expectedResult = service.addOrcToCollectionIfMissing([], orc, orc2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(orc);
          expect(expectedResult).toContain(orc2);
        });

        it('should accept null and undefined values', () => {
          const orc: IOrc = { id: 123 };
          expectedResult = service.addOrcToCollectionIfMissing([], null, orc, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(orc);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
