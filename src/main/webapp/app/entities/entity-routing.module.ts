import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'categoria',
        data: { pageTitle: 'Categorias' },
        loadChildren: () => import('./categoria/categoria.module').then(m => m.CategoriaModule),
      },
      {
        path: 'libro',
        data: { pageTitle: 'Libros' },
        loadChildren: () => import('./libro/libro.module').then(m => m.LibroModule),
      },
      {
        path: 'persona',
        data: { pageTitle: 'Personas' },
        loadChildren: () => import('./persona/persona.module').then(m => m.PersonaModule),
      },
      {
        path: 'prestamo',
        data: { pageTitle: 'Prestamos' },
        loadChildren: () => import('./prestamo/prestamo.module').then(m => m.PrestamoModule),
      },
      {
        path: 'devolucion',
        data: { pageTitle: 'Devoluciones' },
        loadChildren: () => import('./devolucion/devolucion.module').then(m => m.DevolucionModule),
      },
      {
        path: 'provincia',
        data: { pageTitle: 'Provincias' },
        loadChildren: () => import('./provincia/provincia.module').then(m => m.ProvinciaModule),
      },
      {
        path: 'localidad',
        data: { pageTitle: 'Localidades' },
        loadChildren: () => import('./localidad/localidad.module').then(m => m.LocalidadModule),
      },

      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
