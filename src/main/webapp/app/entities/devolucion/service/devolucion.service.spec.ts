import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDevolucion, Devolucion } from '../devolucion.model';

import { DevolucionService } from './devolucion.service';

describe('Service Tests', () => {
  describe('Devolucion Service', () => {
    let service: DevolucionService;
    let httpMock: HttpTestingController;
    let elemDefault: IDevolucion;
    let expectedResult: IDevolucion | IDevolucion[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DevolucionService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        observaciones: 'AAAAAAA',
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

      it('should create a Devolucion', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Devolucion()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Devolucion', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            observaciones: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Devolucion', () => {
        const patchObject = Object.assign(
          {
            observaciones: 'BBBBBB',
          },
          new Devolucion()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Devolucion', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            observaciones: 'BBBBBB',
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

      it('should delete a Devolucion', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDevolucionToCollectionIfMissing', () => {
        it('should add a Devolucion to an empty array', () => {
          const devolucion: IDevolucion = { id: 123 };
          expectedResult = service.addDevolucionToCollectionIfMissing([], devolucion);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(devolucion);
        });

        it('should not add a Devolucion to an array that contains it', () => {
          const devolucion: IDevolucion = { id: 123 };
          const devolucionCollection: IDevolucion[] = [
            {
              ...devolucion,
            },
            { id: 456 },
          ];
          expectedResult = service.addDevolucionToCollectionIfMissing(devolucionCollection, devolucion);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Devolucion to an array that doesn't contain it", () => {
          const devolucion: IDevolucion = { id: 123 };
          const devolucionCollection: IDevolucion[] = [{ id: 456 }];
          expectedResult = service.addDevolucionToCollectionIfMissing(devolucionCollection, devolucion);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(devolucion);
        });

        it('should add only unique Devolucion to an array', () => {
          const devolucionArray: IDevolucion[] = [{ id: 123 }, { id: 456 }, { id: 83227 }];
          const devolucionCollection: IDevolucion[] = [{ id: 123 }];
          expectedResult = service.addDevolucionToCollectionIfMissing(devolucionCollection, ...devolucionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const devolucion: IDevolucion = { id: 123 };
          const devolucion2: IDevolucion = { id: 456 };
          expectedResult = service.addDevolucionToCollectionIfMissing([], devolucion, devolucion2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(devolucion);
          expect(expectedResult).toContain(devolucion2);
        });

        it('should accept null and undefined values', () => {
          const devolucion: IDevolucion = { id: 123 };
          expectedResult = service.addDevolucionToCollectionIfMissing([], null, devolucion, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(devolucion);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
