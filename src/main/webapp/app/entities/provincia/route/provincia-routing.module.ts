import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProvinciaComponent } from '../list/provincia.component';
import { ProvinciaUpdateComponent } from '../update/provincia-update.component';
import { ProvinciaRoutingResolveService } from './provincia-routing-resolve.service';

const provinciaRoute: Routes = [
  {
    path: '',
    component: ProvinciaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProvinciaUpdateComponent,
    resolve: {
      provincia: ProvinciaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProvinciaUpdateComponent,
    resolve: {
      provincia: ProvinciaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(provinciaRoute)],
  exports: [RouterModule],
})
export class ProvinciaRoutingModule {}
