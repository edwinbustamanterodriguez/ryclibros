import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LibroComponent } from '../list/libro.component';
import { LibroDetailComponent } from '../detail/libro-detail.component';
import { LibroUpdateComponent } from '../update/libro-update.component';
import { LibroRoutingResolveService } from './libro-routing-resolve.service';

const libroRoute: Routes = [
  {
    path: '',
    component: LibroComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LibroDetailComponent,
    resolve: {
      libro: LibroRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LibroUpdateComponent,
    resolve: {
      libro: LibroRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LibroUpdateComponent,
    resolve: {
      libro: LibroRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(libroRoute)],
  exports: [RouterModule],
})
export class LibroRoutingModule {}
