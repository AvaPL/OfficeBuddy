export interface UpdateOffice {
  name: string | null;
  notes: string[] | null;
  address: UpdateAddress | null;
}

export interface UpdateAddress {
  addressLine1: string | null;
  addressLine2: string | null;
  postalCode: string | null;
  city: string | null;
  country: string | null;
}
