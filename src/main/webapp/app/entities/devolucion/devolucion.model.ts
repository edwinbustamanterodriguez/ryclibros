import { IUser } from 'app/entities/user/user.model';
import { IPersona } from 'app/entities/persona/persona.model';
import { IPrestamo } from 'app/entities/prestamo/prestamo.model';

export interface IDevolucion {
  id?: number;
  observaciones?: string | null;
  user?: IUser;
  persona?: IPersona;
  prestamo?: IPrestamo;
}

export class Devolucion implements IDevolucion {
  constructor(
    public id?: number,
    public observaciones?: string | null,
    public user?: IUser,
    public persona?: IPersona,
    public prestamo?: IPrestamo
  ) {}
}

export function getDevolucionIdentifier(devolucion: IDevolucion): number | undefined {
  return devolucion.id;
}
