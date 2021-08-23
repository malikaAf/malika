import * as dayjs from 'dayjs';
import { IEntree } from 'app/entities/entree/entree.model';
import { IAcheteur } from 'app/entities/acheteur/acheteur.model';

export interface ISortie {
  id?: number;
  libelle?: string | null;
  quantite?: number;
  prixUnitaireTTC?: number;
  dateSortie?: dayjs.Dayjs | null;
  bordereauContentType?: string | null;
  bordereau?: string | null;
  observation?: string;
  deleted?: boolean;
  entree?: IEntree | null;
  acheteur?: IAcheteur | null;
}

export class Sortie implements ISortie {
  constructor(
    public id?: number,
    public libelle?: string | null,
    public quantite?: number,
    public prixUnitaireTTC?: number,
    public dateSortie?: dayjs.Dayjs | null,
    public bordereauContentType?: string | null,
    public bordereau?: string | null,
    public observation?: string,
    public deleted?: boolean,
    public entree?: IEntree | null,
    public acheteur?: IAcheteur | null
  ) {
    this.deleted = this.deleted ?? false;
  }
}

export function getSortieIdentifier(sortie: ISortie): number | undefined {
  return sortie.id;
}
