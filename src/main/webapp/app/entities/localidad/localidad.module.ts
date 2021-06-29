import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { LocalidadComponent } from './list/localidad.component';
import { LocalidadDetailComponent } from './detail/localidad-detail.component';
import { LocalidadUpdateComponent } from './update/localidad-update.component';
import { LocalidadDeleteDialogComponent } from './delete/localidad-delete-dialog.component';
import { LocalidadRoutingModule } from './route/localidad-routing.module';

@NgModule({
  imports: [SharedModule, LocalidadRoutingModule],
  declarations: [LocalidadComponent, LocalidadDetailComponent, LocalidadUpdateComponent, LocalidadDeleteDialogComponent],
  entryComponents: [LocalidadDeleteDialogComponent],
})
export class LocalidadModule {}
