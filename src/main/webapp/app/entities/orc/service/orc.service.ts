import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrc, getOrcIdentifier } from '../orc.model';

export type EntityResponseType = HttpResponse<IOrc>;
export type EntityArrayResponseType = HttpResponse<IOrc[]>;

@Injectable({ providedIn: 'root' })
export class OrcService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/orcs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(orc: IOrc): Observable<EntityResponseType> {
    return this.http.post<IOrc>(this.resourceUrl, orc, { observe: 'response' });
  }

  update(orc: IOrc): Observable<EntityResponseType> {
    return this.http.put<IOrc>(`${this.resourceUrl}/${getOrcIdentifier(orc) as number}`, orc, { observe: 'response' });
  }

  partialUpdate(orc: IOrc): Observable<EntityResponseType> {
    return this.http.patch<IOrc>(`${this.resourceUrl}/${getOrcIdentifier(orc) as number}`, orc, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrc>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrc[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrcToCollectionIfMissing(orcCollection: IOrc[], ...orcsToCheck: (IOrc | null | undefined)[]): IOrc[] {
    const orcs: IOrc[] = orcsToCheck.filter(isPresent);
    if (orcs.length > 0) {
      const orcCollectionIdentifiers = orcCollection.map(orcItem => getOrcIdentifier(orcItem)!);
      const orcsToAdd = orcs.filter(orcItem => {
        const orcIdentifier = getOrcIdentifier(orcItem);
        if (orcIdentifier == null || orcCollectionIdentifiers.includes(orcIdentifier)) {
          return false;
        }
        orcCollectionIdentifiers.push(orcIdentifier);
        return true;
      });
      return [...orcsToAdd, ...orcCollection];
    }
    return orcCollection;
  }
}
