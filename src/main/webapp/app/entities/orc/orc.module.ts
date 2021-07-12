import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { OrcComponent } from './list/orc.component';
import { OrcDetailComponent } from './detail/orc-detail.component';
import { OrcUpdateComponent } from './update/orc-update.component';
import { OrcDeleteDialogComponent } from './delete/orc-delete-dialog.component';
import { OrcRoutingModule } from './route/orc-routing.module';

@NgModule({
  imports: [SharedModule, OrcRoutingModule],
  declarations: [OrcComponent, OrcDetailComponent, OrcUpdateComponent, OrcDeleteDialogComponent],
  entryComponents: [OrcDeleteDialogComponent],
})
export class OrcModule {}
