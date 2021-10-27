import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDevolucion, Devolucion } from '../devolucion.model';
import { DevolucionService } from '../service/devolucion.service';

@Injectable({ providedIn: 'root' })
export class DevolucionRoutingResolveService implements Resolve<IDevolucion> {
  constructor(protected service: DevolucionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDevolucion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((devolucion: HttpResponse<Devolucion>) => {
          if (devolucion.body) {
            return of(devolucion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Devolucion());
  }
}
