<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { OrderService } from '@/api/services'
import type { OrderResponse } from '@/types/dto'
import dayjs from 'dayjs'   // install if not installed: npm i dayjs

const page = ref(0)
const size = ref(5)
const totalPages = ref(0)
const orders = ref<OrderResponse[]>([])

const loadOrders = async () => {
    try {
        const { data } = await OrderService.getOrders(page.value, size.value)

        // Sort: PENDING, COMPLETED, PROCESSING → FIRST
        // CANCELLED → LAST
        orders.value = data.content.sort((a, b) => {
            if (a.status === 'CANCELLED' && b.status !== 'CANCELLED') return 1
            if (a.status !== 'CANCELLED' && b.status === 'CANCELLED') return -1
            return 0
        })

        totalPages.value = data.totalPages
        page.value = data.number
    } catch (err) {
        console.error("Failed to load orders: ", err)
    }
}


const nextPage = () => {
    if (page.value + 1 < totalPages.value) {
        page.value++
        loadOrders()
    }
}

const prevPage = () => {
    if (page.value > 0) {
        page.value--
        loadOrders()
    }
}

// Check if order is within 7 days
const canCancel = (order: OrderResponse) => {
    const createdDate = dayjs(order.createdAt)
    return dayjs().diff(createdDate, 'day') < 7 && order.status === 'PENDING'
}

// Cancel order handler
const cancelOrder = async (orderId: number) => {
    try {
        await OrderService.cancelOrder(orderId)
        await loadOrders()
    } catch (err) {
        console.error("Failed to cancel order:", err)
    }
}

onMounted(loadOrders)
</script>

<template>
    <div class="p-6">
        <h2 class="text-xl font-semibold mb-4">My Purchases</h2>

        <div class="space-y-4">
            <div
                v-for="order in orders"
                :key="order.id"
                class="border rounded-lg p-4 shadow-sm"
            >
                <div class="flex justify-between items-center">
                    <p class="font-semibold">
                        Order #{{ order.id }} — <span class="text-blue-700">{{ order.status }}</span>
                    </p>

                    <!-- Cancel button -->
                    <button
                        v-if="canCancel(order)"
                        @click="cancelOrder(order.id)"
                        class="px-3 py-1 text-sm bg-red-500 text-white rounded hover:bg-red-600"
                    >
                        Cancel Order
                    </button>
                </div>

                <div class="border-t pt-3 space-y-2">
                    <p class="text-sm font-medium text-gray-700">Shipping Address: </p>
                    <p class="text-gray-600">{{order.address}}</p>
                    <div
                        v-for="item in order.items"
                        :key="item.id"
                        class="flex justify-between"
                    >
                        <span>{{ item.productName }} x {{ item.quantity }}</span>
                        <span class="font-semibold">P{{ item.snapshotPrice.toLocaleString() }}</span>
                    </div>
                </div>

                <div class="flex justify-between mt-2 pt-2 border-t font-bold">
                    <span>Total</span>
                    <span>P {{ order.totalAmount.toLocaleString() }}</span>
                </div>

                <!-- Created Date -->
                <p class="text-xs text-gray-500 mt-1">
                    Purchased: {{ order.createdAt }}
                </p>
            </div>

            <!-- Pagination -->
            <div class="flex justify-between mt-4">
                <button
                    @click="prevPage"
                    :disabled="page === 0"
                    class="px-3 py-1 border rounded hover:bg-gray-100 disabled:opacity-40"
                >
                    Prev
                </button>

                <span>Page {{ page + 1 }} of {{ totalPages }}</span>

                <button
                    @click="nextPage"
                    :disabled="page + 1 >= totalPages"
                    class="px-3 py-1 border rounded hover:bg-gray-100 disabled:opacity-40"
                >
                    Next
                </button>
            </div>
        </div>
    </div>
</template>
