<script setup lang="ts">
    import { defineProps, defineEmits } from 'vue'
    import { useRouter } from 'vue-router'
    import type { CartStore } from '@/stores/cart'
    import { OrderService, ProductService } from '@/api/services'

    const props = defineProps({
        open: Boolean,
        cart: Object as () => CartStore
    })
    
    const router = useRouter()
    const emit = defineEmits(['update:open'])


    function closeCart() {
        emit('update:open', false)
    }

    // Optional: Increase/decrease quantity
    async function increase(item: any) {
        const { data: product } = await ProductService.getProduct(item.product.id)

        if(item.quantity + 1 <= product.stock) {
            props.cart.updateQuantity(item.product.id, item.quantity + 1)
        } else {
            alert('Reached maximum stock')
        }
    }

    function decrease(item: any) {
        if (item.quantity > 1) {
            props.cart.updateQuantity(item.product.id, item.quantity - 1)
        }
    }

    function remove(item: any) {
        props.cart.removeFromCart(item.product.id)
    }


    const checkOut = async () => {
        if(props.cart.items.length === 0) return
        try {
            await OrderService.createOrder()
            props.cart.clearCart()
            closeCart()
            window.location.href = "account/purchase"
        } catch (err) {
            console.error("Failed to add orders: ", err)
        }
    }
</script>

<template>
  <transition name="slide">
    <div
      v-if="open"
      class="fixed top-0 right-0 w-96 h-full bg-white shadow-lg flex flex-col z-50"
    >
      <!-- Header -->
      <div class="flex justify-between items-center p-4 border-b">
        <h2 class="text-lg font-bold">Your Cart</h2>
        <button @click="closeCart" class="text-gray-500 hover:text-gray-700">✕</button>
      </div>

      <!-- Items -->
      <div class="flex-1 overflow-y-auto p-4 space-y-4">
        <div v-if="cart.items.length === 0" class="text-gray-500">
          Cart is empty
        </div>

        <div v-else>
          <div
            v-for="item in cart.items"
            :key="item.product.id"
            class="flex justify-between items-center border-b pb-2"
          >
            <div class="flex-1">
              <p class="font-semibold">{{ item.product.name }}</p>
              <p class="text-gray-500 text-sm">P {{ item.product.price.toLocaleString() }} each</p>
            </div>
            <div class="flex items-center gap-2">
              <button @click="decrease(item)" class="px-2 py-1 bg-gray-200 rounded hover:bg-gray-300">-</button>
              <span>{{ item.quantity }}</span>
              <button @click="increase(item)" class="px-2 py-1 bg-gray-200 rounded hover:bg-gray-300">+</button>
            </div>
            <button @click="remove(item)" class="ml-2 text-red-500 hover:text-red-700 font-bold">✕</button>
          </div> 
        </div>
      </div>

      <!-- Footer / Total -->
      <div class="p-4 border-t">
        <div class="flex justify-between font-bold mb-4">
          <span>Total:</span>
          <span>P{{ cart.totalAmount.toLocaleString() }}</span>
        </div>
        <button @click="checkOut" class="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700">
          Checkout
        </button>
      </div>
    </div>
  </transition>
</template>

<style scoped>
/* Slide-in animation for cart sidebar */
.slide-enter-active, .slide-leave-active {
  transition: transform 0.3s ease;
}
.slide-enter-from {
  transform: translateX(100%);
}
.slide-enter-to {
  transform: translateX(0);
}
.slide-leave-from {
  transform: translateX(0);
}
.slide-leave-to {
  transform: translateX(100%);
}
</style>
