import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUbicacion, Ubicacion } from '../ubicacion.model';
import { UbicacionService } from '../service/ubicacion.service';

@Injectable({ providedIn: 'root' })
export class UbicacionRoutingResolveService implements Resolve<IUbicacion> {
  constructor(protected service: UbicacionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUbicacion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ubicacion: HttpResponse<Ubicacion>) => {
          if (ubicacion.body) {
            return of(ubicacion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ubicacion());
  }
}
