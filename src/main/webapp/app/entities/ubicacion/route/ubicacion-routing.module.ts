import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UbicacionComponent } from '../list/ubicacion.component';
import { UbicacionDetailComponent } from '../detail/ubicacion-detail.component';
import { UbicacionUpdateComponent } from '../update/ubicacion-update.component';
import { UbicacionRoutingResolveService } from './ubicacion-routing-resolve.service';

const ubicacionRoute: Routes = [
  {
    path: '',
    component: UbicacionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UbicacionDetailComponent,
    resolve: {
      ubicacion: UbicacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UbicacionUpdateComponent,
    resolve: {
      ubicacion: UbicacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UbicacionUpdateComponent,
    resolve: {
      ubicacion: UbicacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ubicacionRoute)],
  exports: [RouterModule],
})
export class UbicacionRoutingModule {}
