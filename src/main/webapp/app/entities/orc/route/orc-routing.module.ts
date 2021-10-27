import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrcComponent } from '../list/orc.component';
import { OrcDetailComponent } from '../detail/orc-detail.component';
import { OrcUpdateComponent } from '../update/orc-update.component';
import { OrcRoutingResolveService } from './orc-routing-resolve.service';

const orcRoute: Routes = [
  {
    path: '',
    component: OrcComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrcDetailComponent,
    resolve: {
      orc: OrcRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrcUpdateComponent,
    resolve: {
      orc: OrcRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrcUpdateComponent,
    resolve: {
      orc: OrcRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(orcRoute)],
  exports: [RouterModule],
})
export class OrcRoutingModule {}
