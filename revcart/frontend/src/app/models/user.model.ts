export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  role: 'CUSTOMER' | 'ADMIN' | 'DELIVERY_AGENT';
  addresses: Address[];
  createdAt: Date;
  updatedAt: Date;
}

export interface Address {
  id: number;
  type: string;
  fullName: string;
  phone: string;
  line1: string;
  line2?: string;
  city: string;
  state: string;
  pincode: string;
  isDefault: boolean;
}