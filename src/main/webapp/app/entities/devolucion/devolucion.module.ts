import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DevolucionComponent } from './list/devolucion.component';
import { DevolucionDetailDialogComponent } from './detail/devolucion-detail-dialog.component';
import { DevolucionUpdateComponent } from './update/devolucion-update.component';
import { DevolucionDeleteDialogComponent } from './delete/devolucion-delete-dialog.component';
import { DevolucionRoutingModule } from './route/devolucion-routing.module';

@NgModule({
  imports: [SharedModule, DevolucionRoutingModule],
  declarations: [DevolucionComponent, DevolucionDetailDialogComponent, DevolucionUpdateComponent, DevolucionDeleteDialogComponent],
  entryComponents: [DevolucionDeleteDialogComponent, DevolucionDetailDialogComponent],
})
export class DevolucionModule {}
