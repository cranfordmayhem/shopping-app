import api from './api'
import type {
    UserAccountRequest,
    UserAccountResponse,
    UserProfileRequest,
    UserProfileResponse,
    UserAddressRequest,
    UserAddressResponse
} from '@/types/dto'
import type { PageResponse } from '@/types/page/page'

export const UserService = {
    getAccount(id: number) {
        return api.get<UserAccountResponse>(`/account/${id}`)
    },
    updateAccount(id: number, data: UserAccountRequest) {
        return api.put<UserAccountResponse>(`/account/${id}`, data)
    },
    deleteAccount(id: number) {
        return api.delete<void>(`/account/${id}`)
    },
    createProfile(data: UserProfileRequest) {
        return api.post<UserProfileResponse>('/profile', data)
    },
    getProfiles(page:number=0, size:number=10) {
        return api.get<PageResponse<UserProfileResponse>>(`/profile?page=${page}&size=${size}`)
    },
    getProfile(id: number) {
        return api.get<UserProfileResponse>(`/profile/${id}`)
    },
    getProfileViaLogin() {
        return api.get<UserProfileResponse>('/profile/me')
    },
    updateProfile(id: number, data: UserProfileRequest) {
        return api.put<UserProfileResponse>(`/profile/${id}`, data)
    },
    deleteProfile(id: number) {
        return api.delete<void>(`/profile/${id}`)
    },
    createAddress(data: UserAddressRequest) {
        return api.post<UserAddressResponse>(`/address`, data)
    },
    getAddresses(page:number=0, size:number=10) {
        return api.get<PageResponse<UserAddressRequest>>(`address?page=${page}&size=${size}`)
    },
    getAddress(id: number) {
        return api.get<UserAddressResponse>(`address/${id}`)
    },
    updateAddress(id: number, data: UserAddressRequest) {
        return api.put<UserAddressResponse>(`address/${id}`, data)
    },
    setDefaultAddress(id: number) {
        return api.patch<UserAddressResponse>(`address/${id}/setDefault`)
    },
    deleteAddress(id: number) {
        return api.delete<void>(`address/${id}`)
    }
}