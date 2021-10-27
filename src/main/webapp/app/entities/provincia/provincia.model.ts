export interface IProvincia {
  id?: number;
  nombre?: string;
}

export class Provincia implements IProvincia {
  constructor(public id?: number, public nombre?: string) {}
}

export function getProvinciaIdentifier(provincia: IProvincia): number | undefined {
  return provincia.id;
}
