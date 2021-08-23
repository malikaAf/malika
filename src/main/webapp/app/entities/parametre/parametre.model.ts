import { IEntree } from 'app/entities/entree/entree.model';

export interface IParametre {
  id?: number;
  libelle?: string;
  tva?: number | null;
  deleted?: boolean;
  entrees?: IEntree[] | null;
}

export class Parametre implements IParametre {
  constructor(
    public id?: number,
    public libelle?: string,
    public tva?: number | null,
    public deleted?: boolean,
    public entrees?: IEntree[] | null
  ) {
    this.deleted = this.deleted ?? false;
  }
}

export function getParametreIdentifier(parametre: IParametre): number | undefined {
  return parametre.id;
}
