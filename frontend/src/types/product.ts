export interface ProductRequest {
    name: string;
    imageUrl: string;
    description: string;
    price: number;
    stock: number;
}

export interface StockUpdate {
    stock: number;
}

export interface ProductResponse {
    id: number;
    name: string;
    imageUrl: string;
    description: string;
    price: number;
    stock: number;
}