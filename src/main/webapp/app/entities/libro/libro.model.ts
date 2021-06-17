import { ICategoria } from 'app/entities/categoria/categoria.model';

export interface ILibro {
  id?: number;
  numero?: string;
  observaciones?: string | null;
  categoria?: ICategoria;
}

export class Libro implements ILibro {
  constructor(public id?: number, public numero?: string, public observaciones?: string | null, public categoria?: ICategoria) {}
}

export function getLibroIdentifier(libro: ILibro): number | undefined {
  return libro.id;
}
