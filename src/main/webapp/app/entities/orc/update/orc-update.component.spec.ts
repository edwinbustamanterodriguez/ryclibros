jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrcService } from '../service/orc.service';
import { IOrc, Orc } from '../orc.model';

import { OrcUpdateComponent } from './orc-update.component';

describe('Component Tests', () => {
  describe('Orc Management Update Component', () => {
    let comp: OrcUpdateComponent;
    let fixture: ComponentFixture<OrcUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let orcService: OrcService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrcUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OrcUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrcUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      orcService = TestBed.inject(OrcService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const orc: IOrc = { id: 456 };

        activatedRoute.data = of({ orc });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(orc));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const orc = { id: 123 };
        spyOn(orcService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ orc });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: orc }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(orcService.update).toHaveBeenCalledWith(orc);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const orc = new Orc();
        spyOn(orcService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ orc });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: orc }));
        saveSubject.complete();

        // THEN
        expect(orcService.create).toHaveBeenCalledWith(orc);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const orc = { id: 123 };
        spyOn(orcService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ orc });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(orcService.update).toHaveBeenCalledWith(orc);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
