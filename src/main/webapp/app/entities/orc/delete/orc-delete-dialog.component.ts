import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrc } from '../orc.model';
import { OrcService } from '../service/orc.service';

@Component({
  templateUrl: './orc-delete-dialog.component.html',
})
export class OrcDeleteDialogComponent {
  orc?: IOrc;

  constructor(protected orcService: OrcService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orcService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
