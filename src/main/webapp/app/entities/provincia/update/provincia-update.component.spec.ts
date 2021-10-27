jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProvinciaService } from '../service/provincia.service';
import { IProvincia, Provincia } from '../provincia.model';

import { ProvinciaUpdateComponent } from './provincia-update.component';

describe('Component Tests', () => {
  describe('Provincia Management Update Component', () => {
    let comp: ProvinciaUpdateComponent;
    let fixture: ComponentFixture<ProvinciaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let provinciaService: ProvinciaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProvinciaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProvinciaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProvinciaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      provinciaService = TestBed.inject(ProvinciaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const provincia: IProvincia = { id: 456 };

        activatedRoute.data = of({ provincia });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(provincia));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const provincia = { id: 123 };
        spyOn(provinciaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ provincia });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: provincia }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(provinciaService.update).toHaveBeenCalledWith(provincia);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const provincia = new Provincia();
        spyOn(provinciaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ provincia });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: provincia }));
        saveSubject.complete();

        // THEN
        expect(provinciaService.create).toHaveBeenCalledWith(provincia);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const provincia = { id: 123 };
        spyOn(provinciaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ provincia });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(provinciaService.update).toHaveBeenCalledWith(provincia);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
