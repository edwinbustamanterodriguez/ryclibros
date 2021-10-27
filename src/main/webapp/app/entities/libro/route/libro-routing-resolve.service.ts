import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILibro, Libro } from '../libro.model';
import { LibroService } from '../service/libro.service';

@Injectable({ providedIn: 'root' })
export class LibroRoutingResolveService implements Resolve<ILibro> {
  constructor(protected service: LibroService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILibro> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((libro: HttpResponse<Libro>) => {
          if (libro.body) {
            return of(libro.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Libro());
  }
}
