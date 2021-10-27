/* eslint-disable */
import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILibro } from '../libro.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { LibroService } from '../service/libro.service';
import { LibroDeleteDialogComponent } from '../delete/libro-delete-dialog.component';
@Component({
  selector: 'jhi-libro',
  templateUrl: './libro.component.html',
})
export class LibroComponent implements OnInit {
  libros?: ILibro[];
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
    protected libroService: LibroService,
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

  delete(libro: ILibro): void {
    const modalRef = this.modalService.open(LibroDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.libro = libro;
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
      const search = params.get('search');
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending || search !== this.currentSearch) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.currentSearch = search ? search : '';
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: ILibro[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/libro'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
          search: this.currentSearch,
        },
      });
    }
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
    this.loadPage(1, true);
  }

  onSearchChange(): void {
    if (!this.currentSearch) {
      this.currentSearch = '';
      this.loadPage(1, true);
    }
  }
}
