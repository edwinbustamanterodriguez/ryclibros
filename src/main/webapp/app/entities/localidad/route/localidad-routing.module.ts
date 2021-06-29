import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LocalidadComponent } from '../list/localidad.component';
import { LocalidadDetailComponent } from '../detail/localidad-detail.component';
import { LocalidadUpdateComponent } from '../update/localidad-update.component';
import { LocalidadRoutingResolveService } from './localidad-routing-resolve.service';

const localidadRoute: Routes = [
  {
    path: '',
    component: LocalidadComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LocalidadDetailComponent,
    resolve: {
      localidad: LocalidadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LocalidadUpdateComponent,
    resolve: {
      localidad: LocalidadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LocalidadUpdateComponent,
    resolve: {
      localidad: LocalidadRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(localidadRoute)],
  exports: [RouterModule],
})
export class LocalidadRoutingModule {}
