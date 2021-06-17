import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILibro } from '../libro.model';
import { LibroService } from '../service/libro.service';

@Component({
  templateUrl: './libro-delete-dialog.component.html',
})
export class LibroDeleteDialogComponent {
  libro?: ILibro;

  constructor(protected libroService: LibroService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.libroService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
