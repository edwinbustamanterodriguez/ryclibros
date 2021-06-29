import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILibro, Libro } from '../libro.model';
import { LibroService } from '../service/libro.service';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';
import { IProvincia } from 'app/entities/provincia/provincia.model';
import { ILocalidad } from 'app/entities/localidad/localidad.model';
import { ProvinciaService } from 'app/entities/provincia/service/provincia.service';
import { LocalidadService } from 'app/entities/localidad/service/localidad.service';

@Component({
  selector: 'jhi-libro-update',
  templateUrl: './libro-update.component.html',
})
export class LibroUpdateComponent implements OnInit {
  isSaving = false;

  categoriasSharedCollection: ICategoria[] = [];
  provinciasSharedCollection: IProvincia[] = [];
  localidadesSharedCollection: ILocalidad[] = [];

  editForm = this.fb.group({
    id: [],
    numero: [null, [Validators.required]],
    observaciones: [],
    cantidad: [null, [Validators.required, Validators.min(0)]],
    categoria: [null, Validators.required],
    provincia: [null, Validators.required],
    localidad: [null, Validators.required],
  });

  constructor(
    protected libroService: LibroService,
    protected categoriaService: CategoriaService,
    protected provinciaService: ProvinciaService,
    protected localidadService: LocalidadService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ libro }) => {
      this.updateForm(libro);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const libro = this.createFromForm();
    if (libro.id !== undefined) {
      this.subscribeToSaveResponse(this.libroService.update(libro));
    } else {
      this.subscribeToSaveResponse(this.libroService.create(libro));
    }
  }

  trackCategoriaById(index: number, item: ICategoria): number {
    return item.id!;
  }

  trackProvinciaById(index: number, item: IProvincia): number {
    return item.id!;
  }

  trackLocalidadById(index: number, item: ILocalidad): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILibro>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(libro: ILibro): void {
    this.editForm.patchValue({
      id: libro.id,
      numero: libro.numero,
      cantidad: libro.cantidad,
      observaciones: libro.observaciones,
      categoria: libro.categoria,
      provincia: libro.provincia,
      localidad: libro.localidad,
    });

    this.categoriasSharedCollection = this.categoriaService.addCategoriaToCollectionIfMissing(
      this.categoriasSharedCollection,
      libro.categoria
    );

    this.provinciasSharedCollection = this.provinciaService.addProvinciaToCollectionIfMissing(
      this.provinciasSharedCollection,
      libro.provincia
    );

    this.localidadesSharedCollection = this.localidadService.addLocalidadToCollectionIfMissing(
      this.categoriasSharedCollection,
      libro.localidad
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categoriaService
      .getAllActiveStatus()
      .pipe(map((res: HttpResponse<ICategoria[]>) => res.body ?? []))
      .pipe(
        map((categorias: ICategoria[]) =>
          this.categoriaService.addCategoriaToCollectionIfMissing(categorias, this.editForm.get('categoria')!.value)
        )
      )
      .subscribe((categorias: ICategoria[]) => (this.categoriasSharedCollection = categorias));

    this.provinciaService
      .query()
      .pipe(map((res: HttpResponse<IProvincia[]>) => res.body ?? []))
      .pipe(
        map((provincias: IProvincia[]) =>
          this.provinciaService.addProvinciaToCollectionIfMissing(provincias, this.editForm.get('provincia')!.value)
        )
      )
      .subscribe((provincias: IProvincia[]) => (this.provinciasSharedCollection = provincias));

    this.localidadService
      .query()
      .pipe(map((res: HttpResponse<ILocalidad[]>) => res.body ?? []))
      .pipe(
        map((localidades: ILocalidad[]) =>
          this.localidadService.addLocalidadToCollectionIfMissing(localidades, this.editForm.get('localidad')!.value)
        )
      )
      .subscribe((localidades: ILocalidad[]) => (this.localidadesSharedCollection = localidades));
  }

  protected createFromForm(): ILibro {
    return {
      ...new Libro(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      observaciones: this.editForm.get(['observaciones'])!.value,
      cantidad: this.editForm.get(['cantidad'])!.value,
      categoria: this.editForm.get(['categoria'])!.value,
      provincia: this.editForm.get(['provincia'])!.value,
      localidad: this.editForm.get(['localidad'])!.value,
    };
  }
}
