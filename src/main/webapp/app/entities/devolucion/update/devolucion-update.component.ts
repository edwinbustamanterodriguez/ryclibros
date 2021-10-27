import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDevolucion, Devolucion } from '../devolucion.model';
import { DevolucionService } from '../service/devolucion.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPersona } from 'app/entities/persona/persona.model';
import { PersonaService } from 'app/entities/persona/service/persona.service';
import { IPrestamo } from 'app/entities/prestamo/prestamo.model';
import { PrestamoService } from 'app/entities/prestamo/service/prestamo.service';

@Component({
  selector: 'jhi-devolucion-update',
  templateUrl: './devolucion-update.component.html',
})
export class DevolucionUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  personasSharedCollection: IPersona[] = [];
  prestamosCollection: IPrestamo[] = [];

  editForm = this.fb.group({
    id: [],
    observaciones: [],
    user: [null, Validators.required],
    persona: [null, Validators.required],
    prestamo: [null, Validators.required],
  });

  constructor(
    protected devolucionService: DevolucionService,
    protected userService: UserService,
    protected personaService: PersonaService,
    protected prestamoService: PrestamoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ devolucion }) => {
      this.updateForm(devolucion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const devolucion = this.createFromForm();
    if (devolucion.id !== undefined) {
      this.subscribeToSaveResponse(this.devolucionService.update(devolucion));
    } else {
      this.subscribeToSaveResponse(this.devolucionService.create(devolucion));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackPersonaById(index: number, item: IPersona): number {
    return item.id!;
  }

  trackPrestamoById(index: number, item: IPrestamo): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDevolucion>>): void {
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

  protected updateForm(devolucion: IDevolucion): void {
    this.editForm.patchValue({
      id: devolucion.id,
      observaciones: devolucion.observaciones,
      user: devolucion.user,
      persona: devolucion.persona,
      prestamo: devolucion.prestamo,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, devolucion.user);
    this.personasSharedCollection = this.personaService.addPersonaToCollectionIfMissing(this.personasSharedCollection, devolucion.persona);
    this.prestamosCollection = this.prestamoService.addPrestamoToCollectionIfMissing(this.prestamosCollection, devolucion.prestamo);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.personaService
      .query()
      .pipe(map((res: HttpResponse<IPersona[]>) => res.body ?? []))
      .pipe(
        map((personas: IPersona[]) => this.personaService.addPersonaToCollectionIfMissing(personas, this.editForm.get('persona')!.value))
      )
      .subscribe((personas: IPersona[]) => (this.personasSharedCollection = personas));

    this.prestamoService
      .query({ filter: 'devolucion-is-null' })
      .pipe(map((res: HttpResponse<IPrestamo[]>) => res.body ?? []))
      .pipe(
        map((prestamos: IPrestamo[]) =>
          this.prestamoService.addPrestamoToCollectionIfMissing(prestamos, this.editForm.get('prestamo')!.value)
        )
      )
      .subscribe((prestamos: IPrestamo[]) => (this.prestamosCollection = prestamos));
  }

  protected createFromForm(): IDevolucion {
    return {
      ...new Devolucion(),
      id: this.editForm.get(['id'])!.value,
      observaciones: this.editForm.get(['observaciones'])!.value,
      user: this.editForm.get(['user'])!.value,
      persona: this.editForm.get(['persona'])!.value,
      prestamo: this.editForm.get(['prestamo'])!.value,
    };
  }
}
