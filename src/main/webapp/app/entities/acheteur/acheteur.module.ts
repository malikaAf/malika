import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AcheteurComponent } from './list/acheteur.component';
import { AcheteurDetailComponent } from './detail/acheteur-detail.component';
import { AcheteurUpdateComponent } from './update/acheteur-update.component';
import { AcheteurDeleteDialogComponent } from './delete/acheteur-delete-dialog.component';
import { AcheteurRoutingModule } from './route/acheteur-routing.module';

@NgModule({
  imports: [SharedModule, AcheteurRoutingModule],
  declarations: [AcheteurComponent, AcheteurDetailComponent, AcheteurUpdateComponent, AcheteurDeleteDialogComponent],
  entryComponents: [AcheteurDeleteDialogComponent],
})
export class AcheteurModule {}
