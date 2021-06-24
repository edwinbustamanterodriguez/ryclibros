/* eslint-disable */
import { Component } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IPersona } from 'app/entities/persona/persona.model';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { EnumPersonaSearch } from 'app/shared/enums/enum-persona-search';
import { PersonaService } from 'app/entities/persona/service/persona.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { PersonaDeleteDialogComponent } from 'app/entities/persona/delete/persona-delete-dialog.component';
import { combineLatest } from 'rxjs';
import { EnumDepartamentos } from 'app/shared/enums/enum-expedicion';

@Component({
  templateUrl: './selected-persona-dialog.component.html',
})
export class SelectedPersonaDialogComponent {
  personas?: IPersona[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  currentSearch: string;
  searching = false;

  enumPersonaSearch = EnumPersonaSearch;
  searchModes: string[] = [];
  optionSelected = 'Nombre';

  constructor(
    protected personaService: PersonaService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    public activeModal: NgbActiveModal
  ) {
    this.currentSearch = '';
    this.searchModes = Object.values(this.enumPersonaSearch);
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    if (this.currentSearch && this.currentSearch.length !== 0) {
      this.searching = true;

      // @ts-ignore
      let number = Object.values(EnumPersonaSearch).indexOf(this.optionSelected);
      const numberAux = number ? number : 0;

      this.personaService
        .search(this.currentSearch, numberAux, {
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IPersona[]>) => {
            this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
          },
          () => {
            this.onError();
          }
        );
    } else {
      this.personaService
        .query({
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IPersona[]>) => {
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

  trackId(index: number, item: IPersona): number {
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

  protected onSuccess(data: IPersona[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.personas = data ?? [];
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

  departamentosAbr(expedicion: string | undefined): string {
    // @ts-ignore
    return Object.keys(EnumDepartamentos)[Object.values(EnumDepartamentos).indexOf(expedicion)];
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  selectedPersona(persona: IPersona): void {
    this.activeModal.close(persona);
  }
}
