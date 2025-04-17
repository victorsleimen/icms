import { Resource } from 'app/common/hateoas';


export class KnowledgeArticlesDTO extends Resource  {

  constructor(data:Partial<KnowledgeArticlesDTO>) {
    super();
    Object.assign(this, data);
  }

  id?: number|null;
  title?: string|null;
  content?: string|null;
  status?: string|null;
  client?: number|null;

}
