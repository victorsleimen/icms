import { Resource } from 'app/common/hateoas';


export class AttachmentDTO extends Resource  {

  constructor(data:Partial<AttachmentDTO>) {
    super();
    Object.assign(this, data);
  }

  id?: number|null;
  fileName?: string|null;
  filePath?: string|null;
  mimeType?: string|null;
  client?: number|null;
  type?: number|null;

}
