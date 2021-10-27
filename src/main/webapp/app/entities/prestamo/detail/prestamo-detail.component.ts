import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrestamo } from '../prestamo.model';

@Component({
  selector: 'jhi-prestamo-detail',
  templateUrl: './prestamo-detail.component.html',
})
export class PrestamoDetailComponent implements OnInit {
  prestamo: IPrestamo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prestamo }) => {
      this.prestamo = prestamo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
