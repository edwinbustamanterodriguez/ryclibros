/* eslint-disable */
import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrestamo } from '../prestamo.model';
import { PrestamoService } from '../service/prestamo.service';
import { FormBuilder, Validators } from '@angular/forms';
import { IPersona } from 'app/entities/persona/persona.model';
import { EnumDepartamentos } from 'app/shared/enums/enum-expedicion';
import { SelectedLibroDialogComponent } from 'app/entities/prestamo/selected-libro/selected-libro-dialog.component';
import { ILibro } from 'app/entities/libro/libro.model';
import { SelectedPersonaDialogComponent } from 'app/entities/prestamo/selected-persona/selected-persona-dialog.component';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { DevolucionService } from 'app/entities/devolucion/service/devolucion.service';
import { Devolucion, IDevolucion } from 'app/entities/devolucion/devolucion.model';

@Component({
  templateUrl: './prestamo-devolver-dialog.component.html',
})
export class PrestamoDevolverDialogComponent implements OnInit {
  prestamo?: IPrestamo;
  isSaving = false;

  persona?: IPersona;
  libro?: ILibro;

  editForm = this.fb.group({
    id: [],
    observaciones: [],
    persona: [null, Validators.required],
    prestamo: [null, Validators.required],
  });

  constructor(
    protected prestamoService: PrestamoService,
    protected devolucionService: DevolucionService,
    public activeModal: NgbActiveModal,
    protected fb: FormBuilder,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    if (this.prestamo) {
      this.updateForm(this.prestamo);
      this.persona = this.prestamo.persona;
      this.libro = this.prestamo.libro;
    }
  }

  protected updateForm(prestamo: IPrestamo): void {
    this.editForm.patchValue({
      persona: prestamo.persona,
      prestamo: prestamo,
    });
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  departamentosAbr(expedicion: string | undefined): string {
    // @ts-ignore
    return Object.keys(EnumDepartamentos)[Object.values(EnumDepartamentos).indexOf(expedicion)];
  }

  selectedPrestatario(): void {
    const modalRef = this.modalService.open(SelectedPersonaDialogComponent, { size: 'xl', backdrop: 'static' });
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(
      (persona: IPersona) => {
        this.editForm.patchValue({
          persona: persona,
        });
        this.persona = persona;
      },
      () => {}
    );
  }

  protected createFromForm(): IDevolucion {
    return {
      ...new Devolucion(),
      observaciones: this.editForm.get(['observaciones'])!.value,
      persona: this.editForm.get(['persona'])!.value,
      prestamo: this.editForm.get(['prestamo'])!.value,
    };
  }

  confirmDevolucion(iPrestamo: IPrestamo): void {
    this.isSaving = true;

    this.devolucionService.createDevolution(this.createFromForm()).subscribe(
      () => {
        this.isSaving = false;
        this.activeModal.close('returned');
      },
      () => {
        this.isSaving = false;
      }
    );
  }

  validInputBootstrap(formName: string): string {
    return this.editForm.get(formName)!.dirty || this.editForm.get(formName)!.touched
      ? this.editForm.get(formName)!.invalid
        ? 'is-invalid'
        : 'is-valid'
      : '';
  }
}
