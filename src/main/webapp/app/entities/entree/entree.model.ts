import * as dayjs from 'dayjs';
import { ISortie } from 'app/entities/sortie/sortie.model';
import { IArticle } from 'app/entities/article/article.model';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { IExercice } from 'app/entities/exercice/exercice.model';
import { IParametre } from 'app/entities/parametre/parametre.model';

export interface IEntree {
  id?: number;
  libelle?: string | null;
  quantite?: number;
  restant?: number | null;
  prixUnitaireTTC?: number;
  serie?: string;
  model?: string;
  caractSupplementaire?: string | null;
  dateEntree?: dayjs.Dayjs | null;
  bordereauContentType?: string | null;
  bordereau?: string | null;
  observation?: string;
  enStock?: boolean | null;
  enCommande?: boolean | null;
  deleted?: boolean;
  sorties?: ISortie[] | null;
  article?: IArticle | null;
  fournisseur?: IFournisseur | null;
  exercice?: IExercice | null;
  parametre?: IParametre | null;
}

export class Entree implements IEntree {
  constructor(
    public id?: number,
    public libelle?: string | null,
    public quantite?: number,
    public restant?: number | null,
    public prixUnitaireTTC?: number,
    public serie?: string,
    public model?: string,
    public caractSupplementaire?: string | null,
    public dateEntree?: dayjs.Dayjs | null,
    public bordereauContentType?: string | null,
    public bordereau?: string | null,
    public observation?: string,
    public enStock?: boolean | null,
    public enCommande?: boolean | null,
    public deleted?: boolean,
    public sorties?: ISortie[] | null,
    public article?: IArticle | null,
    public fournisseur?: IFournisseur | null,
    public exercice?: IExercice | null,
    public parametre?: IParametre | null
  ) {
    this.enStock = this.enStock ?? false;
    this.enCommande = this.enCommande ?? false;
    this.deleted = this.deleted ?? false;
  }
}

export function getEntreeIdentifier(entree: IEntree): number | undefined {
  return entree.id;
}
