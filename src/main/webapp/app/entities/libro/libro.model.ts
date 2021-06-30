import { ICategoria } from 'app/entities/categoria/categoria.model';
import { IUserRequired } from 'app/entities/user/user-required.model';
import { IProvincia } from 'app/entities/provincia/provincia.model';
import { ILocalidad } from 'app/entities/localidad/localidad.model';
import { IUbicacion } from 'app/entities/ubicacion/ubicacion.model';

export interface ILibro {
  id?: number;
  numero?: string;
  observaciones?: string | null;
  cantidad?: number;
  categoria?: ICategoria;
  provincia?: IProvincia;
  localidad?: ILocalidad;
  user?: IUserRequired;
  ubicacion?: IUbicacion;
  createdBy?: string;
  createdDate?: Date;
  lastModifiedBy?: string;
  lastModifiedDate?: Date;
}

export class Libro implements ILibro {
  constructor(
    public id?: number,
    public numero?: string,
    public observaciones?: string | null,
    public cantidad?: number,
    public categoria?: ICategoria,
    public provincia?: IProvincia,
    public localidad?: ILocalidad,
    public user?: IUserRequired,
    public ubicacion?: IUbicacion,
    public createdBy?: string,
    public createdDate?: Date,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Date
  ) {}
}

export function getLibroIdentifier(libro: ILibro): number | undefined {
  return libro.id;
}
