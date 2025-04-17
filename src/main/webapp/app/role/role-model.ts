import { Resource } from 'app/common/hateoas';


export class RoleDTO extends Resource  {

  constructor(data:Partial<RoleDTO>) {
    super();
    Object.assign(this, data);
  }

  id?: number|null;
  name?: string|null;

}
