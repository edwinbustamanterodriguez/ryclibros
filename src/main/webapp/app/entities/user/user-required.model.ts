export interface IUserRequired {
  id?: number;
  firstName?: string;
  lastName?: string;
}

export class UserRequired implements IUserRequired {
  constructor(public id: number, public firstName: string, public lastName: string) {}
}

export function getUserIdentifier(user: IUserRequired): number | undefined {
  return user.id;
}
