<script setup lang="ts">
    import { useAuthStore } from '@/stores/auth'
    import { ref, onMounted } from 'vue'
    import { ProductService } from '@/api/services'
    import type { ProductResponse } from '@/types/dto'
    import { useRouter } from 'vue-router'
    import Navbar from '@/components/layout/Navbar.vue'

    const router = useRouter()

    const products = ref<ProductResponse[]>([])
    const page = ref(0)
    const size = ref(10)
    const totalPages = ref(0)

    const loadProducts = async () => {
        try {
            const { data } = await ProductService.getProducts(page.value, size.value)
            products.value = data.content
            totalPages.value = data.totalPages
        } catch (err) {
            console.error('Failed to load Products: ', err)
        }
    }

    const prevPage = () => {
        if (page.value > 0) {
            page.value--
            loadProducts()
        }
    }

    const nextPage = () => {
        if (page.value + 1 < totalPages.value) {
            page.value++
            loadProducts()
        }
    }

    const goToAddProduct = () => {
        router.push('/admin/products/add')
    }

    const editProduct = (id: number) => {
        router.push(`/admin/product/${id}`)
    }

    const deleteProduct = async (id: number) => {
        try {
            await ProductService.deleteProduct(id)
            alert('Product deleted successfully')
            await loadProducts()
        } catch (err) {
            console.error('Failed to delete product: ', err)
        }
    }

    onMounted(loadProducts)
</script>
<template>
    <Navbar />
    <div class="p-6">
        <h1 class="text-2xl font-bold mb-4">Admin Dashboard - Product</h1>
        <div class="flex justify-start gap-4 mt-4">
            <button @click="goToAddProduct" class="mb-4 px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700">
            Add New Product
            </button>

            <button @click="$router.push('/admin/orders')" class="mb-4 px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">
                Manage Orders
            </button>

        </div>
        <div class="overflow-x-auto">
            <table class="min-w-full border">
                <thead>
                    <tr class="bg-gray-100">
                        <th class="px-4 py-2">ID</th>
                        <th class="px-4 py-2">Name</th>
                        <th class="px-4 py-2">Price</th>
                        <th class="px-4 py-2">Stock</th>
                        <th class="px-4 py-2">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="product in products" :key="product.id" class="text-center">
                        <td class="px-4 py-2 border">{{ product.id }}</td>
                        <td class="px-4 py-2 border">{{ product.name }}</td>
                        <td class="px-4 py-2 border">{{ product.price.toLocaleString() }}</td>
                        <td class="px-4 py-2 border">{{ product.stock }}</td>
                        <td class="px-4 py-2 border flex justify-center gap-2">
                            <button @click="editProduct(product.id)" class="px-4 py-1 bg-blue-600 text-white rounded hover:bg-blue-700">
                                Edit
                            </button>
                            <button @click="deleteProduct(product.id)" class="px-4 py-1 bg-red-600 text-white rounded hover:bg-red-700">
                                Delete
                            </button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div class="flex justify-between mt-4 items-center">
            <button @click="prevPage" :disabled="page === 0" class="px-4 py-2 border rounded hover:bg-gray-100 disabled:opacity-50">
                Prev
            </button>
            <span>Page {{ page + 1 }} of {{ totalPages }}</span>
            <button @click="nextPage" :disabled="page + 1 >= totalPages" class="px-4 py-2 border rounded hover:bg-gray-100 disabled:opacity-50">
                Next
            </button>
        </div>
    </div>
</template>