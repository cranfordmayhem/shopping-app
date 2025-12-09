<script setup lang="ts">
    import { ref, onMounted } from 'vue'
    import { useRoute, useRouter } from 'vue-router'
    import { ProductService } from '@/api/services'
    import type { ProductResponse } from '@/types/dto'
    import { useCartStore } from '@/stores/cart'
    import NavbarMenu from '@/components/layout/Navbar.vue'

    const route = useRoute()
    const router = useRouter()
    const cart = useCartStore()

    const productId = Number(route.params.id)
    const product = ref<ProductResponse | null>(null)
    const loading = ref(true)
    const error = ref('')

    const loadProduct = async () => {
        try {
            const { data } = await ProductService.getProduct(productId)
            product.value = data
        } catch (err) {
            console.error("Failed to load product: ", err)
            error.value = "Product not found"
        } finally {
            loading.value = false
        }
    }

    const addToCart = () => {
        if (product.value && product.value.stock > 0) {
            cart.addToCart(product.value.id, 1)
        }
    }

    onMounted(loadProduct)

</script>
<template>
    <NavbarMenu />
    <div class="max-w-4xl mx-auto py-10 px-4">
        <button @click="router.back()" class="mb-4 text-blue-600 hover:underline">
            Back
        </button>

        <div v-if="loading" class="text-center py-10">Loading...</div>
        <div v-else-if="error" class="text-red-500">{{ error }}</div>
        <div v-else-if="product" class="grid grid-cols-1 md:grid-cols-2 gap-6 bg-white rounded-lg shadow p-6">
            <img
                :src="product.imageUrl"
                alt="Product Image"
                class="w-full h-80 object-cover rounded"
            />

            <div class="flex flex-col">
                <h2 class="text-2xl font-bold mb-2">{{ product.name }}</h2>
                <p class="text-gray-700 mb-4">{{ product.description }}</p>
                <p class="text-xl font-semibold mb-2">P {{ product.price.toLocaleString() }}</p>
                <p class="mb-4">
                    Stock: 
                    <span :class="product.stock > 0 ? 'text-green-600' : 'text-red-500'">
                        {{ product.stock > 0 ? product.stock : 'Sold Out' }}
                    </span>
                </p>

                <button
                    @click="addToCart"
                    :disabled="product.stock === 0"
                    class="mt-auto py-2 rounded text-white transition
                        bg-blue-600 hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed"
                >
                    Add to Cart
                </button>
            </div>
        </div>
    </div>
</template>