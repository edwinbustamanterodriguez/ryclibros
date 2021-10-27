jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UbicacionService } from '../service/ubicacion.service';
import { IUbicacion, Ubicacion } from '../ubicacion.model';

import { UbicacionUpdateComponent } from './ubicacion-update.component';

describe('Component Tests', () => {
  describe('Ubicacion Management Update Component', () => {
    let comp: UbicacionUpdateComponent;
    let fixture: ComponentFixture<UbicacionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ubicacionService: UbicacionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UbicacionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UbicacionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UbicacionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ubicacionService = TestBed.inject(UbicacionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const ubicacion: IUbicacion = { id: 456 };

        activatedRoute.data = of({ ubicacion });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ubicacion));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ubicacion = { id: 123 };
        spyOn(ubicacionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ubicacion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ubicacion }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ubicacionService.update).toHaveBeenCalledWith(ubicacion);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ubicacion = new Ubicacion();
        spyOn(ubicacionService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ubicacion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ubicacion }));
        saveSubject.complete();

        // THEN
        expect(ubicacionService.create).toHaveBeenCalledWith(ubicacion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const ubicacion = { id: 123 };
        spyOn(ubicacionService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ ubicacion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ubicacionService.update).toHaveBeenCalledWith(ubicacion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
