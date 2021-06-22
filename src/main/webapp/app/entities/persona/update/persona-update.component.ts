import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPersona, Persona } from '../persona.model';
import { PersonaService } from '../service/persona.service';
import { EnumDepartamentos } from 'app/shared/enums/enum-expedicion';

@Component({
  selector: 'jhi-persona-update',
  templateUrl: './persona-update.component.html',
})
export class PersonaUpdateComponent implements OnInit {
  isSaving = false;

  departamentos: string[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    apaterno: [null, [Validators.required]],
    amaterno: [],
    ci: [null, [Validators.required]],
    expedicion: [null, [Validators.required]],
    telefono: [],
    institucion: [],
  });

  constructor(protected personaService: PersonaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {
    this.departamentos = Object.values(EnumDepartamentos);
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ persona }) => {
      this.updateForm(persona);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const persona = this.createFromForm();
    if (persona.id !== undefined) {
      this.subscribeToSaveResponse(this.personaService.update(persona));
    } else {
      this.subscribeToSaveResponse(this.personaService.create(persona));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersona>>): void {
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

  protected updateForm(persona: IPersona): void {
    this.editForm.patchValue({
      id: persona.id,
      nombre: persona.nombre,
      apaterno: persona.apaterno,
      amaterno: persona.amaterno,
      ci: persona.ci,
      expedicion: persona.expedicion,
      telefono: persona.telefono,
      institucion: persona.institucion,
    });
  }

  protected createFromForm(): IPersona {
    return {
      ...new Persona(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      apaterno: this.editForm.get(['apaterno'])!.value,
      amaterno: this.editForm.get(['amaterno'])!.value,
      ci: this.editForm.get(['ci'])!.value,
      expedicion: this.editForm.get(['expedicion'])!.value,
      telefono: this.editForm.get(['telefono'])!.value,
      institucion: this.editForm.get(['institucion'])!.value,
    };
  }
}
