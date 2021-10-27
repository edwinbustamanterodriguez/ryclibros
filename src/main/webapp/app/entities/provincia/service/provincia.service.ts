import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProvincia, getProvinciaIdentifier } from '../provincia.model';

export type EntityResponseType = HttpResponse<IProvincia>;
export type EntityArrayResponseType = HttpResponse<IProvincia[]>;

@Injectable({ providedIn: 'root' })
export class ProvinciaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/provincias');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(provincia: IProvincia): Observable<EntityResponseType> {
    return this.http.post<IProvincia>(this.resourceUrl, provincia, { observe: 'response' });
  }

  update(provincia: IProvincia): Observable<EntityResponseType> {
    return this.http.put<IProvincia>(`${this.resourceUrl}/${getProvinciaIdentifier(provincia) as number}`, provincia, {
      observe: 'response',
    });
  }

  partialUpdate(provincia: IProvincia): Observable<EntityResponseType> {
    return this.http.patch<IProvincia>(`${this.resourceUrl}/${getProvinciaIdentifier(provincia) as number}`, provincia, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProvincia>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProvincia[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProvinciaToCollectionIfMissing(
    provinciaCollection: IProvincia[],
    ...provinciasToCheck: (IProvincia | null | undefined)[]
  ): IProvincia[] {
    const provincias: IProvincia[] = provinciasToCheck.filter(isPresent);
    if (provincias.length > 0) {
      const provinciaCollectionIdentifiers = provinciaCollection.map(provinciaItem => getProvinciaIdentifier(provinciaItem)!);
      const provinciasToAdd = provincias.filter(provinciaItem => {
        const provinciaIdentifier = getProvinciaIdentifier(provinciaItem);
        if (provinciaIdentifier == null || provinciaCollectionIdentifiers.includes(provinciaIdentifier)) {
          return false;
        }
        provinciaCollectionIdentifiers.push(provinciaIdentifier);
        return true;
      });
      return [...provinciasToAdd, ...provinciaCollection];
    }
    return provinciaCollection;
  }
}
