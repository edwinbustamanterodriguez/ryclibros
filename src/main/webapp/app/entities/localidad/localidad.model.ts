import { IProvincia } from 'app/entities/provincia/provincia.model';

export interface ILocalidad {
  id?: number;
  nombre?: string;
  provincia?: IProvincia;
}

export class Localidad implements ILocalidad {
  constructor(public id?: number, public nombre?: string, public provicia?: IProvincia) {}
}

export function getLocalidadIdentifier(localidad: ILocalidad): number | undefined {
  return localidad.id;
}
