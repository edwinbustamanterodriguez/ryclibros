jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PersonaService } from '../service/persona.service';
import { IPersona, Persona } from '../persona.model';

import { PersonaUpdateComponent } from './persona-update.component';

describe('Component Tests', () => {
  describe('Persona Management Update Component', () => {
    let comp: PersonaUpdateComponent;
    let fixture: ComponentFixture<PersonaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let personaService: PersonaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PersonaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PersonaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PersonaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      personaService = TestBed.inject(PersonaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const persona: IPersona = { id: 456 };

        activatedRoute.data = of({ persona });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(persona));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const persona = { id: 123 };
        spyOn(personaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ persona });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: persona }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(personaService.update).toHaveBeenCalledWith(persona);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const persona = new Persona();
        spyOn(personaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ persona });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: persona }));
        saveSubject.complete();

        // THEN
        expect(personaService.create).toHaveBeenCalledWith(persona);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const persona = { id: 123 };
        spyOn(personaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ persona });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(personaService.update).toHaveBeenCalledWith(persona);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
