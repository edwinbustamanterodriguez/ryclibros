jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PrestamoService } from '../service/prestamo.service';
import { IPrestamo, Prestamo } from '../prestamo.model';
import { ILibro } from 'app/entities/libro/libro.model';
import { LibroService } from 'app/entities/libro/service/libro.service';
import { IPersona } from 'app/entities/persona/persona.model';
import { PersonaService } from 'app/entities/persona/service/persona.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { PrestamoUpdateComponent } from './prestamo-update.component';

describe('Component Tests', () => {
  describe('Prestamo Management Update Component', () => {
    let comp: PrestamoUpdateComponent;
    let fixture: ComponentFixture<PrestamoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let prestamoService: PrestamoService;
    let libroService: LibroService;
    let personaService: PersonaService;
    let userService: UserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PrestamoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PrestamoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PrestamoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      prestamoService = TestBed.inject(PrestamoService);
      libroService = TestBed.inject(LibroService);
      personaService = TestBed.inject(PersonaService);
      userService = TestBed.inject(UserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Libro query and add missing value', () => {
        const prestamo: IPrestamo = { id: 456 };
        const libro: ILibro = { id: 79437 };
        prestamo.libro = libro;

        const libroCollection: ILibro[] = [{ id: 50956 }];
        spyOn(libroService, 'query').and.returnValue(of(new HttpResponse({ body: libroCollection })));
        const additionalLibros = [libro];
        const expectedCollection: ILibro[] = [...additionalLibros, ...libroCollection];
        spyOn(libroService, 'addLibroToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ prestamo });
        comp.ngOnInit();

        expect(libroService.query).toHaveBeenCalled();
        expect(libroService.addLibroToCollectionIfMissing).toHaveBeenCalledWith(libroCollection, ...additionalLibros);
        expect(comp.librosSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Persona query and add missing value', () => {
        const prestamo: IPrestamo = { id: 456 };
        const persona: IPersona = { id: 75961 };
        prestamo.persona = persona;

        const personaCollection: IPersona[] = [{ id: 62204 }];
        spyOn(personaService, 'query').and.returnValue(of(new HttpResponse({ body: personaCollection })));
        const additionalPersonas = [persona];
        const expectedCollection: IPersona[] = [...additionalPersonas, ...personaCollection];
        spyOn(personaService, 'addPersonaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ prestamo });
        comp.ngOnInit();

        expect(personaService.query).toHaveBeenCalled();
        expect(personaService.addPersonaToCollectionIfMissing).toHaveBeenCalledWith(personaCollection, ...additionalPersonas);
        expect(comp.personasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call User query and add missing value', () => {
        const prestamo: IPrestamo = { id: 456 };
        const user: IUser = { id: 27699 };
        prestamo.user = user;

        const userCollection: IUser[] = [{ id: 87926 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ prestamo });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const prestamo: IPrestamo = { id: 456 };
        const libro: ILibro = { id: 48468 };
        prestamo.libro = libro;
        const persona: IPersona = { id: 80653 };
        prestamo.persona = persona;
        const user: IUser = { id: 47918 };
        prestamo.user = user;

        activatedRoute.data = of({ prestamo });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(prestamo));
        expect(comp.librosSharedCollection).toContain(libro);
        expect(comp.personasSharedCollection).toContain(persona);
        expect(comp.usersSharedCollection).toContain(user);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const prestamo = { id: 123 };
        spyOn(prestamoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ prestamo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: prestamo }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(prestamoService.update).toHaveBeenCalledWith(prestamo);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const prestamo = new Prestamo();
        spyOn(prestamoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ prestamo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: prestamo }));
        saveSubject.complete();

        // THEN
        expect(prestamoService.create).toHaveBeenCalledWith(prestamo);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const prestamo = { id: 123 };
        spyOn(prestamoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ prestamo });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(prestamoService.update).toHaveBeenCalledWith(prestamo);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackLibroById', () => {
        it('Should return tracked Libro primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLibroById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPersonaById', () => {
        it('Should return tracked Persona primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPersonaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
