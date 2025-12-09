<script setup lang="ts">
    import { ref, onMounted } from 'vue'
    import { useRouter } from 'vue-router'
    import { useCartStore } from '@/stores/cart'
    import Cart from '@/components/layout/Cart.vue'
    import NavbarMenu from '@/components/layout/Navbar.vue'
    import { ProductService } from '@/api/services'
    import type { ProductResponse } from '@/types/dto'

    const cart = useCartStore()
    const router = useRouter()
    const products = ref<ProductResponse[]>([])
    const page = ref(0)
    const size = ref(10)
    const totalPages = ref(0)

    function addProduct(product: ProductResponse) {
        const cartItem = cart.items.find(i => i.product.id === product.id)
        const quantityInCart = cartItem ? cartItem.quantity : 0
        if(quantityInCart < product.stock){
            cart.addToCart(product.id, 1)
        } else {
            alert('Reached maximum stock available')
        }
    }

    const loadProducts = async () => {
        try {
            const { data } = await ProductService.getProducts(page.value, size.value)
            products.value = data.content
            totalPages.value = data.totalPages
        } catch (err) {
            console.error('Failed to load products', err)
        }
    }

    function goToProduct(productId: number) {
        router.push(`/product/${productId}`)
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

    onMounted(loadProducts)
</script>

<template>
    <div class="min-h-screen bg-gray-100">
        <NavbarMenu />


        <main class="max-w-6xl mx-auto py-10 px-4">
            <h2 class="text-2xl font-bold mb-6">Products</h2>

            <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
                <div
                    v-for="product in products"
                    :key="product.id"
                    class="bg-white rounded-lg shadow p-4 flex flex-col"
                >
                    <img
                        :src="product.imageUrl"
                        alt="Product Image"
                        class="h-40 object-cover rounded mb-4 cursor-pointer"
                        @click="goToProduct(product.id)"
                    />
                    <h3 class="text-lg font-semibold">{{ product.name }}</h3>
                    <p class="text-gray-600 mt-1">P{{ product.price.toLocaleString() }}</p>
                    <p v-if="product.stock === 0" class="text-red-500 mt-1 font-semibold">Sold Out</p>
                    <button 
                        @click="addProduct(product)" 
                        :disabled="product.stock === 0"
                        class="mt-auto py-2 rounded text-white transition bg-blue-600 hover:bg-blue-700 
                            disabled:bg-gray-400 disabled:cursor-not-allowed"
                    >
                        Add to Cart
                    </button>
                </div>
            </div>

            <!-- Pagination Controls -->
            <div class="flex justify-between mt-6 items-center">
                <button
                    @click="prevPage"
                    :disabled="page === 0"
                    class="px-4 py-2 border rounded hover:bg-gray-100 disabled:opacity-50"
                >
                    Prev
                </button>
                <span>Page {{ page + 1 }} of {{ totalPages }}</span>
                <button
                    @click="nextPage"
                    :disabled="page + 1 >= totalPages"
                    class="px-4 py-2 border rounded hover:bg-gray-100 disabled:opacity-50"
                >
                    Next
                </button>
            </div>
        </main>
    </div>
</template>
