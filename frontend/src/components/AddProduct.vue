<script setup lang="ts">
    import { ref } from 'vue'
    import { useRouter } from 'vue-router'
    import { ProductService } from '@/api/services'
    import type { ProductRequest } from '@/api/types/dto'

    const router = useRouter()

    const name = ref('')
    const description = ref('')
    const price = ref<number | null>(null)
    const stock = ref<number | null>(null)
    const imageUrl = ref('')

    const submitProduct = async () => {
        if (!name.value || !description.value || price.value === null || stock.value === null || !imageUrl.value){
            alert('Please fill in all fields')
            return
        }

        const payload: ProductRequest = {
            name: name.value,
            description: description.value,
            price: price.value,
            stock: stock.value,
            imageUrl: imageUrl.value
        }

        try {
            await ProductService.createProduct(payload)
            alert('Product added successfully')
            router.push('/admin')
        } catch (err) {
            console.error('Failed to add product: ', err)
        }
    }

</script>
<template>
    <div class="max-w-xl mx-auto p-6 bg-white rounded shadow mt-10">
        <h2 class="text-2xl font-bold mb-6">Add New Product</h2>

        <div class="space-y-4">
            <div>
                <label class="block mb-1 font-semibold">Name</label>
                <input
                    v-model="name" type="text"
                    class="w-full border px-3 py-2 rounded"
                />
            </div>

            <div>
                <label class="block mb-1 font-semibold">Description</label>
                <input
                    v-model="description" type="text"
                    class="w-full border px-3 py-2 rounded"
                />
            </div>

            <div>
                <label class="block mb-1 font-semibold">Price</label>
                <input
                    v-model.number="price" type="number" min="0"
                    class="w-full border px-3 py-2 rounded"
                />
            </div>

            <div>
                <label class="block mb-1 font-semibold">Stock</label>
                <input
                    v-model.number="stock" type="number" min="0"
                    class="w-full border px-3 py-2 rounded"
                />
            </div>

            <div>
                <label class="block mb-1 font-semibold">image URL</label>
                <input
                    v-model="imageUrl" type="text"
                    class="w-full border px-3 py-2 rounded"
                />
            </div>

            <div class="flex justify-end gap-2">
                <button @click="router.push('/admin')" class="px-4 py-2 bg-gray-300 rounded hover:bg-gray">
                    Cancel
                </button>
                <button @click="submitProduct" class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700">
                    Add Product
                </button>
            </div>
        </div>
    </div>
</template>