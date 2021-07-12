export interface IOrc {
  id?: number;
  numero?: string;
}

export class Orc implements IOrc {
  constructor(public id?: number, public numero?: string) {}
}

export function getOrcIdentifier(orc: IOrc): number | undefined {
  return orc.id;
}
