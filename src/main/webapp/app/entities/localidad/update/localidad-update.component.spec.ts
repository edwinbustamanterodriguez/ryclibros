jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LocalidadService } from '../service/localidad.service';
import { ILocalidad, Localidad } from '../localidad.model';

import { LocalidadUpdateComponent } from './localidad-update.component';

describe('Component Tests', () => {
  describe('Localidad Management Update Component', () => {
    let comp: LocalidadUpdateComponent;
    let fixture: ComponentFixture<LocalidadUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let localidadService: LocalidadService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LocalidadUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LocalidadUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LocalidadUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      localidadService = TestBed.inject(LocalidadService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const localidad: ILocalidad = { id: 456 };

        activatedRoute.data = of({ localidad });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(localidad));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const localidad = { id: 123 };
        spyOn(localidadService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ localidad });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: localidad }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(localidadService.update).toHaveBeenCalledWith(localidad);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const localidad = new Localidad();
        spyOn(localidadService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ localidad });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: localidad }));
        saveSubject.complete();

        // THEN
        expect(localidadService.create).toHaveBeenCalledWith(localidad);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const localidad = { id: 123 };
        spyOn(localidadService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ localidad });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(localidadService.update).toHaveBeenCalledWith(localidad);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
