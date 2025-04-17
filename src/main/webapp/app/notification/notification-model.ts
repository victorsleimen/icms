import { Resource } from 'app/common/hateoas';


export class NotificationDTO extends Resource  {

  constructor(data:Partial<NotificationDTO>) {
    super();
    Object.assign(this, data);
  }

  id?: number|null;
  notifType?: string|null;
  message?: string|null;
  isRead?: boolean|null;
  client?: number|null;
  user?: number|null;
  ticket?: number|null;

}
