import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILocalidad, Localidad } from '../localidad.model';

import { LocalidadService } from './localidad.service';

describe('Service Tests', () => {
  describe('Localidad Service', () => {
    let service: LocalidadService;
    let httpMock: HttpTestingController;
    let elemDefault: ILocalidad;
    let expectedResult: ILocalidad | ILocalidad[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LocalidadService);
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

      it('should create a Localidad', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Localidad()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Localidad', () => {
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

      it('should partial update a Localidad', () => {
        const patchObject = Object.assign({}, new Localidad());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Localidad', () => {
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

      it('should delete a Localidad', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLocalidadToCollectionIfMissing', () => {
        it('should add a Localidad to an empty array', () => {
          const localidad: ILocalidad = { id: 123 };
          expectedResult = service.addLocalidadToCollectionIfMissing([], localidad);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(localidad);
        });

        it('should not add a Localidad to an array that contains it', () => {
          const localidad: ILocalidad = { id: 123 };
          const localidadCollection: ILocalidad[] = [
            {
              ...localidad,
            },
            { id: 456 },
          ];
          expectedResult = service.addLocalidadToCollectionIfMissing(localidadCollection, localidad);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Localidad to an array that doesn't contain it", () => {
          const localidad: ILocalidad = { id: 123 };
          const localidadCollection: ILocalidad[] = [{ id: 456 }];
          expectedResult = service.addLocalidadToCollectionIfMissing(localidadCollection, localidad);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(localidad);
        });

        it('should add only unique Localidad to an array', () => {
          const localidadArray: ILocalidad[] = [{ id: 123 }, { id: 456 }, { id: 99480 }];
          const localidadCollection: ILocalidad[] = [{ id: 123 }];
          expectedResult = service.addLocalidadToCollectionIfMissing(localidadCollection, ...localidadArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const localidad: ILocalidad = { id: 123 };
          const localidad2: ILocalidad = { id: 456 };
          expectedResult = service.addLocalidadToCollectionIfMissing([], localidad, localidad2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(localidad);
          expect(expectedResult).toContain(localidad2);
        });

        it('should accept null and undefined values', () => {
          const localidad: ILocalidad = { id: 123 };
          expectedResult = service.addLocalidadToCollectionIfMissing([], null, localidad, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(localidad);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
