export interface ILocalidad {
  id?: number;
  nombre?: string;
}

export class Localidad implements ILocalidad {
  constructor(public id?: number, public nombre?: string) {}
}

export function getLocalidadIdentifier(localidad: ILocalidad): number | undefined {
  return localidad.id;
}
