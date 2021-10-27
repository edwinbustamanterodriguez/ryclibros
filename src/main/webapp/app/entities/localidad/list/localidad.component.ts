/* eslint-disable */
import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocalidad } from '../localidad.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { LocalidadService } from '../service/localidad.service';
import { LocalidadDeleteDialogComponent } from '../delete/localidad-delete-dialog.component';
import { IProvincia } from 'app/entities/provincia/provincia.model';
import { LocalidadUpdateDialogComponent } from 'app/entities/localidad/dialog/localidad-update-dialog.component';

@Component({
  selector: 'jhi-localidad',
  templateUrl: './localidad.component.html',
})
export class LocalidadComponent implements OnInit {
  provincia?: IProvincia;

  localidads?: ILocalidad[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected localidadService: LocalidadService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;
    if (this.provincia?.id !== undefined) {
      this.localidadService
        .query2(this.provincia.id, {
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<ILocalidad[]>) => {
            this.isLoading = false;
            this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
          },
          () => {
            this.isLoading = false;
            this.onError();
          }
        );
    }
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ provincia }) => {
      this.provincia = provincia;
    });
    this.handleNavigation();
  }

  trackId(index: number, item: ILocalidad): number {
    return item.id!;
  }

  delete(localidad: ILocalidad): void {
    const modalRef = this.modalService.open(LocalidadDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.localidad = localidad;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: ILocalidad[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/localidad/' + this.provincia?.id + '/manage'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.localidads = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  previousState(): void {
    this.router.navigate(['/provincia']);
  }

  newLocalidad(): void {
    const modalRef = this.modalService.open(LocalidadUpdateDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.provincia = this.provincia;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'close-dialog-localidad') {
        this.loadPage();
      }
    });
  }

  editarLocalidad(localidad: ILocalidad): void {
    const modalRef = this.modalService.open(LocalidadUpdateDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.provincia = this.provincia;
    modalRef.componentInstance.localidad = localidad;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'close-dialog-localidad') {
        this.loadPage();
      }
    });
  }
}
