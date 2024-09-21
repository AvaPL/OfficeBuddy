export interface UpdateOffice {
  name?: string;
  notes?: string[];
  address?: UpdateAddress;
}

export interface UpdateAddress {
  addressLine1?: string;
  addressLine2?: string;
  postalCode?: string;
  city?: string;
  country?: string;
}
