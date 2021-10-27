import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILocalidad, Localidad } from '../localidad.model';
import { LocalidadService } from '../service/localidad.service';

@Injectable({ providedIn: 'root' })
export class LocalidadRoutingResolveService implements Resolve<ILocalidad> {
  constructor(protected service: LocalidadService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILocalidad> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((localidad: HttpResponse<Localidad>) => {
          if (localidad.body) {
            return of(localidad.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Localidad());
  }
}
