import { Resource } from 'app/common/hateoas';


export class PriorityMatrixDTO extends Resource  {

  constructor(data:Partial<PriorityMatrixDTO>) {
    super();
    Object.assign(this, data);
  }

  id?: number|null;
  urgency?: number|null;
  impact?: number|null;
  priority?: string|null;
  type?: number|null;

}
