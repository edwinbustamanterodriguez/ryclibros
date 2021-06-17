import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { LibroComponent } from './list/libro.component';
import { LibroDetailComponent } from './detail/libro-detail.component';
import { LibroUpdateComponent } from './update/libro-update.component';
import { LibroDeleteDialogComponent } from './delete/libro-delete-dialog.component';
import { LibroRoutingModule } from './route/libro-routing.module';

@NgModule({
  imports: [SharedModule, LibroRoutingModule],
  declarations: [LibroComponent, LibroDetailComponent, LibroUpdateComponent, LibroDeleteDialogComponent],
  entryComponents: [LibroDeleteDialogComponent],
})
export class LibroModule {}
