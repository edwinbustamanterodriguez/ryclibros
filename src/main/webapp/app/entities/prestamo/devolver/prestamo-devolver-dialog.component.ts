import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrestamo } from '../prestamo.model';
import { PrestamoService } from '../service/prestamo.service';
import { FormBuilder } from '@angular/forms';

@Component({
  templateUrl: './prestamo-devolver-dialog.component.html',
})
export class PrestamoDevolverDialogComponent {
  prestamo?: IPrestamo;
  isSaving = false;
  returnedForm = this.fb.group({
    observaciones: [],
  });

  constructor(protected prestamoService: PrestamoService, public activeModal: NgbActiveModal, protected fb: FormBuilder) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDevolucion(iPrestamo: IPrestamo): void {
    this.isSaving = true;
    iPrestamo.devuelto = true;
    this.prestamoService.update(iPrestamo).subscribe(() => {
      this.activeModal.close('returned');
    });
  }
}
