import api from './api'
import type {
    CartRequest,
    CartProductResponse,
    UpdateQuantityRequest,
    CartResponse
} from '@/types/dto'
import type { PageResponse } from '@/types/page/page'

export const CartService = {
    createCart(data: CartRequest) {
        return api.post<CartResponse>(`/cart`, data)
    },
    getCarts(page:number=0, size:number=10) {
        return api.get<PageResponse<CartResponse>>(`/cart?page=${page}&size=${size}`)
    },
    updateQuantity(id: number, data: UpdateQuantityRequest) {
        return api.put<CartResponse>(`/cart/product/${id}`, data)
    },
    deleteCart(id: number) {
        return api.delete<void>(`/cart/product/${id}`)
    }
}