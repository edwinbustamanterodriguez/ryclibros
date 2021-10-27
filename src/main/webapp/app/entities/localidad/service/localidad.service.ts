import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILocalidad, getLocalidadIdentifier } from '../localidad.model';

export type EntityResponseType = HttpResponse<ILocalidad>;
export type EntityArrayResponseType = HttpResponse<ILocalidad[]>;

@Injectable({ providedIn: 'root' })
export class LocalidadService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/localidads');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(localidad: ILocalidad): Observable<EntityResponseType> {
    return this.http.post<ILocalidad>(this.resourceUrl, localidad, { observe: 'response' });
  }

  update(localidad: ILocalidad): Observable<EntityResponseType> {
    return this.http.put<ILocalidad>(`${this.resourceUrl}/${getLocalidadIdentifier(localidad) as number}`, localidad, {
      observe: 'response',
    });
  }

  partialUpdate(localidad: ILocalidad): Observable<EntityResponseType> {
    return this.http.patch<ILocalidad>(`${this.resourceUrl}/${getLocalidadIdentifier(localidad) as number}`, localidad, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILocalidad>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILocalidad[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  query2(id: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILocalidad[]>(`${this.resourceUrl}/provincia/${id}`, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLocalidadToCollectionIfMissing(
    localidadCollection: ILocalidad[],
    ...localidadsToCheck: (ILocalidad | null | undefined)[]
  ): ILocalidad[] {
    const localidads: ILocalidad[] = localidadsToCheck.filter(isPresent);
    if (localidads.length > 0) {
      const localidadCollectionIdentifiers = localidadCollection.map(localidadItem => getLocalidadIdentifier(localidadItem)!);
      const localidadsToAdd = localidads.filter(localidadItem => {
        const localidadIdentifier = getLocalidadIdentifier(localidadItem);
        if (localidadIdentifier == null || localidadCollectionIdentifiers.includes(localidadIdentifier)) {
          return false;
        }
        localidadCollectionIdentifiers.push(localidadIdentifier);
        return true;
      });
      return [...localidadsToAdd, ...localidadCollection];
    }
    return localidadCollection;
  }
}
