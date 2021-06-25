import { IUser } from 'app/entities/user/user.model';
import { IPersona } from 'app/entities/persona/persona.model';
import { IPrestamo } from 'app/entities/prestamo/prestamo.model';
import { IUserRequired } from 'app/entities/user/user-required.model';

export interface IDevolucion {
  id?: number;
  observaciones?: string | null;
  user?: IUserRequired;
  persona?: IPersona;
  prestamo?: IPrestamo;
  createdBy?: string;
  createdDate?: Date;
  lastModifiedBy?: string;
  lastModifiedDate?: Date;
}

export class Devolucion implements IDevolucion {
  constructor(
    public id?: number,
    public observaciones?: string | null,
    public user?: IUserRequired,
    public persona?: IPersona,
    public prestamo?: IPrestamo,
    public createdBy?: string,
    public createdDate?: Date,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Date
  ) {}
}

export function getDevolucionIdentifier(devolucion: IDevolucion): number | undefined {
  return devolucion.id;
}
