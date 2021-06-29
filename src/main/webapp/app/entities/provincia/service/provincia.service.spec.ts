import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProvincia, Provincia } from '../provincia.model';

import { ProvinciaService } from './provincia.service';

describe('Service Tests', () => {
  describe('Provincia Service', () => {
    let service: ProvinciaService;
    let httpMock: HttpTestingController;
    let elemDefault: IProvincia;
    let expectedResult: IProvincia | IProvincia[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ProvinciaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nombre: 'AAAAAAA',
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

      it('should create a Provincia', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Provincia()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Provincia', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Provincia', () => {
        const patchObject = Object.assign({}, new Provincia());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Provincia', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
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

      it('should delete a Provincia', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addProvinciaToCollectionIfMissing', () => {
        it('should add a Provincia to an empty array', () => {
          const provincia: IProvincia = { id: 123 };
          expectedResult = service.addProvinciaToCollectionIfMissing([], provincia);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(provincia);
        });

        it('should not add a Provincia to an array that contains it', () => {
          const provincia: IProvincia = { id: 123 };
          const provinciaCollection: IProvincia[] = [
            {
              ...provincia,
            },
            { id: 456 },
          ];
          expectedResult = service.addProvinciaToCollectionIfMissing(provinciaCollection, provincia);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Provincia to an array that doesn't contain it", () => {
          const provincia: IProvincia = { id: 123 };
          const provinciaCollection: IProvincia[] = [{ id: 456 }];
          expectedResult = service.addProvinciaToCollectionIfMissing(provinciaCollection, provincia);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(provincia);
        });

        it('should add only unique Provincia to an array', () => {
          const provinciaArray: IProvincia[] = [{ id: 123 }, { id: 456 }, { id: 39522 }];
          const provinciaCollection: IProvincia[] = [{ id: 123 }];
          expectedResult = service.addProvinciaToCollectionIfMissing(provinciaCollection, ...provinciaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const provincia: IProvincia = { id: 123 };
          const provincia2: IProvincia = { id: 456 };
          expectedResult = service.addProvinciaToCollectionIfMissing([], provincia, provincia2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(provincia);
          expect(expectedResult).toContain(provincia2);
        });

        it('should accept null and undefined values', () => {
          const provincia: IProvincia = { id: 123 };
          expectedResult = service.addProvinciaToCollectionIfMissing([], null, provincia, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(provincia);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
