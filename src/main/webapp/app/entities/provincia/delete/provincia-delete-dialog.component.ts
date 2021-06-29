import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProvincia } from '../provincia.model';
import { ProvinciaService } from '../service/provincia.service';

@Component({
  templateUrl: './provincia-delete-dialog.component.html',
})
export class ProvinciaDeleteDialogComponent {
  provincia?: IProvincia;

  constructor(protected provinciaService: ProvinciaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.provinciaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
