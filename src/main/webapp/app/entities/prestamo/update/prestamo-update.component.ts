/* eslint-disable */
import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPrestamo, Prestamo } from '../prestamo.model';
import { PrestamoService } from '../service/prestamo.service';
import { ILibro } from 'app/entities/libro/libro.model';
import { LibroService } from 'app/entities/libro/service/libro.service';
import { IPersona } from 'app/entities/persona/persona.model';
import { PersonaService } from 'app/entities/persona/service/persona.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { PrestamoDevolverDialogComponent } from 'app/entities/prestamo/devolver/prestamo-devolver-dialog.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SelectedLibroDialogComponent } from 'app/entities/prestamo/selected-libro/selected-libro-dialog.component';

@Component({
  selector: 'jhi-prestamo-update',
  templateUrl: './prestamo-update.component.html',
})
export class PrestamoUpdateComponent implements OnInit {
  isSaving = false;

  librosSharedCollection: ILibro[] = [];
  personasSharedCollection: IPersona[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    observaciones: [],
    fechaFin: [null, [Validators.required]],
    libro: [null, Validators.required],
    persona: [null, Validators.required],
  });

  libro?: ILibro;

  constructor(
    protected prestamoService: PrestamoService,
    protected libroService: LibroService,
    protected personaService: PersonaService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prestamo }) => {
      if (prestamo.id === undefined) {
        const today = dayjs().startOf('day');
        prestamo.fechaFin = today;
      }

      this.updateForm(prestamo);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prestamo = this.createFromForm();
    if (prestamo.id !== undefined) {
      this.subscribeToSaveResponse(this.prestamoService.update(prestamo));
    } else {
      this.subscribeToSaveResponse(this.prestamoService.create(prestamo));
    }
  }

  trackLibroById(index: number, item: ILibro): number {
    return item.id!;
  }

  trackPersonaById(index: number, item: IPersona): number {
    return item.id!;
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrestamo>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(prestamo: IPrestamo): void {
    this.editForm.patchValue({
      id: prestamo.id,
      observaciones: prestamo.observaciones,
      fechaFin: prestamo.fechaFin ? prestamo.fechaFin.format(DATE_TIME_FORMAT) : null,
      libro: prestamo.libro,
      persona: prestamo.persona,
      user: prestamo.user,
    });

    this.librosSharedCollection = this.libroService.addLibroToCollectionIfMissing(this.librosSharedCollection, prestamo.libro);
    this.personasSharedCollection = this.personaService.addPersonaToCollectionIfMissing(this.personasSharedCollection, prestamo.persona);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, prestamo.user);
  }

  protected loadRelationshipsOptions(): void {
    this.libroService
      .query()
      .pipe(map((res: HttpResponse<ILibro[]>) => res.body ?? []))
      .pipe(map((libros: ILibro[]) => this.libroService.addLibroToCollectionIfMissing(libros, this.editForm.get('libro')!.value)))
      .subscribe((libros: ILibro[]) => (this.librosSharedCollection = libros));

    this.personaService
      .query()
      .pipe(map((res: HttpResponse<IPersona[]>) => res.body ?? []))
      .pipe(
        map((personas: IPersona[]) => this.personaService.addPersonaToCollectionIfMissing(personas, this.editForm.get('persona')!.value))
      )
      .subscribe((personas: IPersona[]) => (this.personasSharedCollection = personas));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IPrestamo {
    return {
      ...new Prestamo(),
      id: this.editForm.get(['id'])!.value,
      observaciones: this.editForm.get(['observaciones'])!.value,
      fechaFin: this.editForm.get(['fechaFin'])!.value ? dayjs(this.editForm.get(['fechaFin'])!.value, DATE_TIME_FORMAT) : undefined,
      libro: this.editForm.get(['libro'])!.value,
      persona: this.editForm.get(['persona'])!.value,
    };
  }

  selectedPrestatario(): void {}

  selectedLibro(): void {
    const modalRef = this.modalService.open(SelectedLibroDialogComponent, { size: 'xl', backdrop: 'static' });
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(
      (libro: ILibro) => {
        this.editForm.patchValue({
          libro: libro,
        });
        this.libro = libro;
      },
      () => {}
    );
  }
}
