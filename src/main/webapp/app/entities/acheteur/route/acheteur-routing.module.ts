import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AcheteurComponent } from '../list/acheteur.component';
import { AcheteurDetailComponent } from '../detail/acheteur-detail.component';
import { AcheteurUpdateComponent } from '../update/acheteur-update.component';
import { AcheteurRoutingResolveService } from './acheteur-routing-resolve.service';

const acheteurRoute: Routes = [
  {
    path: '',
    component: AcheteurComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AcheteurDetailComponent,
    resolve: {
      acheteur: AcheteurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AcheteurUpdateComponent,
    resolve: {
      acheteur: AcheteurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AcheteurUpdateComponent,
    resolve: {
      acheteur: AcheteurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(acheteurRoute)],
  exports: [RouterModule],
})
export class AcheteurRoutingModule {}
