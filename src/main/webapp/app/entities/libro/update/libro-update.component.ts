/* eslint-disable */
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
import { EnumUbicaciones } from 'app/shared/enums/enum-ubicaciones';
import { IUbicacion, Ubicacion } from 'app/entities/ubicacion/ubicacion.model';
import { IOrc } from 'app/entities/orc/orc.model';
import { OrcService } from 'app/entities/orc/service/orc.service';
import { PersonaService } from 'app/entities/persona/service/persona.service';
import { IPersona } from 'app/entities/persona/persona.model';

@Component({
  selector: 'jhi-libro-update',
  templateUrl: './libro-update.component.html',
})
export class LibroUpdateComponent implements OnInit {
  isSaving = false;

  categoriasSharedCollection: ICategoria[] = [];
  provinciasSharedCollection: IProvincia[] = [];
  localidadesSharedCollection: ILocalidad[] = [];
  orcsSharedCollection: IOrc[] = [];
  personasSharedCollection: IPersona[] = [];

  sectores: string[] = [];

  editForm = this.fb.group({
    id: [],
    numero: [null, [Validators.required]],
    observaciones: [],
    cantidad: [null, [Validators.required, Validators.min(0)]],
    categoria: [null, Validators.required],
    provincia: [null, Validators.required],
    localidad: [null, Validators.required],
    orc: [null, Validators.required],
    persona: [null, Validators.required],
    ubicacionId: [],
    ubicacionSector: [null, [Validators.required]],
    ubicacionNumero: [null, [Validators.required, Validators.min(0)]],
    ubicacionSerie: [null, [Validators.required]],
  });

  constructor(
    protected libroService: LibroService,
    protected categoriaService: CategoriaService,
    protected provinciaService: ProvinciaService,
    protected localidadService: LocalidadService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected orcService: OrcService,
    protected personaService: PersonaService
  ) {
    this.sectores = Object.values(EnumUbicaciones);
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ libro }) => {
      this.updateForm(libro);
      this.loadRelationshipsOptions();

      if (libro.id) {
        this.loadLocalidades(libro.provincia.id);
        this.loadPersonas(libro.orc.id);
      }
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

  trackOrcById(index: number, item: IOrc): number {
    return item.id!;
  }

  trackPersonaById(index: number, item: IPersona): number {
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
      orc: libro.orc,
      persona: libro.persona,
      ubicacionId: libro.ubicacion?.id,
      ubicacionSector: libro.ubicacion?.sector,
      ubicacionNumero: libro.ubicacion?.numero,
      ubicacionSerie: libro.ubicacion?.serie,
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
      this.localidadesSharedCollection,
      libro.localidad
    );

    this.orcsSharedCollection = this.orcService.addOrcToCollectionIfMissing(this.orcsSharedCollection, libro.orc);

    this.personasSharedCollection = this.personaService.addPersonaToCollectionIfMissing(this.personasSharedCollection, libro.persona);
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

    this.orcService
      .query()
      .pipe(map((res: HttpResponse<IOrc[]>) => res.body ?? []))
      .pipe(map((orcs: IOrc[]) => this.orcService.addOrcToCollectionIfMissing(orcs, this.editForm.get('orc')!.value)))
      .subscribe((orcs: IOrc[]) => (this.orcsSharedCollection = orcs));
  }

  loadLocalidades(provinciaId: number): void {
    this.localidadService
      .query2(provinciaId)
      .pipe(map((res: HttpResponse<ILocalidad[]>) => res.body ?? []))
      .pipe(
        map((localidades: ILocalidad[]) =>
          this.localidadService.addLocalidadToCollectionIfMissing(localidades, this.editForm.get('localidad')!.value)
        )
      )
      .subscribe((localidades: ILocalidad[]) => (this.localidadesSharedCollection = localidades));
  }

  loadPersonas(orcId: number): void {
    this.personaService
      .oficialesDeRegistroPorOrc(orcId)
      .pipe(map((res: HttpResponse<IPersona[]>) => res.body ?? []))
      .pipe(
        map((personas: IPersona[]) => this.personaService.addPersonaToCollectionIfMissing(personas, this.editForm.get('persona')!.value))
      )
      .subscribe((personas: IPersona[]) => (this.personasSharedCollection = personas));
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
      orc: this.editForm.get(['orc'])!.value,
      persona: this.editForm.get(['persona'])!.value,
      ubicacion: this.createFromFormUbicacion(),
    };
  }

  protected createFromFormUbicacion(): IUbicacion {
    return {
      ...new Ubicacion(),
      id: this.editForm.get(['ubicacionId'])!.value,
      sector: this.editForm.get(['ubicacionSector'])!.value,
      numero: this.editForm.get(['ubicacionNumero'])!.value,
      serie: this.editForm.get(['ubicacionSerie'])!.value,
    };
  }

  onChangeProvincia(iProvincia: IProvincia) {
    this.editForm.patchValue({
      localidad: null,
    });

    if (iProvincia && iProvincia.id !== undefined) {
      this.loadLocalidades(iProvincia.id);
    }
  }

  onChangeOrc(iOrc: IOrc) {
    this.editForm.patchValue({
      orc: null,
    });

    if (iOrc && iOrc.id !== undefined) {
      this.loadPersonas(iOrc.id);
    }
  }
}
