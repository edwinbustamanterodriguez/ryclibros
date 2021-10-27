import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPersona, Persona } from '../persona.model';

import { PersonaService } from './persona.service';

describe('Service Tests', () => {
  describe('Persona Service', () => {
    let service: PersonaService;
    let httpMock: HttpTestingController;
    let elemDefault: IPersona;
    let expectedResult: IPersona | IPersona[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PersonaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nombre: 'AAAAAAA',
        apaterno: 'AAAAAAA',
        amaterno: 'AAAAAAA',
        ci: '12',
        expedicion: 'AAAAAAA',
        telefono: 'AAAAAAA',
        institucion: 'AAAAAAA',
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

      it('should create a Persona', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Persona()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Persona', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            apaterno: 'BBBBBB',
            amaterno: 'BBBBBB',
            ci: 1,
            expedicion: 'BBBBBB',
            telefono: 'BBBBBB',
            institucion: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Persona', () => {
        const patchObject = Object.assign(
          {
            nombre: 'BBBBBB',
            institucion: 'BBBBBB',
          },
          new Persona()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Persona', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            apaterno: 'BBBBBB',
            amaterno: 'BBBBBB',
            ci: 1,
            expedicion: 'BBBBBB',
            telefono: 'BBBBBB',
            institucion: 'BBBBBB',
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

      it('should delete a Persona', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPersonaToCollectionIfMissing', () => {
        it('should add a Persona to an empty array', () => {
          const persona: IPersona = { id: 123 };
          expectedResult = service.addPersonaToCollectionIfMissing([], persona);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(persona);
        });

        it('should not add a Persona to an array that contains it', () => {
          const persona: IPersona = { id: 123 };
          const personaCollection: IPersona[] = [
            {
              ...persona,
            },
            { id: 456 },
          ];
          expectedResult = service.addPersonaToCollectionIfMissing(personaCollection, persona);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Persona to an array that doesn't contain it", () => {
          const persona: IPersona = { id: 123 };
          const personaCollection: IPersona[] = [{ id: 456 }];
          expectedResult = service.addPersonaToCollectionIfMissing(personaCollection, persona);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(persona);
        });

        it('should add only unique Persona to an array', () => {
          const personaArray: IPersona[] = [{ id: 123 }, { id: 456 }, { id: 98633 }];
          const personaCollection: IPersona[] = [{ id: 123 }];
          expectedResult = service.addPersonaToCollectionIfMissing(personaCollection, ...personaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const persona: IPersona = { id: 123 };
          const persona2: IPersona = { id: 456 };
          expectedResult = service.addPersonaToCollectionIfMissing([], persona, persona2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(persona);
          expect(expectedResult).toContain(persona2);
        });

        it('should accept null and undefined values', () => {
          const persona: IPersona = { id: 123 };
          expectedResult = service.addPersonaToCollectionIfMissing([], null, persona, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(persona);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
