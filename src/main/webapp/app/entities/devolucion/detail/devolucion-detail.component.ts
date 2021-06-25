import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDevolucion } from '../devolucion.model';

@Component({
  selector: 'jhi-devolucion-detail',
  templateUrl: './devolucion-detail.component.html',
})
export class DevolucionDetailComponent implements OnInit {
  devolucion: IDevolucion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ devolucion }) => {
      this.devolucion = devolucion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
