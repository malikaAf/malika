import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EntreeComponent } from '../list/entree.component';
import { EntreeDetailComponent } from '../detail/entree-detail.component';
import { EntreeUpdateComponent } from '../update/entree-update.component';
import { EntreeRoutingResolveService } from './entree-routing-resolve.service';

const entreeRoute: Routes = [
  {
    path: '',
    component: EntreeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EntreeDetailComponent,
    resolve: {
      entree: EntreeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EntreeUpdateComponent,
    resolve: {
      entree: EntreeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EntreeUpdateComponent,
    resolve: {
      entree: EntreeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(entreeRoute)],
  exports: [RouterModule],
})
export class EntreeRoutingModule {}
