export interface ICategoria {
  id?: number;
  nombre?: string;
  descripcion?: string | null;
  activo?: boolean;
}

export class Categoria implements ICategoria {
  constructor(public id?: number, public nombre?: string, public descripcion?: string | null, public activo?: boolean) {
    this.activo = this.activo ?? false;
  }
}

export function getCategoriaIdentifier(categoria: ICategoria): number | undefined {
  return categoria.id;
}
