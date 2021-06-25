import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDevolucion } from '../devolucion.model';
import { DevolucionService } from '../service/devolucion.service';

@Component({
  templateUrl: './devolucion-delete-dialog.component.html',
})
export class DevolucionDeleteDialogComponent {
  devolucion?: IDevolucion;

  constructor(protected devolucionService: DevolucionService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.devolucionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
