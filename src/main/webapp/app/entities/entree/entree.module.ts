import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EntreeComponent } from './list/entree.component';
import { EntreeDetailComponent } from './detail/entree-detail.component';
import { EntreeUpdateComponent } from './update/entree-update.component';
import { EntreeDeleteDialogComponent } from './delete/entree-delete-dialog.component';
import { EntreeRoutingModule } from './route/entree-routing.module';

@NgModule({
  imports: [SharedModule, EntreeRoutingModule],
  declarations: [EntreeComponent, EntreeDetailComponent, EntreeUpdateComponent, EntreeDeleteDialogComponent],
  entryComponents: [EntreeDeleteDialogComponent],
})
export class EntreeModule {}
