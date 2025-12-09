import type { OrderStatus } from './enum/status'

export interface UpdateOrderStatusRequest {
    status: OrderStatus
}

export interface OrderResponse {
    id: number;
    userId: number;
    firstName: string;
    lastName: string;
    address: string;
    totalAmount: number;
    status: OrderStatus;
    items: OrderItemResponse[];
}

export interface OrderItemResponse {
    id: number;
    orderId: number;
    productId: number;
    productName: string;
    snapshotPrice: number;
    quantity: number;
    price: number;
}