import { Resource } from 'app/common/hateoas';


export class AllStatusDTO extends Resource  {

  constructor(data:Partial<AllStatusDTO>) {
    super();
    Object.assign(this, data);
  }

  id?: number|null;
  name?: string|null;

}
