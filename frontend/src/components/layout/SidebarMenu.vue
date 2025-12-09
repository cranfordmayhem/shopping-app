<script setup lang="ts">
    import { useRoute, useRouter } from 'vue-router'

    const route = useRoute()
    const router = useRouter()

    const menuItems = [
        {label: "Profile", path: "/account/profile"},
        {label: "Addresses", path: "/account/addresses"},
        {label: "My Purchase", path: "/account/purchase"},
    ]

    function go(path: string) {
        router.push(path)
    }
</script>
<template>
    <aside class="w-64 border-r pr-4">
        <div class="mb-6">
            <h2 class="text-xl font-semibold ">My Account</h2>
            <p class="text-gray-500">Manage your account</p>
        </div>

        <ul class="space-y-1">
            <li
                v-for="item in menuItems"
                :key="item.path"
            >
                <button
                    @click="go(item.path)"
                    class="w-full text-left px-3 py-2 rounded-lg transition hover:bg-gray-100 font-medium"
                    :class="route.path === item.path ? 'text-blue-600 bg-blue-50' : 'text-gray-700'"
                >
                    {{item.label}}
                </button>
            </li>
        </ul>
    </aside>
</template>