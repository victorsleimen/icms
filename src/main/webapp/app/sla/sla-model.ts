import { Resource } from 'app/common/hateoas';


export class SlaDTO extends Resource  {

  constructor(data:Partial<SlaDTO>) {
    super();
    Object.assign(this, data);
  }

  id?: number|null;
  name?: string|null;
  description?: string|null;
  responseTime?: number|null;
  resolutionTime?: number|null;
  client?: number|null;

}
