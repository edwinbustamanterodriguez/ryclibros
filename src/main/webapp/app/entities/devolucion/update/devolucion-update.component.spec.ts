jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DevolucionService } from '../service/devolucion.service';
import { IDevolucion, Devolucion } from '../devolucion.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPersona } from 'app/entities/persona/persona.model';
import { PersonaService } from 'app/entities/persona/service/persona.service';
import { IPrestamo } from 'app/entities/prestamo/prestamo.model';
import { PrestamoService } from 'app/entities/prestamo/service/prestamo.service';

import { DevolucionUpdateComponent } from './devolucion-update.component';

describe('Component Tests', () => {
  describe('Devolucion Management Update Component', () => {
    let comp: DevolucionUpdateComponent;
    let fixture: ComponentFixture<DevolucionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let devolucionService: DevolucionService;
    let userService: UserService;
    let personaService: PersonaService;
    let prestamoService: PrestamoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DevolucionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(DevolucionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DevolucionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      devolucionService = TestBed.inject(DevolucionService);
      userService = TestBed.inject(UserService);
      personaService = TestBed.inject(PersonaService);
      prestamoService = TestBed.inject(PrestamoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const devolucion: IDevolucion = { id: 456 };
        const user: IUser = { id: 27699 };
        devolucion.user = user;

        const userCollection: IUser[] = [{ id: 87926 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ devolucion });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Persona query and add missing value', () => {
        const devolucion: IDevolucion = { id: 456 };
        const persona: IPersona = { id: 75961 };
        devolucion.persona = persona;

        const personaCollection: IPersona[] = [{ id: 62204 }];
        spyOn(personaService, 'query').and.returnValue(of(new HttpResponse({ body: personaCollection })));
        const additionalPersonas = [persona];
        const expectedCollection: IPersona[] = [...additionalPersonas, ...personaCollection];
        spyOn(personaService, 'addPersonaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ devolucion });
        comp.ngOnInit();

        expect(personaService.query).toHaveBeenCalled();
        expect(personaService.addPersonaToCollectionIfMissing).toHaveBeenCalledWith(personaCollection, ...additionalPersonas);
        expect(comp.personasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call prestamo query and add missing value', () => {
        const devolucion: IDevolucion = { id: 456 };
        const prestamo: IPrestamo = { id: 90392 };
        devolucion.prestamo = prestamo;

        const prestamoCollection: IPrestamo[] = [{ id: 98233 }];
        spyOn(prestamoService, 'query').and.returnValue(of(new HttpResponse({ body: prestamoCollection })));
        const expectedCollection: IPrestamo[] = [prestamo, ...prestamoCollection];
        spyOn(prestamoService, 'addPrestamoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ devolucion });
        comp.ngOnInit();

        expect(prestamoService.query).toHaveBeenCalled();
        expect(prestamoService.addPrestamoToCollectionIfMissing).toHaveBeenCalledWith(prestamoCollection, prestamo);
        expect(comp.prestamosCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const devolucion: IDevolucion = { id: 456 };
        const user: IUser = { id: 47918 };
        devolucion.user = user;
        const persona: IPersona = { id: 80653 };
        devolucion.persona = persona;
        const prestamo: IPrestamo = { id: 35748 };
        devolucion.prestamo = prestamo;

        activatedRoute.data = of({ devolucion });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(devolucion));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.personasSharedCollection).toContain(persona);
        expect(comp.prestamosCollection).toContain(prestamo);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const devolucion = { id: 123 };
        spyOn(devolucionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ devolucion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: devolucion }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(devolucionService.update).toHaveBeenCalledWith(devolucion);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const devolucion = new Devolucion();
        spyOn(devolucionService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ devolucion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: devolucion }));
        saveSubject.complete();

        // THEN
        expect(devolucionService.create).toHaveBeenCalledWith(devolucion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const devolucion = { id: 123 };
        spyOn(devolucionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ devolucion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(devolucionService.update).toHaveBeenCalledWith(devolucion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
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

      describe('trackPrestamoById', () => {
        it('Should return tracked Prestamo primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPrestamoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
