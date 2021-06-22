export interface IPersona {
  id?: number;
  nombre?: string;
  apaterno?: string;
  amaterno?: string | null;
  ci?: string;
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
    public ci?: string,
    public expedicion?: string,
    public telefono?: string | null,
    public institucion?: string | null
  ) {}
}

export function getPersonaIdentifier(persona: IPersona): number | undefined {
  return persona.id;
}
