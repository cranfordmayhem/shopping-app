import api from './api'
import type {
    ProductRequest,
    StockUpdate,
    ProductResponse
} from '@/types/dto'
import type { PageResponse } from '@/types/page/page'

export const ProductService = {
    createProduct(data: ProductRequest) {
        return api.post<ProductResponse>(`/product`, data)
    },
    getProducts(page:number=0, size:number=10) {
        return api.get<PageResponse<ProductResponse>>(`/product?page=${page}&size=${size}`)
    },
    getProduct(id: number) {
        return api.get<ProductResponse>(`/product/${id}`)
    },
    updateProduct(id: number, data: ProductRequest) {
        return api.put<ProductResponse>(`/product/${id}`, data)
    },
    updateStock(id: number, data: StockUpdate) {
        return api.patch<ProductResponse>(`/product/${id}`)
    },
    deleteProduct(id: number) {
        return api.delete<void>(`/product/${id}`)
    }
}