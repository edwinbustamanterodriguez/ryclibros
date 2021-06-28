import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDevolucion } from '../devolucion.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IPrestamo } from 'app/entities/prestamo/prestamo.model';
@Component({
  selector: 'jhi-devolucion-detail',
  templateUrl: './devolucion-detail-dialog.component.html',
})
export class DevolucionDetailDialogComponent implements OnInit {
  devolucion?: IDevolucion;

  prestamo?: IPrestamo;

  constructor(protected activatedRoute: ActivatedRoute, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  ngOnInit(): void {
    this.prestamo = this.devolucion?.prestamo;
  }
}
