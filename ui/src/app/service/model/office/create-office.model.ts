import {Address} from "./office.model";

export interface CreateOffice {
  name: string;
  notes: string[];
  address: Address;
}
