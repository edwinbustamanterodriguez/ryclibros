/* eslint-disable */
import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOrc, Orc } from '../orc.model';
import { OrcService } from '../service/orc.service';
import { IPersona } from 'app/entities/persona/persona.model';
import { PersonaService } from 'app/entities/persona/service/persona.service';

@Component({
  selector: 'jhi-orc-update',
  templateUrl: './orc-update.component.html',
})
export class OrcUpdateComponent implements OnInit {
  isSaving = false;
  personasSharedCollection: IPersona[] = [];

  editForm = this.fb.group({
    id: [],
    numero: [null, [Validators.required]],
    personas: [],
  });

  constructor(
    protected orcService: OrcService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected personaService: PersonaService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orc }) => {
      this.updateForm(orc);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orc = this.createFromForm();
    if (orc.id !== undefined) {
      this.subscribeToSaveResponse(this.orcService.update(orc));
    } else {
      this.subscribeToSaveResponse(this.orcService.create(orc));
    }
  }

  trackPersonaById(index: number, item: IPersona): number {
    return item.id!;
  }

  getSelectedPersona(option: IPersona, selectedVals?: IPersona[]): IPersona {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrc>>): void {
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

  protected updateForm(orc: IOrc): void {
    this.editForm.patchValue({
      id: orc.id,
      numero: orc.numero,
      personas: orc.personas,
    });

    this.personasSharedCollection = this.personaService.addPersonaToCollectionIfMissing(
      this.personasSharedCollection,
      ...(orc.personas ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personaService
      .oficialesDeRegistro()
      .pipe(map((res: HttpResponse<IPersona[]>) => res.body ?? []))
      .pipe(
        map((personas: IPersona[]) =>
          this.personaService.addPersonaToCollectionIfMissing(personas, ...(this.editForm.get('personas')!.value ?? []))
        )
      )
      .subscribe((personas: IPersona[]) => (this.personasSharedCollection = personas));
  }

  protected createFromForm(): IOrc {
    return {
      ...new Orc(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      personas: this.editForm.get(['personas'])!.value,
    };
  }
}
