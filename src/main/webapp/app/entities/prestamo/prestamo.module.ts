import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PrestamoComponent } from './list/prestamo.component';
import { PrestamoDetailComponent } from './detail/prestamo-detail.component';
import { PrestamoUpdateComponent } from './update/prestamo-update.component';
import { PrestamoDevolverDialogComponent } from './devolver/prestamo-devolver-dialog.component';
import { PrestamoRoutingModule } from './route/prestamo-routing.module';
import { SelectedLibroDialogComponent } from 'app/entities/prestamo/selected-libro/selected-libro-dialog.component';
import { SelectedPersonaDialogComponent } from 'app/entities/prestamo/selected-persona/selected-persona-dialog.component';

@NgModule({
  imports: [SharedModule, PrestamoRoutingModule],
  declarations: [
    PrestamoComponent,
    PrestamoDetailComponent,
    PrestamoUpdateComponent,
    PrestamoDevolverDialogComponent,
    SelectedLibroDialogComponent,
    SelectedPersonaDialogComponent,
  ],
  entryComponents: [PrestamoDevolverDialogComponent, SelectedLibroDialogComponent, SelectedPersonaDialogComponent],
})
export class PrestamoModule {}
