import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAcheteur } from '../acheteur.model';

@Component({
  selector: 'jhi-acheteur-detail',
  templateUrl: './acheteur-detail.component.html',
})
export class AcheteurDetailComponent implements OnInit {
  acheteur: IAcheteur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ acheteur }) => {
      this.acheteur = acheteur;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
