import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMark } from '../mark.model';

@Component({
  selector: 'jhi-mark-detail',
  templateUrl: './mark-detail.component.html',
})
export class MarkDetailComponent implements OnInit {
  mark: IMark | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mark }) => {
      this.mark = mark;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
