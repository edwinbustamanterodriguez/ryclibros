import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersona, getPersonaIdentifier } from '../persona.model';

export type EntityResponseType = HttpResponse<IPersona>;
export type EntityArrayResponseType = HttpResponse<IPersona[]>;

@Injectable({ providedIn: 'root' })
export class PersonaService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/personas');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(persona: IPersona): Observable<EntityResponseType> {
    return this.http.post<IPersona>(this.resourceUrl, persona, { observe: 'response' });
  }

  update(persona: IPersona): Observable<EntityResponseType> {
    return this.http.put<IPersona>(`${this.resourceUrl}/${getPersonaIdentifier(persona) as number}`, persona, { observe: 'response' });
  }

  partialUpdate(persona: IPersona): Observable<EntityResponseType> {
    return this.http.patch<IPersona>(`${this.resourceUrl}/${getPersonaIdentifier(persona) as number}`, persona, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPersona>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersona[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  oficialesDeRegistro(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersona[]>(this.resourceUrl + '/or', { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPersonaToCollectionIfMissing(personaCollection: IPersona[], ...personasToCheck: (IPersona | null | undefined)[]): IPersona[] {
    const personas: IPersona[] = personasToCheck.filter(isPresent);
    if (personas.length > 0) {
      const personaCollectionIdentifiers = personaCollection.map(personaItem => getPersonaIdentifier(personaItem)!);
      const personasToAdd = personas.filter(personaItem => {
        const personaIdentifier = getPersonaIdentifier(personaItem);
        if (personaIdentifier == null || personaCollectionIdentifiers.includes(personaIdentifier)) {
          return false;
        }
        personaCollectionIdentifiers.push(personaIdentifier);
        return true;
      });
      return [...personasToAdd, ...personaCollection];
    }
    return personaCollection;
  }

  search(currentSearch: string, optioSelected: number, req?: any): Observable<EntityArrayResponseType> {
    let options = createRequestOption(req);
    options = options.append('query', currentSearch);
    options = options.append('filter', `${optioSelected}`);

    return this.http.get<IPersona[]>(this.resourceUrl + '/_search', { params: options, observe: 'response' });
  }
}
