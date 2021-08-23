import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'category',
        data: { pageTitle: 'gestockApp.category.home.title' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'mark',
        data: { pageTitle: 'gestockApp.mark.home.title' },
        loadChildren: () => import('./mark/mark.module').then(m => m.MarkModule),
      },
      {
        path: 'article',
        data: { pageTitle: 'gestockApp.article.home.title' },
        loadChildren: () => import('./article/article.module').then(m => m.ArticleModule),
      },
      {
        path: 'fournisseur',
        data: { pageTitle: 'gestockApp.fournisseur.home.title' },
        loadChildren: () => import('./fournisseur/fournisseur.module').then(m => m.FournisseurModule),
      },
      {
        path: 'acheteur',
        data: { pageTitle: 'gestockApp.acheteur.home.title' },
        loadChildren: () => import('./acheteur/acheteur.module').then(m => m.AcheteurModule),
      },
      {
        path: 'parametre',
        data: { pageTitle: 'gestockApp.parametre.home.title' },
        loadChildren: () => import('./parametre/parametre.module').then(m => m.ParametreModule),
      },
      {
        path: 'exercice',
        data: { pageTitle: 'gestockApp.exercice.home.title' },
        loadChildren: () => import('./exercice/exercice.module').then(m => m.ExerciceModule),
      },
      {
        path: 'entree',
        data: { pageTitle: 'gestockApp.entree.home.title' },
        loadChildren: () => import('./entree/entree.module').then(m => m.EntreeModule),
      },
      {
        path: 'sortie',
        data: { pageTitle: 'gestockApp.sortie.home.title' },
        loadChildren: () => import('./sortie/sortie.module').then(m => m.SortieModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
