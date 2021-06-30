export interface IUbicacion {
  id?: number;
  sector?: string;
  numero?: number;
  serie?: string;
}

export class Ubicacion implements IUbicacion {
  constructor(public id?: number, public sector?: string, public numero?: number, public serie?: string) {}
}

export function getUbicacionIdentifier(ubicacion: IUbicacion): number | undefined {
  return ubicacion.id;
}
