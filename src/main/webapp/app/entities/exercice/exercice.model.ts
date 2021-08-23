import { IEntree } from 'app/entities/entree/entree.model';

export interface IExercice {
  id?: number;
  annee?: number | null;
  deleted?: boolean;
  entrees?: IEntree[] | null;
}

export class Exercice implements IExercice {
  constructor(public id?: number, public annee?: number | null, public deleted?: boolean, public entrees?: IEntree[] | null) {
    this.deleted = this.deleted ?? false;
  }
}

export function getExerciceIdentifier(exercice: IExercice): number | undefined {
  return exercice.id;
}
