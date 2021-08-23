import { IEntree } from 'app/entities/entree/entree.model';
import { TypeFournisseur } from 'app/entities/enumerations/type-fournisseur.model';

export interface IFournisseur {
  id?: number;
  typeClient?: TypeFournisseur | null;
  nom?: string;
  prenom?: string | null;
  tel?: string;
  cnib?: string;
  email?: string;
  adresse?: string | null;
  numroBanquaire?: string | null;
  deleted?: boolean;
  entrees?: IEntree[] | null;
}

export class Fournisseur implements IFournisseur {
  constructor(
    public id?: number,
    public typeClient?: TypeFournisseur | null,
    public nom?: string,
    public prenom?: string | null,
    public tel?: string,
    public cnib?: string,
    public email?: string,
    public adresse?: string | null,
    public numroBanquaire?: string | null,
    public deleted?: boolean,
    public entrees?: IEntree[] | null
  ) {
    this.deleted = this.deleted ?? false;
  }
}

export function getFournisseurIdentifier(fournisseur: IFournisseur): number | undefined {
  return fournisseur.id;
}
