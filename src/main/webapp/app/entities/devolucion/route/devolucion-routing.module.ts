import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DevolucionComponent } from '../list/devolucion.component';
import { DevolucionDetailDialogComponent } from '../detail/devolucion-detail-dialog.component';
import { DevolucionUpdateComponent } from '../update/devolucion-update.component';
import { DevolucionRoutingResolveService } from './devolucion-routing-resolve.service';

const devolucionRoute: Routes = [
  {
    path: '',
    component: DevolucionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DevolucionDetailDialogComponent,
    resolve: {
      devolucion: DevolucionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DevolucionUpdateComponent,
    resolve: {
      devolucion: DevolucionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DevolucionUpdateComponent,
    resolve: {
      devolucion: DevolucionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(devolucionRoute)],
  exports: [RouterModule],
})
export class DevolucionRoutingModule {}
