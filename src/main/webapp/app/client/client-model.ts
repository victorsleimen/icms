import { Resource } from 'app/common/hateoas';


export class ClientDTO extends Resource  {

  constructor(data:Partial<ClientDTO>) {
    super();
    Object.assign(this, data);
  }

  id?: number|null;
  clientName?: string|null;
  address?: string|null;
  tel?: string|null;
  fax?: string|null;
  mobile?: string|null;
  email?: string|null;
  registrationNum?: string|null;
  webURL?: string|null;
  isBcom?: boolean|null;

}
