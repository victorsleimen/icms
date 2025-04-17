import { Resource } from 'app/common/hateoas';


export class UserDTO extends Resource  {

  constructor(data:Partial<UserDTO>) {
    super();
    Object.assign(this, data);
  }

  id?: number|null;
  username?: string|null;
  password?: string|null;
  firstName?: string|null;
  lastName?: string|null;
  email?: string|null;
  doe?: string|null;
  timeZoneId?: string|null;
  firstLogin?: boolean|null;
  isUtc?: boolean|null;
  isActive?: boolean|null;
  loggedUser?: string|null;
  roles?: number[]|null;
  client?: number|null;

}
