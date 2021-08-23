import { IEntree } from 'app/entities/entree/entree.model';
import { IMark } from 'app/entities/mark/mark.model';

export interface IArticle {
  id?: number;
  libelle?: string;
  deleted?: boolean;
  entrees?: IEntree[] | null;
  mark?: IMark | null;
}

export class Article implements IArticle {
  constructor(
    public id?: number,
    public libelle?: string,
    public deleted?: boolean,
    public entrees?: IEntree[] | null,
    public mark?: IMark | null
  ) {
    this.deleted = this.deleted ?? false;
  }
}

export function getArticleIdentifier(article: IArticle): number | undefined {
  return article.id;
}
