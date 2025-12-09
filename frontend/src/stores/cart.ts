// src/stores/cart.ts
import { computed } from 'vue'
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { CartService } from '@/api/services'
import type { CartResponse, CartRequest, UpdateQuantityRequest } from '@/types/dto'

export const useCartStore = defineStore('cart', () => {
    const items = ref<CartResponse[]>([])
    const page = ref(0)
    const size = ref(10)
    const totalPages = ref(0)

    // Load cart items from backend
    const loadCart = async () => {
        try {
            const { data } = await CartService.getCarts(page.value, size.value)
            items.value = data.content
            totalPages.value = data.totalPages
            page.value = data.number
        } catch (err) {
            console.error('Failed to load cart', err)
        }
    }

    // Add product to cart
    const addToCart = async (productId: number, quantity: number = 1) => {
        try {
            const payload: CartRequest = { productId, quantity }
            const { data } = await CartService.createCart(payload)
            // Add or update item locally
            const index = items.value.findIndex(i => i.product.id === data.product.id)
            if (index >= 0) {
                items.value[index].quantity += quantity
            } else {
                items.value.push(data)
            }
        } catch (err) {
            console.error('Failed to add to cart', err)
        }
    }

    // Update quantity
    const updateQuantity = async (cartItemId: number, quantity: number) => {
        try {
            const payload: UpdateQuantityRequest = { quantity }
            const { data } = await CartService.updateQuantity(cartItemId, payload)
            const index = items.value.findIndex(i => i.product.id === data.product.id)
            if (index >= 0) items.value[index] = data
        } catch (err) {
            console.error('Failed to update cart quantity', err)
        }
    }

    // Remove from cart
    const removeFromCart = async (cartItemId: number) => {
        try {
            await CartService.deleteCart(cartItemId)
            items.value = items.value.filter(i => i.product.id !== cartItemId)
        } catch (err) {
            console.error('Failed to remove from cart', err)
        }
    }

    const clearCart = async () => {
        try {
            items.value = []
        } catch (err) {
            console.error('Failed to remove all from cart', err)
        }
    }

    // Total amount
    const totalAmount = computed(() =>
        items.value.reduce((sum, item) => sum + item.quantity * item.product.price, 0)
    )

    return {
        items,
        page,
        size,
        totalPages,
        loadCart,
        addToCart,
        updateQuantity,
        clearCart,
        removeFromCart,
        totalAmount
    }
})