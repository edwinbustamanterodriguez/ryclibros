/* eslint-disable */
import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IProvincia, Provincia } from '../provincia.model';
import { ProvinciaService } from '../service/provincia.service';

@Component({
  selector: 'jhi-provincia-update',
  templateUrl: './provincia-update.component.html',
})
export class ProvinciaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
  });

  constructor(protected provinciaService: ProvinciaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ provincia }) => {
      this.updateForm(provincia);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const provincia = this.createFromForm();
    if (provincia.id !== undefined) {
      this.subscribeToSaveResponse(this.provinciaService.update(provincia));
    } else {
      this.subscribeToSaveResponse(this.provinciaService.create(provincia));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProvincia>>): void {
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

  protected updateForm(provincia: IProvincia): void {
    this.editForm.patchValue({
      id: provincia.id,
      nombre: provincia.nombre,
    });
  }

  protected createFromForm(): IProvincia {
    return {
      ...new Provincia(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
    };
  }

  validInputBootstrap(formName: string): string {
    return this.editForm.get(formName)!.dirty || this.editForm.get(formName)!.touched
      ? this.editForm.get(formName)!.invalid
        ? 'is-invalid'
        : 'is-valid'
      : '';
  }
}
