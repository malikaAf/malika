import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MarkComponent } from '../list/mark.component';
import { MarkDetailComponent } from '../detail/mark-detail.component';
import { MarkUpdateComponent } from '../update/mark-update.component';
import { MarkRoutingResolveService } from './mark-routing-resolve.service';

const markRoute: Routes = [
  {
    path: '',
    component: MarkComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MarkDetailComponent,
    resolve: {
      mark: MarkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MarkUpdateComponent,
    resolve: {
      mark: MarkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MarkUpdateComponent,
    resolve: {
      mark: MarkRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(markRoute)],
  exports: [RouterModule],
})
export class MarkRoutingModule {}
