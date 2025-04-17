import { Resource } from 'app/common/hateoas';


export class TicketDTO extends Resource  {

  constructor(data:Partial<TicketDTO>) {
    super();
    Object.assign(this, data);
  }

  id?: number|null;
  code?: string|null;
  name?: string|null;
  description?: string|null;
  attachment?: string|null;
  openDate?: string|null;
  ticketType?: string|null;
  owner?: string|null;
  status?: string|null;
  urgency?: number|null;
  impact?: number|null;
  priority?: string|null;
  client?: number|null;
  sla?: number|null;
  assignee?: number|null;

}
