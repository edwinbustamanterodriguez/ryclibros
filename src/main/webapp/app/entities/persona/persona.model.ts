export interface IPersona {
  id?: number;
  nombre?: string;
  apaterno?: string;
  amaterno?: string | null;
  ci?: number;
  expedicion?: string;
  telefono?: string | null;
  institucion?: string | null;
}

export class Persona implements IPersona {
  constructor(
    public id?: number,
    public nombre?: string,
    public apaterno?: string,
    public amaterno?: string | null,
    public ci?: number,
    public expedicion?: string,
    public telefono?: string | null,
    public institucion?: string | null
  ) {}
}

export function getPersonaIdentifier(persona: IPersona): number | undefined {
  return persona.id;
}
