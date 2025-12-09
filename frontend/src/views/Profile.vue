<script setup lang="ts">
    import { ref, onMounted, computed } from 'vue'
    import type { UserProfileResponse, UserProfileRequest } from '@/types/dto'
    import { UserService } from '@/api/services'
    import { useAuthStore } from '@/stores/auth'

    const profile = ref<UserProfileResponse | null>(null)
    const profileUpdate = ref<UserProfileRequest | null>(null)

    onMounted(async() => {
        const { data } = await UserService.getProfileViaLogin()
        profile.value = data
    })
    async function saveProfile() {
        if (!profile.value) return

        const payload: UserProfileRequest = {
            firstName: profile.value.firstName,
            lastName: profile.value.lastName,
            age: Number(profile.value.age)
        }

        console.log(JSON.stringify(payload))

        try {
            const { data } = await UserService.updateProfile(profile.value.id, payload)
            profile.value = data
        } catch (err) {
            console.error('Update failed: ', err)
        }
    }
</script>

<template>
    <div v-if="profile" class="p-6">
        <h2 class="text-xl font-semibold mb-4">My Profile</h2>

        <div class="space-y-4 max-w-xl">
            <div>
                <label class="block text-gray-600">First Name</label>
                <input
                    v-model="profile.firstName" type="text"
                    class="mt-1 w-full border rounded-lg px-3 py-2"
                />
            </div>

            <div>
                <label class="block text-gray-600">Last Name</label>
                <input
                    v-model="profile.lastName" type="text"
                    class="mt-1 w-full border rounded-lg px-3 py-2"
                />
            </div>

            <div>
                <label class="block text-gray-600">Email</label>
                <input
                    v-model="profile.email" type="email"
                    class="mt-1 w-full border rounded-lg px-3 py-2"
                    disabled
                />
                <button class="mt-1 text-sm text-blue-600 hover:underline">Change</button>
            </div>

            <div>
                <label class="block text-gray-600">Age</label>
                <input
                    v-model.number="profile.age" type="number"
                    class="mt-1 w-full border rounded-lg px-2 py-2"
                />
            </div>

            <button
                @click="saveProfile"
                class="px-4 bg-blue-600 hover:bg-blue-700 text-white py-2 rounded-lg font-medium transition"
            >
                Save
            </button>
        </div>
    </div>
</template>