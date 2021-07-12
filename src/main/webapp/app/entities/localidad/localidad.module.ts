import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { LocalidadComponent } from './list/localidad.component';
import { LocalidadDeleteDialogComponent } from './delete/localidad-delete-dialog.component';
import { LocalidadRoutingModule } from './route/localidad-routing.module';
import { LocalidadUpdateDialogComponent } from 'app/entities/localidad/dialog/localidad-update-dialog.component';

@NgModule({
  imports: [SharedModule, LocalidadRoutingModule],
  declarations: [LocalidadComponent, LocalidadDeleteDialogComponent, LocalidadUpdateDialogComponent],
  entryComponents: [LocalidadDeleteDialogComponent, LocalidadUpdateDialogComponent],
})
export class LocalidadModule {}
