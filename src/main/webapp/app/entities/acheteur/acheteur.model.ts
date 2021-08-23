import { ISortie } from 'app/entities/sortie/sortie.model';
import { TypeAcheteur } from 'app/entities/enumerations/type-acheteur.model';

export interface IAcheteur {
  id?: number;
  typeClient?: TypeAcheteur | null;
  nom?: string;
  prenom?: string | null;
  tel?: string;
  cnib?: string;
  email?: string;
  adresse?: string | null;
  numroBanquaire?: string | null;
  deleted?: boolean;
  sorties?: ISortie[] | null;
}

export class Acheteur implements IAcheteur {
  constructor(
    public id?: number,
    public typeClient?: TypeAcheteur | null,
    public nom?: string,
    public prenom?: string | null,
    public tel?: string,
    public cnib?: string,
    public email?: string,
    public adresse?: string | null,
    public numroBanquaire?: string | null,
    public deleted?: boolean,
    public sorties?: ISortie[] | null
  ) {
    this.deleted = this.deleted ?? false;
  }
}

export function getAcheteurIdentifier(acheteur: IAcheteur): number | undefined {
  return acheteur.id;
}
