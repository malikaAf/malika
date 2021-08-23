import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SortieComponent } from './list/sortie.component';
import { SortieDetailComponent } from './detail/sortie-detail.component';
import { SortieUpdateComponent } from './update/sortie-update.component';
import { SortieDeleteDialogComponent } from './delete/sortie-delete-dialog.component';
import { SortieRoutingModule } from './route/sortie-routing.module';
import { A11yModule } from '@angular/cdk/a11y';

@NgModule({
  imports: [SharedModule, SortieRoutingModule, A11yModule],
  declarations: [SortieComponent, SortieDetailComponent, SortieUpdateComponent, SortieDeleteDialogComponent],
  entryComponents: [SortieDeleteDialogComponent],
})
export class SortieModule {}
