import { IMark } from 'app/entities/mark/mark.model';

export interface ICategory {
  id?: number;
  libelle?: string;
  deleted?: boolean;
  marks?: IMark[] | null;
}

export class Category implements ICategory {
  constructor(public id?: number, public libelle?: string, public deleted?: boolean, public marks?: IMark[] | null) {
    this.deleted = this.deleted ?? false;
  }
}

export function getCategoryIdentifier(category: ICategory): number | undefined {
  return category.id;
}
