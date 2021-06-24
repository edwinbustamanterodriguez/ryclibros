/* eslint-disable */
import { Component } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ILibro } from 'app/entities/libro/libro.model';
import { ITEMS_PER_PAGE_DIALOG } from 'app/config/pagination.constants';
import { LibroService } from 'app/entities/libro/service/libro.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { combineLatest } from 'rxjs';

@Component({
  templateUrl: './selected-libro-dialog.component.html',
})
export class SelectedLibroDialogComponent {
  libros?: ILibro[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE_DIALOG;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  currentSearch: string;
  searching = false;

  constructor(
    protected libroService: LibroService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    public activeModal: NgbActiveModal
  ) {
    this.currentSearch = '';
    this.ascending = true;
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    if (this.currentSearch && this.currentSearch.length !== 0) {
      this.searching = true;
      this.libroService
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
      this.libroService
        .query({
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
    }
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  trackId(index: number, item: ILibro): number {
    return item.id!;
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
      const predicate = 'id';
      const ascending = true;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: ILibro[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.libros = data ?? [];
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
    this.loadPage(1, false);
  }

  onSearchChange(): void {
    if (!this.currentSearch) {
      this.currentSearch = '';
      this.loadPage(1, false);
    }
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  selectedLibro(libro: ILibro): void {
    this.activeModal.close(libro);
  }
}
