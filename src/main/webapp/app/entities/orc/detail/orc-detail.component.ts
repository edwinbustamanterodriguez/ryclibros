/* eslint-disable */
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrc } from '../orc.model';
import { EnumDepartamentos } from 'app/shared/enums/enum-expedicion';

@Component({
  selector: 'jhi-orc-detail',
  templateUrl: './orc-detail.component.html',
})
export class OrcDetailComponent implements OnInit {
  orc: IOrc | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orc }) => {
      this.orc = orc;
    });
  }

  previousState(): void {
    window.history.back();
  }
  departamentosAbr(expedicion: string | undefined): string {
    // @ts-ignore
    return Object.keys(EnumDepartamentos)[Object.values(EnumDepartamentos).indexOf(expedicion)];
  }
}
