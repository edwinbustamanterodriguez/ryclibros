import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IUbicacion, Ubicacion } from '../ubicacion.model';
import { UbicacionService } from '../service/ubicacion.service';

@Component({
  selector: 'jhi-ubicacion-update',
  templateUrl: './ubicacion-update.component.html',
})
export class UbicacionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    sector: [null, [Validators.required]],
    numero: [null, [Validators.required]],
    serie: [null, [Validators.required]],
  });

  constructor(protected ubicacionService: UbicacionService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ubicacion }) => {
      this.updateForm(ubicacion);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ubicacion = this.createFromForm();
    if (ubicacion.id !== undefined) {
      this.subscribeToSaveResponse(this.ubicacionService.update(ubicacion));
    } else {
      this.subscribeToSaveResponse(this.ubicacionService.create(ubicacion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUbicacion>>): void {
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

  protected updateForm(ubicacion: IUbicacion): void {
    this.editForm.patchValue({
      id: ubicacion.id,
      sector: ubicacion.sector,
      numero: ubicacion.numero,
      serie: ubicacion.serie,
    });
  }

  protected createFromForm(): IUbicacion {
    return {
      ...new Ubicacion(),
      id: this.editForm.get(['id'])!.value,
      sector: this.editForm.get(['sector'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      serie: this.editForm.get(['serie'])!.value,
    };
  }
}
