import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISortie } from '../sortie.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-sortie-detail',
  templateUrl: './sortie-detail.component.html',
})
export class SortieDetailComponent implements OnInit {
  sortie: ISortie | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sortie }) => {
      this.sortie = sortie;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
