import { ICategoria } from 'app/entities/categoria/categoria.model';

export interface ILibro {
  id?: number;
  numero?: string;
  observaciones?: string | null;
  categoria?: ICategoria;
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
    public categoria?: ICategoria,
    public createdBy?: string,
    public createdDate?: Date,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Date
  ) {}
}

export function getLibroIdentifier(libro: ILibro): number | undefined {
  return libro.id;
}
