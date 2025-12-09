<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useCartStore } from '@/stores/cart'
import { UserService } from '@/api/services'
import Cart from '@/components/layout/Cart.vue'
import type { UserProfileResponse } from '@/types/dto'

const router = useRouter()
const auth = useAuthStore()
const cart = useCartStore()
const profile = ref<UserProfileResponse>()

// Cart Sidebar
const cartOpen = ref(false)
function toggleCart() {
  cartOpen.value = !cartOpen.value
}

// Profile Dropdown
const dropdownOpen = ref(false)
function toggleDropdown() {
  dropdownOpen.value = !dropdownOpen.value
}
function closeDropdown() {
  dropdownOpen.value = false
}

// Load user profile
onMounted(async () => {
  try {
    const { data } = await UserService.getProfileViaLogin()
    profile.value = data
    // Load cart when navbar mounts
    await cart.loadCart()
  } catch (err) {
    console.error('Failed to load profile or cart', err)
  }
})

// Computed first letter of name
const userName = computed(() => profile.value?.firstName || 'User')

// Navigation functions
function goToAccount() {
  router.push('/account/profile')
  closeDropdown()
}
function goToPurchases() {
  router.push('/account/purchase')
  closeDropdown()
}
function handleLogout() {
  auth.logout()
  router.push('/login')
  closeDropdown()
}

</script>

<template>
  <nav class="bg-white shadow-md px-6 py-4 flex justify-between items-center w-full relative">
    <!-- Logo / Dashboard Link -->
    <div class="text-xl font-bold text-gray-800 cursor-pointer" @click="router.push('/')">
      Dashboard
    </div>

    <!-- Right Icons -->
    <div class="flex items-center gap-4">
        <!-- CART ICON (ONLY FOR USERS) -->
        <button v-if="auth.account?.role !== 'ADMIN'" @click="toggleCart" class="relative">
            🛒
            <span
            v-if="cart.items.length"
            class="absolute -top-2 -right-2 bg-red-500 text-white text-xs px-1 rounded-full"
            >
            {{ cart.items.length }}
            </span>
        </button>

        <!-- PROFILE / LOGOUT -->
        <div class="relative">
            <button
            @click="toggleDropdown"
            class="w-10 h-10 rounded-full bg-gray-200 flex items-center justify-center hover:bg-gray-300 transition"
            >
            <span class="text-gray-700 font-bold uppercase">{{ userName.charAt(0) }}</span>
            </button>

            <div
            v-show="dropdownOpen"
            @click.outside="closeDropdown"
            class="absolute right-0 mt-2 w-48 bg-white border rounded-lg shadow-lg py-2 z-50"
            >
            <!-- USER OPTIONS (not admin) -->
            <template v-if="auth.account?.role !== 'ADMIN'">
                <button
                @click="goToAccount"
                class="block w-full text-left px-4 py-2 text-gray-700 hover:bg-gray-100"
                >
                My Account
                </button>
                <button
                @click="goToPurchases"
                class="block w-full text-left px-4 py-2 text-gray-700 hover:bg-gray-100"
                >
                My Purchases
                </button>
            </template>

            <!-- LOGOUT (all users) -->
            <button
                @click="handleLogout"
                class="block w-full text-left px-4 py-2 text-red-500 hover:bg-gray-100"
            >
                Logout
            </button>
            </div>
        </div>
        </div>
  </nav>

  <!-- Cart Sidebar -->
  <Cart v-model:open="cartOpen" :cart="cart" />
</template>

<style scoped>
/* Smooth dropdown transition */
[v-cloak] > * { display: none; }
</style>
