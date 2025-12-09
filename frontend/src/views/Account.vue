<script setup lang="ts">
    import Navbar from '@/components/layout/Navbar.vue'
    import SidebarMenu from '@/components/layout/SidebarMenu.vue'
</script>

<template>
    <div class="min-h-screen bg-gray-100">
        <Navbar />
        <div class="max-w mx-20 m-5 mx-auto bg-white rounded-lg shadow-sm p-6 flex gap-6">
            <SidebarMenu />
            <div class="flex-1">
                <router-view />
            </div>
        </div>
    </div>
</template>