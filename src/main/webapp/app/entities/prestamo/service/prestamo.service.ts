import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPrestamo, getPrestamoIdentifier } from '../prestamo.model';

export type EntityResponseType = HttpResponse<IPrestamo>;
export type EntityArrayResponseType = HttpResponse<IPrestamo[]>;

@Injectable({ providedIn: 'root' })
export class PrestamoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/prestamos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(prestamo: IPrestamo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prestamo);
    return this.http
      .post<IPrestamo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(prestamo: IPrestamo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prestamo);
    return this.http
      .put<IPrestamo>(`${this.resourceUrl}/${getPrestamoIdentifier(prestamo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(prestamo: IPrestamo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prestamo);
    return this.http
      .patch<IPrestamo>(`${this.resourceUrl}/${getPrestamoIdentifier(prestamo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPrestamo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrestamo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPrestamoToCollectionIfMissing(prestamoCollection: IPrestamo[], ...prestamosToCheck: (IPrestamo | null | undefined)[]): IPrestamo[] {
    const prestamos: IPrestamo[] = prestamosToCheck.filter(isPresent);
    if (prestamos.length > 0) {
      const prestamoCollectionIdentifiers = prestamoCollection.map(prestamoItem => getPrestamoIdentifier(prestamoItem)!);
      const prestamosToAdd = prestamos.filter(prestamoItem => {
        const prestamoIdentifier = getPrestamoIdentifier(prestamoItem);
        if (prestamoIdentifier == null || prestamoCollectionIdentifiers.includes(prestamoIdentifier)) {
          return false;
        }
        prestamoCollectionIdentifiers.push(prestamoIdentifier);
        return true;
      });
      return [...prestamosToAdd, ...prestamoCollection];
    }
    return prestamoCollection;
  }

  protected convertDateFromClient(prestamo: IPrestamo): IPrestamo {
    return Object.assign({}, prestamo, {
      fechaFin: prestamo.fechaFin?.isValid() ? prestamo.fechaFin.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaFin = res.body.fechaFin ? dayjs(res.body.fechaFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((prestamo: IPrestamo) => {
        prestamo.fechaFin = prestamo.fechaFin ? dayjs(prestamo.fechaFin) : undefined;
      });
    }
    return res;
  }
}
