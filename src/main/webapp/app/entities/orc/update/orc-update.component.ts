import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IOrc, Orc } from '../orc.model';
import { OrcService } from '../service/orc.service';

@Component({
  selector: 'jhi-orc-update',
  templateUrl: './orc-update.component.html',
})
export class OrcUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    numero: [null, [Validators.required]],
  });

  constructor(protected orcService: OrcService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orc }) => {
      this.updateForm(orc);
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
    });
  }

  protected createFromForm(): IOrc {
    return {
      ...new Orc(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
    };
  }
}
