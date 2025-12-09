import type { Role } from './enum/role'

export interface UserAccountRequest {
    email: string;
    password: string;
}

export interface UserAccountResponse {
    id: number;
    email: string;
    role: Role;
}

export interface UserProfileRequest {
    firstName: string;
    lastName: string;
    age: number;
}

export interface UserProfileResponse {
    id: number;
    firstName: string;
    lastName: string;
    age: number;
    email: string;
}

export interface UserAddressRequest {
    city: string; 
    state: string;
    zipCode: string;
    country: string;
    contactNumber: string;
}

export interface UserAddressResponse {
    id: number;
    city: string; 
    state: string;
    zipCode: string;
    country: string;
    contactNumber: string;
}