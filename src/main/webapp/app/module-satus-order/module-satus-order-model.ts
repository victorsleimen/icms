import { Resource } from 'app/common/hateoas';


export class ModuleSatusOrderDTO extends Resource  {

  constructor(data:Partial<ModuleSatusOrderDTO>) {
    super();
    Object.assign(this, data);
  }

  id?: number|null;
  moduleName?: string|null;
  level?: number|null;
  order?: number|null;
  type?: number|null;
  status?: number|null;

}
