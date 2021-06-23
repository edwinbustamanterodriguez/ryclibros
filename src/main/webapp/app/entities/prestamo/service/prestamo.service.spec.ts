import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPrestamo, Prestamo } from '../prestamo.model';

import { PrestamoService } from './prestamo.service';

describe('Service Tests', () => {
  describe('Prestamo Service', () => {
    let service: PrestamoService;
    let httpMock: HttpTestingController;
    let elemDefault: IPrestamo;
    let expectedResult: IPrestamo | IPrestamo[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PrestamoService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        observaciones: 'AAAAAAA',
        fechaFin: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fechaFin: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Prestamo', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fechaFin: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaFin: currentDate,
          },
          returnedFromService
        );

        service.create(new Prestamo()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Prestamo', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            observaciones: 'BBBBBB',
            fechaFin: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaFin: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Prestamo', () => {
        const patchObject = Object.assign(
          {
            observaciones: 'BBBBBB',
          },
          new Prestamo()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            fechaFin: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Prestamo', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            observaciones: 'BBBBBB',
            fechaFin: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaFin: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Prestamo', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPrestamoToCollectionIfMissing', () => {
        it('should add a Prestamo to an empty array', () => {
          const prestamo: IPrestamo = { id: 123 };
          expectedResult = service.addPrestamoToCollectionIfMissing([], prestamo);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(prestamo);
        });

        it('should not add a Prestamo to an array that contains it', () => {
          const prestamo: IPrestamo = { id: 123 };
          const prestamoCollection: IPrestamo[] = [
            {
              ...prestamo,
            },
            { id: 456 },
          ];
          expectedResult = service.addPrestamoToCollectionIfMissing(prestamoCollection, prestamo);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Prestamo to an array that doesn't contain it", () => {
          const prestamo: IPrestamo = { id: 123 };
          const prestamoCollection: IPrestamo[] = [{ id: 456 }];
          expectedResult = service.addPrestamoToCollectionIfMissing(prestamoCollection, prestamo);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(prestamo);
        });

        it('should add only unique Prestamo to an array', () => {
          const prestamoArray: IPrestamo[] = [{ id: 123 }, { id: 456 }, { id: 51564 }];
          const prestamoCollection: IPrestamo[] = [{ id: 123 }];
          expectedResult = service.addPrestamoToCollectionIfMissing(prestamoCollection, ...prestamoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const prestamo: IPrestamo = { id: 123 };
          const prestamo2: IPrestamo = { id: 456 };
          expectedResult = service.addPrestamoToCollectionIfMissing([], prestamo, prestamo2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(prestamo);
          expect(expectedResult).toContain(prestamo2);
        });

        it('should accept null and undefined values', () => {
          const prestamo: IPrestamo = { id: 123 };
          expectedResult = service.addPrestamoToCollectionIfMissing([], null, prestamo, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(prestamo);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
