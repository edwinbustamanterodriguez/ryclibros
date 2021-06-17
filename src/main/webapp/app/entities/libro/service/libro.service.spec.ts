import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILibro, Libro } from '../libro.model';

import { LibroService } from './libro.service';

describe('Service Tests', () => {
  describe('Libro Service', () => {
    let service: LibroService;
    let httpMock: HttpTestingController;
    let elemDefault: ILibro;
    let expectedResult: ILibro | ILibro[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LibroService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        numero: 'AAAAAAA',
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

      it('should create a Libro', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Libro()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Libro', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            numero: 'BBBBBB',
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

      it('should partial update a Libro', () => {
        const patchObject = Object.assign(
          {
            observaciones: 'BBBBBB',
          },
          new Libro()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Libro', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            numero: 'BBBBBB',
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

      it('should delete a Libro', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLibroToCollectionIfMissing', () => {
        it('should add a Libro to an empty array', () => {
          const libro: ILibro = { id: 123 };
          expectedResult = service.addLibroToCollectionIfMissing([], libro);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(libro);
        });

        it('should not add a Libro to an array that contains it', () => {
          const libro: ILibro = { id: 123 };
          const libroCollection: ILibro[] = [
            {
              ...libro,
            },
            { id: 456 },
          ];
          expectedResult = service.addLibroToCollectionIfMissing(libroCollection, libro);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Libro to an array that doesn't contain it", () => {
          const libro: ILibro = { id: 123 };
          const libroCollection: ILibro[] = [{ id: 456 }];
          expectedResult = service.addLibroToCollectionIfMissing(libroCollection, libro);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(libro);
        });

        it('should add only unique Libro to an array', () => {
          const libroArray: ILibro[] = [{ id: 123 }, { id: 456 }, { id: 20171 }];
          const libroCollection: ILibro[] = [{ id: 123 }];
          expectedResult = service.addLibroToCollectionIfMissing(libroCollection, ...libroArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const libro: ILibro = { id: 123 };
          const libro2: ILibro = { id: 456 };
          expectedResult = service.addLibroToCollectionIfMissing([], libro, libro2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(libro);
          expect(expectedResult).toContain(libro2);
        });

        it('should accept null and undefined values', () => {
          const libro: ILibro = { id: 123 };
          expectedResult = service.addLibroToCollectionIfMissing([], null, libro, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(libro);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
