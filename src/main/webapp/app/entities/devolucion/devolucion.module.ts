import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DevolucionComponent } from './list/devolucion.component';
import { DevolucionDetailComponent } from './detail/devolucion-detail.component';
import { DevolucionUpdateComponent } from './update/devolucion-update.component';
import { DevolucionDeleteDialogComponent } from './delete/devolucion-delete-dialog.component';
import { DevolucionRoutingModule } from './route/devolucion-routing.module';

@NgModule({
  imports: [SharedModule, DevolucionRoutingModule],
  declarations: [DevolucionComponent, DevolucionDetailComponent, DevolucionUpdateComponent, DevolucionDeleteDialogComponent],
  entryComponents: [DevolucionDeleteDialogComponent],
})
export class DevolucionModule {}
