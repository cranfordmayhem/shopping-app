<script setup lang="ts">
    import { ref, onMounted } from 'vue'
    import { useRouter } from 'vue-router'
    import { OrderService } from '@/api/services'
    import { OrderResponse } from '@/types/dto'

    const router = useRouter()
    const orders = ref<OrderResponse[]>([])
    const page = ref(0)
    const size = ref(10)
    const totalPages = ref(0)

    const loadOrders = async () => {
        try {
            const { data } = await OrderService.getAllOrders(page.value, size.value)
            orders.value = data.content
            totalPages.value = data.totalPages
        } catch (err) {
            console.error('Failed to load orders: ', err)
        }
    }

    const prevPage = () => {
        if (page.value > 0) {
            page.value--
            loadOrders()
        }
    }

    const nextPage = () => {
        if (page.value + 1 < totalPages.value) {
            page.value++
            loadOrders()
        }
    }

    const updateOrderStatus = async (orderId: number, status: string) => {
        try {
            await OrderService.updateStatus(orderId, status as OrderStatus)
            await loadOrders()
        } catch (err) {
            console.error('Failed to update order status: ', err)
        }
    }

    const goToDashboard = () => {
        router.push('/admin')
    }

    onMounted(loadOrders)
</script>
<template>
    <div class="p-6">
        <div class="flex justify-between items-center mb-4">
            <h1 class="text-2xl font-bold">Admin Dashboard - Orders</h1>
            <button
                @click="goToDashboard"
                class="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
            >
                Back to Dashboard
            </button>
        </div>
        <div class="overflow-x-auto">
            <table class="min-w-full border">
                <thead>
                    <tr class="bg-gray-100">
                        <th class="px-4 py-2">Order ID</th>
                        <th class="px-4 py-2">Customer</th>
                        <th class="px-4 py-2">Address</th>
                        <th class="px-4 py-2">Items</th>
                        <th class="px-4 py-2">Total Price</th>
                        <th class="px-4 py-2">Status</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="order in orders" :key="order.id" class="text-center">
                        <td class="px-4 py-2 border">{{ order.id }}</td>
                        <td class="px-4 py-2 border">{{ order.firstName }} {{ order.lastName }}</td>
                        <td class="px-4 py-2 border">{{ order.address }}</td>
                        <td class="px-4 py-2 border">
                            <ul class="text-left">
                                <li v-for="item in order.items" :key="item.productId">
                                    {{ item.productName }} x {{ item.quantity }}
                                </li>
                            </ul>
                        </td>
                        <td class="px-4 py-2 border">P {{ order.totalAmount.toLocaleString() }}</td>
                        <td class="px-4 py-2 border">
                            <select
                                v-model="order.status" @change="updateOrderStatus(order.id, order.status)"
                                class="border px-2 py-1 rounded"
                            >
                                <option :value="'PENDING'">PENDING</option>
                                <option :value="'PROCESSING'">PROCESSING</option>
                                <option :value="'SHIPPED'">SHIPPED</option>
                                <option :value="'DELIVERED'">DELIVERED</option>
                                <option :value="'CANCELLED'">CANCELLED</option>
                            </select>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="flex justify-between mt-4 items-center">
            <button
                @click="prevPage" :disabled="page === 0"
                class="px-4 py-2 border rounded hover:bg-gray-100 disabled:opacity-50"
            >
                Prev
            </button>
            <span>Page {{ page + 1 }} of {{ totalPages }}</span>
            <button
                @click="nextPage" :disabled="page + 1 >= totalPages"
                class="px-4 py-2 border rounded hover:bg-gray-100 disabled:opacity:50"
            >
                Next
            </button>
        </div>
    </div>
</template>