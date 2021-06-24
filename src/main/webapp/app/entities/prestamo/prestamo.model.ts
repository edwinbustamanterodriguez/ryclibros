import * as dayjs from 'dayjs';
import { ILibro } from 'app/entities/libro/libro.model';
import { IPersona } from 'app/entities/persona/persona.model';
import { IUserRequired } from 'app/entities/user/user-required.model';

export interface IPrestamo {
  id?: number;
  observaciones?: string | null;
  fechaFin?: dayjs.Dayjs;
  libro?: ILibro;
  persona?: IPersona;
  user?: IUserRequired;
  devuelto?: boolean;
  createdBy?: string;
  createdDate?: Date;
  lastModifiedBy?: string;
  lastModifiedDate?: Date;
}

export class Prestamo implements IPrestamo {
  constructor(
    public id?: number,
    public observaciones?: string | null,
    public fechaFin?: dayjs.Dayjs,
    public libro?: ILibro,
    public persona?: IPersona,
    public user?: IUserRequired,
    public devuelto?: boolean,
    public createdBy?: string,
    public createdDate?: Date,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Date
  ) {
    this.devuelto = this.devuelto ?? false;
  }
}

export function getPrestamoIdentifier(prestamo: IPrestamo): number | undefined {
  return prestamo.id;
}
