/* eslint-disable */
import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrestamo } from '../prestamo.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { PrestamoService } from '../service/prestamo.service';
import { PrestamoDevolverDialogComponent } from '../devolver/prestamo-devolver-dialog.component';
import { ILibro } from 'app/entities/libro/libro.model';

@Component({
  selector: 'jhi-prestamo',
  templateUrl: './prestamo.component.html',
})
export class PrestamoComponent implements OnInit {
  prestamos?: IPrestamo[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  currentSearch: string;
  searching = false;

  constructor(
    protected prestamoService: PrestamoService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {
    this.currentSearch = '';
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    if (this.currentSearch && this.currentSearch.length !== 0) {
      this.searching = true;
      this.prestamoService
        .search(this.currentSearch, {
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<ILibro[]>) => {
            this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
          },
          () => {
            this.onError();
          }
        );
    } else {
      this.prestamoService
        .queryDevueltoFalse({
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IPrestamo[]>) => {
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
    this.handleNavigation();
  }

  trackId(index: number, item: IPrestamo): number {
    return item.id!;
  }

  returned(prestamo: IPrestamo): void {
    const modalRef = this.modalService.open(PrestamoDevolverDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.prestamo = prestamo;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'returned') {
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
      const search = params.get('search');
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending || search !== this.currentSearch) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.currentSearch = search ? search : '';
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IPrestamo[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/prestamo'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
          search: this.currentSearch,
        },
      });
    }
    this.prestamos = data ?? [];
    this.ngbPaginationPage = this.page;
    this.searching = false;
    this.isLoading = false;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
    this.searching = false;
    this.isLoading = false;
  }

  deleteSearch(): void {
    this.currentSearch = '';
    this.loadPage(1, true);
  }

  onSearchChange(): void {
    if (!this.currentSearch) {
      this.currentSearch = '';
      this.loadPage(1, true);
    }
  }
}
