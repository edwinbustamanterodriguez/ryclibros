jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LibroService } from '../service/libro.service';
import { ILibro, Libro } from '../libro.model';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';

import { LibroUpdateComponent } from './libro-update.component';

describe('Component Tests', () => {
  describe('Libro Management Update Component', () => {
    let comp: LibroUpdateComponent;
    let fixture: ComponentFixture<LibroUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let libroService: LibroService;
    let categoriaService: CategoriaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LibroUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LibroUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LibroUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      libroService = TestBed.inject(LibroService);
      categoriaService = TestBed.inject(CategoriaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Categoria query and add missing value', () => {
        const libro: ILibro = { id: 456 };
        const categoria: ICategoria = { id: 88080 };
        libro.categoria = categoria;

        const categoriaCollection: ICategoria[] = [{ id: 37462 }];
        spyOn(categoriaService, 'query').and.returnValue(of(new HttpResponse({ body: categoriaCollection })));
        const additionalCategorias = [categoria];
        const expectedCollection: ICategoria[] = [...additionalCategorias, ...categoriaCollection];
        spyOn(categoriaService, 'addCategoriaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ libro });
        comp.ngOnInit();

        expect(categoriaService.query).toHaveBeenCalled();
        expect(categoriaService.addCategoriaToCollectionIfMissing).toHaveBeenCalledWith(categoriaCollection, ...additionalCategorias);
        expect(comp.categoriasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const libro: ILibro = { id: 456 };
        const categoria: ICategoria = { id: 25283 };
        libro.categoria = categoria;

        activatedRoute.data = of({ libro });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(libro));
        expect(comp.categoriasSharedCollection).toContain(categoria);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const libro = { id: 123 };
        spyOn(libroService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ libro });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: libro }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(libroService.update).toHaveBeenCalledWith(libro);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const libro = new Libro();
        spyOn(libroService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ libro });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: libro }));
        saveSubject.complete();

        // THEN
        expect(libroService.create).toHaveBeenCalledWith(libro);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const libro = { id: 123 };
        spyOn(libroService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ libro });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(libroService.update).toHaveBeenCalledWith(libro);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCategoriaById', () => {
        it('Should return tracked Categoria primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCategoriaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
