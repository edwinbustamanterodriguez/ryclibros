import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUbicacion } from '../ubicacion.model';

@Component({
  selector: 'jhi-ubicacion-detail',
  templateUrl: './ubicacion-detail.component.html',
})
export class UbicacionDetailComponent implements OnInit {
  ubicacion: IUbicacion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ubicacion }) => {
      this.ubicacion = ubicacion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
