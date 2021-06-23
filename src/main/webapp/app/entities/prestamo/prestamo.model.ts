import * as dayjs from 'dayjs';
import { ILibro } from 'app/entities/libro/libro.model';
import { IPersona } from 'app/entities/persona/persona.model';
import { IUser } from 'app/entities/user/user.model';

export interface IPrestamo {
  id?: number;
  observaciones?: string | null;
  fechaFin?: dayjs.Dayjs;
  libro?: ILibro;
  persona?: IPersona;
  user?: IUser;
}

export class Prestamo implements IPrestamo {
  constructor(
    public id?: number,
    public observaciones?: string | null,
    public fechaFin?: dayjs.Dayjs,
    public libro?: ILibro,
    public persona?: IPersona,
    public user?: IUser
  ) {}
}

export function getPrestamoIdentifier(prestamo: IPrestamo): number | undefined {
  return prestamo.id;
}
