import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocalidad } from '../localidad.model';
import { LocalidadService } from '../service/localidad.service';

@Component({
  templateUrl: './localidad-delete-dialog.component.html',
})
export class LocalidadDeleteDialogComponent {
  localidad?: ILocalidad;

  constructor(protected localidadService: LocalidadService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.localidadService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
