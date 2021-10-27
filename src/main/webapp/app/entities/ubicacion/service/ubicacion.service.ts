import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUbicacion, getUbicacionIdentifier } from '../ubicacion.model';

export type EntityResponseType = HttpResponse<IUbicacion>;
export type EntityArrayResponseType = HttpResponse<IUbicacion[]>;

@Injectable({ providedIn: 'root' })
export class UbicacionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/ubicacions');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(ubicacion: IUbicacion): Observable<EntityResponseType> {
    return this.http.post<IUbicacion>(this.resourceUrl, ubicacion, { observe: 'response' });
  }

  update(ubicacion: IUbicacion): Observable<EntityResponseType> {
    return this.http.put<IUbicacion>(`${this.resourceUrl}/${getUbicacionIdentifier(ubicacion) as number}`, ubicacion, {
      observe: 'response',
    });
  }

  partialUpdate(ubicacion: IUbicacion): Observable<EntityResponseType> {
    return this.http.patch<IUbicacion>(`${this.resourceUrl}/${getUbicacionIdentifier(ubicacion) as number}`, ubicacion, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUbicacion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUbicacion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUbicacionToCollectionIfMissing(
    ubicacionCollection: IUbicacion[],
    ...ubicacionsToCheck: (IUbicacion | null | undefined)[]
  ): IUbicacion[] {
    const ubicacions: IUbicacion[] = ubicacionsToCheck.filter(isPresent);
    if (ubicacions.length > 0) {
      const ubicacionCollectionIdentifiers = ubicacionCollection.map(ubicacionItem => getUbicacionIdentifier(ubicacionItem)!);
      const ubicacionsToAdd = ubicacions.filter(ubicacionItem => {
        const ubicacionIdentifier = getUbicacionIdentifier(ubicacionItem);
        if (ubicacionIdentifier == null || ubicacionCollectionIdentifiers.includes(ubicacionIdentifier)) {
          return false;
        }
        ubicacionCollectionIdentifiers.push(ubicacionIdentifier);
        return true;
      });
      return [...ubicacionsToAdd, ...ubicacionCollection];
    }
    return ubicacionCollection;
  }
}
