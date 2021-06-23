import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILibro, getLibroIdentifier } from '../libro.model';
import { IPersona } from 'app/entities/persona/persona.model';

export type EntityResponseType = HttpResponse<ILibro>;
export type EntityArrayResponseType = HttpResponse<ILibro[]>;

@Injectable({ providedIn: 'root' })
export class LibroService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/libros');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(libro: ILibro): Observable<EntityResponseType> {
    return this.http.post<ILibro>(this.resourceUrl, libro, { observe: 'response' });
  }

  update(libro: ILibro): Observable<EntityResponseType> {
    return this.http.put<ILibro>(`${this.resourceUrl}/${getLibroIdentifier(libro) as number}`, libro, { observe: 'response' });
  }

  partialUpdate(libro: ILibro): Observable<EntityResponseType> {
    return this.http.patch<ILibro>(`${this.resourceUrl}/${getLibroIdentifier(libro) as number}`, libro, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILibro>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILibro[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLibroToCollectionIfMissing(libroCollection: ILibro[], ...librosToCheck: (ILibro | null | undefined)[]): ILibro[] {
    const libros: ILibro[] = librosToCheck.filter(isPresent);
    if (libros.length > 0) {
      const libroCollectionIdentifiers = libroCollection.map(libroItem => getLibroIdentifier(libroItem)!);
      const librosToAdd = libros.filter(libroItem => {
        const libroIdentifier = getLibroIdentifier(libroItem);
        if (libroIdentifier == null || libroCollectionIdentifiers.includes(libroIdentifier)) {
          return false;
        }
        libroCollectionIdentifiers.push(libroIdentifier);
        return true;
      });
      return [...librosToAdd, ...libroCollection];
    }
    return libroCollection;
  }

  search(currentSearch: string, req?: any): Observable<EntityArrayResponseType> {
    let options = createRequestOption(req);
    options = options.append('search', currentSearch);
    return this.http.get<IPersona[]>(this.resourceUrl + '/_search', { params: options, observe: 'response' });
  }
}
