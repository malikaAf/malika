import { IArticle } from 'app/entities/article/article.model';
import { ICategory } from 'app/entities/category/category.model';

export interface IMark {
  id?: number;
  libelle?: string;
  deleted?: boolean;
  articles?: IArticle[] | null;
  category?: ICategory | null;
}

export class Mark implements IMark {
  constructor(
    public id?: number,
    public libelle?: string,
    public deleted?: boolean,
    public articles?: IArticle[] | null,
    public category?: ICategory | null
  ) {
    this.deleted = this.deleted ?? false;
  }
}

export function getMarkIdentifier(mark: IMark): number | undefined {
  return mark.id;
}
