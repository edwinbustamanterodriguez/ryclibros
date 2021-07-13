import { IPersona } from 'app/entities/persona/persona.model';

export interface IOrc {
  id?: number;
  numero?: string;
  personas?: IPersona[] | null;
}

export class Orc implements IOrc {
  constructor(public id?: number, public numero?: string, public personas?: IPersona[] | null) {}
}

export function getOrcIdentifier(orc: IOrc): number | undefined {
  return orc.id;
}
