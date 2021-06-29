import { ICategoria } from 'app/entities/categoria/categoria.model';
import { IUserRequired } from 'app/entities/user/user-required.model';

export interface ILibro {
  id?: number;
  numero?: string;
  observaciones?: string | null;
  cantidad?: number;
  categoria?: ICategoria;
  user?: IUserRequired;
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
    public user?: IUserRequired,
    public createdBy?: string,
    public createdDate?: Date,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Date
  ) {}
}

export function getLibroIdentifier(libro: ILibro): number | undefined {
  return libro.id;
}
