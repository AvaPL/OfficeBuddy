export interface Office {
  id: string;
  name: string;
  notes: string[];
  address: Address;
  isArchived: boolean;
}

export interface Address {
  addressLine1: string;
  addressLine2: string | null;
  postalCode: string;
  city: string;
  country: string;
}