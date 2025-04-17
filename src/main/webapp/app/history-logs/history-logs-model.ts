import { Resource } from 'app/common/hateoas';


export class HistoryLogsDTO extends Resource  {

  constructor(data:Partial<HistoryLogsDTO>) {
    super();
    Object.assign(this, data);
  }

  id?: number|null;
  entityType?: string|null;
  changedDate?: string|null;
  action?: string|null;
  client?: number|null;

}
