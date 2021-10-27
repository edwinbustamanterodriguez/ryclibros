import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { UbicacionComponent } from './list/ubicacion.component';
import { UbicacionDetailComponent } from './detail/ubicacion-detail.component';
import { UbicacionUpdateComponent } from './update/ubicacion-update.component';
import { UbicacionDeleteDialogComponent } from './delete/ubicacion-delete-dialog.component';
import { UbicacionRoutingModule } from './route/ubicacion-routing.module';

@NgModule({
  imports: [SharedModule, UbicacionRoutingModule],
  declarations: [UbicacionComponent, UbicacionDetailComponent, UbicacionUpdateComponent, UbicacionDeleteDialogComponent],
  entryComponents: [UbicacionDeleteDialogComponent],
})
export class UbicacionModule {}
