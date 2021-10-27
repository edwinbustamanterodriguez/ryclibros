import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LocalidadComponent } from '../list/localidad.component';
import { ProvinciaRoutingResolveService } from 'app/entities/provincia/route/provincia-routing-resolve.service';

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
    path: ':id/manage',
    component: LocalidadComponent,
    data: {
      defaultSort: 'id,asc',
    },
    resolve: {
      provincia: ProvinciaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(localidadRoute)],
  exports: [RouterModule],
})
export class LocalidadRoutingModule {}
