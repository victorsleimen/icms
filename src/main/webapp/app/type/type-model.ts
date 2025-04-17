import { Resource } from 'app/common/hateoas';


export class TypeDTO extends Resource  {

  constructor(data:Partial<TypeDTO>) {
    super();
    Object.assign(this, data);
  }

  id?: number|null;
  typeCode?: string|null;
  typeName?: string|null;
  sequenceNum?: number|null;

}
