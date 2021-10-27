import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDevolucion, getDevolucionIdentifier } from '../devolucion.model';

export type EntityResponseType = HttpResponse<IDevolucion>;
export type EntityArrayResponseType = HttpResponse<IDevolucion[]>;

@Injectable({ providedIn: 'root' })
export class DevolucionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/devolucions');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(devolucion: IDevolucion): Observable<EntityResponseType> {
    return this.http.post<IDevolucion>(this.resourceUrl, devolucion, { observe: 'response' });
  }

  createDevolution(devolucion: IDevolucion): Observable<EntityResponseType> {
    return this.http.post<IDevolucion>(this.resourceUrl + '/prestamo', devolucion, { observe: 'response' });
  }

  update(devolucion: IDevolucion): Observable<EntityResponseType> {
    return this.http.put<IDevolucion>(`${this.resourceUrl}/${getDevolucionIdentifier(devolucion) as number}`, devolucion, {
      observe: 'response',
    });
  }

  partialUpdate(devolucion: IDevolucion): Observable<EntityResponseType> {
    return this.http.patch<IDevolucion>(`${this.resourceUrl}/${getDevolucionIdentifier(devolucion) as number}`, devolucion, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDevolucion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDevolucion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDevolucionToCollectionIfMissing(
    devolucionCollection: IDevolucion[],
    ...devolucionsToCheck: (IDevolucion | null | undefined)[]
  ): IDevolucion[] {
    const devolucions: IDevolucion[] = devolucionsToCheck.filter(isPresent);
    if (devolucions.length > 0) {
      const devolucionCollectionIdentifiers = devolucionCollection.map(devolucionItem => getDevolucionIdentifier(devolucionItem)!);
      const devolucionsToAdd = devolucions.filter(devolucionItem => {
        const devolucionIdentifier = getDevolucionIdentifier(devolucionItem);
        if (devolucionIdentifier == null || devolucionCollectionIdentifiers.includes(devolucionIdentifier)) {
          return false;
        }
        devolucionCollectionIdentifiers.push(devolucionIdentifier);
        return true;
      });
      return [...devolucionsToAdd, ...devolucionCollection];
    }
    return devolucionCollection;
  }
}
