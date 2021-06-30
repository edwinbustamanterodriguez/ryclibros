import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUbicacion, Ubicacion } from '../ubicacion.model';

import { UbicacionService } from './ubicacion.service';

describe('Service Tests', () => {
  describe('Ubicacion Service', () => {
    let service: UbicacionService;
    let httpMock: HttpTestingController;
    let elemDefault: IUbicacion;
    let expectedResult: IUbicacion | IUbicacion[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(UbicacionService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        sector: 'AAAAAAA',
        numero: 0,
        serie: 'AAAAAAA',
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

      it('should create a Ubicacion', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Ubicacion()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Ubicacion', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            sector: 'BBBBBB',
            numero: 1,
            serie: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Ubicacion', () => {
        const patchObject = Object.assign(
          {
            sector: 'BBBBBB',
          },
          new Ubicacion()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Ubicacion', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            sector: 'BBBBBB',
            numero: 1,
            serie: 'BBBBBB',
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

      it('should delete a Ubicacion', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addUbicacionToCollectionIfMissing', () => {
        it('should add a Ubicacion to an empty array', () => {
          const ubicacion: IUbicacion = { id: 123 };
          expectedResult = service.addUbicacionToCollectionIfMissing([], ubicacion);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ubicacion);
        });

        it('should not add a Ubicacion to an array that contains it', () => {
          const ubicacion: IUbicacion = { id: 123 };
          const ubicacionCollection: IUbicacion[] = [
            {
              ...ubicacion,
            },
            { id: 456 },
          ];
          expectedResult = service.addUbicacionToCollectionIfMissing(ubicacionCollection, ubicacion);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Ubicacion to an array that doesn't contain it", () => {
          const ubicacion: IUbicacion = { id: 123 };
          const ubicacionCollection: IUbicacion[] = [{ id: 456 }];
          expectedResult = service.addUbicacionToCollectionIfMissing(ubicacionCollection, ubicacion);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ubicacion);
        });

        it('should add only unique Ubicacion to an array', () => {
          const ubicacionArray: IUbicacion[] = [{ id: 123 }, { id: 456 }, { id: 84077 }];
          const ubicacionCollection: IUbicacion[] = [{ id: 123 }];
          expectedResult = service.addUbicacionToCollectionIfMissing(ubicacionCollection, ...ubicacionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ubicacion: IUbicacion = { id: 123 };
          const ubicacion2: IUbicacion = { id: 456 };
          expectedResult = service.addUbicacionToCollectionIfMissing([], ubicacion, ubicacion2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ubicacion);
          expect(expectedResult).toContain(ubicacion2);
        });

        it('should accept null and undefined values', () => {
          const ubicacion: IUbicacion = { id: 123 };
          expectedResult = service.addUbicacionToCollectionIfMissing([], null, ubicacion, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ubicacion);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
