import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrestamo } from '../prestamo.model';
import { PrestamoService } from '../service/prestamo.service';

@Component({
  templateUrl: './prestamo-delete-dialog.component.html',
})
export class PrestamoDeleteDialogComponent {
  prestamo?: IPrestamo;

  constructor(protected prestamoService: PrestamoService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.prestamoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
