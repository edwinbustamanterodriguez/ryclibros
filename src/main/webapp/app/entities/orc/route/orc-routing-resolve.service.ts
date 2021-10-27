import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrc, Orc } from '../orc.model';
import { OrcService } from '../service/orc.service';

@Injectable({ providedIn: 'root' })
export class OrcRoutingResolveService implements Resolve<IOrc> {
  constructor(protected service: OrcService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrc> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((orc: HttpResponse<Orc>) => {
          if (orc.body) {
            return of(orc.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Orc());
  }
}
