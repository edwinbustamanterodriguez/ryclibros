/* eslint-disable */
import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocalidad, Localidad } from '../localidad.model';
import { LocalidadService } from '../service/localidad.service';
import { IProvincia } from 'app/entities/provincia/provincia.model';
import { FormBuilder, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { finalize } from 'rxjs/operators';

@Component({
  templateUrl: './localidad-update-dialog.component.html',
})
export class LocalidadUpdateDialogComponent implements OnInit {
  localidad?: ILocalidad;
  provincia?: IProvincia;

  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    provincia: [null, [Validators.required]],
  });

  constructor(protected localidadService: LocalidadService, public activeModal: NgbActiveModal, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.editForm.patchValue({
      id: this.localidad?.id,
      nombre: this.localidad?.nombre,
      provincia: this.provincia,
    });
  }

  protected updateForm(localidad: ILocalidad): void {
    this.editForm.patchValue({
      id: localidad.id,
      nombre: localidad.nombre,
    });
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  save(): void {
    this.isSaving = true;
    const localidad = this.createFromForm();
    if (localidad.id !== undefined) {
      this.subscribeToSaveResponse(this.localidadService.update(localidad));
    } else {
      this.subscribeToSaveResponse(this.localidadService.create(localidad));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocalidad>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.activeModal.close('close-dialog-localidad');
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected createFromForm(): ILocalidad {
    return {
      ...new Localidad(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      provincia: this.editForm.get(['provincia'])!.value,
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
