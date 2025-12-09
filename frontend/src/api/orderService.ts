import api from './api'
import type {
    OrderResponse,
    UpdateOrderStatusRequest

} from '@/types/dto'
import type { PageResponse } from '@/types/page/page'

export const OrderService = {
    createOrder() {
        return api.post<OrderResponse>('/order')
    },
    getAllOrders(page:number=0, size:number=10) {
        return api.get<PageResponse<OrderResponse>>(`/order/all?page=${page}&size=${size}`)
    },
    getOrders(page:number=0, size:number=10) {
        return api.get<PageResponse<OrderResponse>>(`/order?page=${page}&size=${size}`)
    },
    getOrder(id: number) {
        return api.get<OrderResponse>(`/order/${id}`)
    },
    updateStatus(id: number, status: string) {
        const payload: UpdateOrderStatusRequest = { status }
        return api.put<OrderResponse>(`/order/${id}`, payload)
    },
    cancelOrder(id: number) {
        return api.patch<void>(`/order/${id}/cancel`)
    }
} 