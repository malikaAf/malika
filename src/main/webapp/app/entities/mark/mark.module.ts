import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MarkComponent } from './list/mark.component';
import { MarkDetailComponent } from './detail/mark-detail.component';
import { MarkUpdateComponent } from './update/mark-update.component';
import { MarkDeleteDialogComponent } from './delete/mark-delete-dialog.component';
import { MarkRoutingModule } from './route/mark-routing.module';

@NgModule({
  imports: [SharedModule, MarkRoutingModule],
  declarations: [MarkComponent, MarkDetailComponent, MarkUpdateComponent, MarkDeleteDialogComponent],
  entryComponents: [MarkDeleteDialogComponent],
})
export class MarkModule {}
