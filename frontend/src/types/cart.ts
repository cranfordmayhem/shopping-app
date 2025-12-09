export interface CartRequest {
    productId: number;
    quantity: number; 
}

export interface CartProductResponse {
    id: number;
    imageUrl: string;
    name: string;
    price: number;
}

export interface UpdateQuantityRequest {
    quantity: number;
}

export interface CartResponse {
    userId: number;
    product: CartProductResponse;
    quantity: number;
}