import { Resource } from 'app/common/hateoas';


export class CommentDTO extends Resource  {

  constructor(data:Partial<CommentDTO>) {
    super();
    Object.assign(this, data);
  }

  id?: number|null;
  description?: string|null;
  client?: number|null;
  user?: number|null;

}
