import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrestamoComponent } from '../list/prestamo.component';
import { PrestamoDetailComponent } from '../detail/prestamo-detail.component';
import { PrestamoUpdateComponent } from '../update/prestamo-update.component';
import { PrestamoRoutingResolveService } from './prestamo-routing-resolve.service';

const prestamoRoute: Routes = [
  {
    path: '',
    component: PrestamoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrestamoDetailComponent,
    resolve: {
      prestamo: PrestamoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrestamoUpdateComponent,
    resolve: {
      prestamo: PrestamoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrestamoUpdateComponent,
    resolve: {
      prestamo: PrestamoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prestamoRoute)],
  exports: [RouterModule],
})
export class PrestamoRoutingModule {}
