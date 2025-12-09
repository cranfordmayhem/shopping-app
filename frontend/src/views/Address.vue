<script setup lang="ts">
    import { ref, onMounted, computed } from 'vue'
    import type { UserAddressRequest, UserAddressResponse } from '@/types/dto'
    import { useAuthStore } from '@/stores/auth'
    import { UserService } from '@/api/services'

    const page = ref(0)
    const size = ref(5)
    const totalPages = ref(0)

    const showAddForm = ref(false)
    const formAddress = ref<UserAddressRequest | null>(null)
    const addresses = ref<UserAddressResponse[]>([])

    const openAddForm = () => {
        formAddress.value = {
            city: '',
            state: '',
            zipCode: '',
            country: '',
            contactNumber: ''
        }
        showAddForm.value = true
    }

    const openEditForm = (address: UserAddressResponse) => {
        formAddress.value = { ...address }
        showAddForm.value = true
    }

    const saveAddress = async () => {
        if(!formAddress.value) return
        try {
            if(formAddress.value.id) {
                const payload: UserAddressRequest = {
                    city: formAddress.value.city,
                    state: formAddress.value.state,
                    zipCode: formAddress.value.zipCode,
                    country: formAddress.value.country,
                    contactNumber: formAddress.value.contactNumber
                }
                console.log(payload)
                await UserService.updateAddress(formAddress.value.id, payload)
                window.alert("Address updated successfully")
            } else {
                await UserService.createAddress(formAddress.value)
                window.alert("Address added successfully")
            }

            showAddForm.value = false
            formAddress.value = null

            const { data } = await UserService.getAddresses(page.value, size.value)
            addresses.value = data.content
            totalPages.value = data.totalPages
            page.value = data.number
        } catch (err) {
            console.error("Failed to update address: ", err)
        }
    }

    const setDefault = async (id: number) => {
        try {
            await UserService.setDefaultAddress(id)

            const { data } = await UserService.getAddresses(page.value, size.value)
            addresses.value = data.content
            totalPages.value = data.totalPages
            page.value = data.number

            window.alert("Default address has been changed successfully")
        } catch (err) {
            console.error("Failed to set default address", err)
        }
    }

    const deleteAddress = async (id: number) => {
        try {
            const confirmDelete = window.confirm("Are you sure you want to delete this address?")
            if (!confirmDelete) return

            await UserService.deleteAddress(id)

            const { data } = await UserService.getAddresses(page.value, size.value)
            addresses.value = data.content
            totalPages.value = data.totalPages
            page.value = data.number

            window.alert("Address deleted successfully")
        } catch (err) {
            console.error("Failed to delete address: ", err)
        }
    }

    const sortedAddresses = computed(() => {
      return [...addresses.value].sort((a, b) => (b.isDefault ? 1 : 0) - (a.isDefault ? 1 : 0));
    });

    onMounted(async() => {
        try {
            const { data } = await UserService.getAddresses(page.value, size.value)
            addresses.value = data.content
            totalPages.value = data.totalPages
            page.value = data.number
        } catch (err) {
            console.error("Failed to load addresses: ", err)
        }
    })
</script>

<template>
    <div class="p-6">
        <h2 class="text-xl font-semibold mb-4">My Addresses</h2>

        <div class="space-y-4">
            <div
            v-for="a in sortedAddresses"
            :key="a.id"
            class="address-card p-4 border rounded mb-2 flex justify-between items-center"
            >
                <div>
                    <p class="font-semibold">{{ a.contactNumber }}</p>
                    <p class="text-gray-600">{{a.city}}, {{a.state}}, {{a.country}} - {{a.zipCode}}</p>
                    <p v-if="a.isDefault" class="text-green-600 font-bold">Default</p>
                </div>
                <div class="flex gap-4">
                    <button
                        v-if="!a.isDefault"
                        @click="setDefault(a.id)"
                        class="btn btn-primary"
                    >
                        Set Default
                    </button>
                    <button @click="openEditForm(a)" class="text-blue-600 hover:underline">
                        Edit
                    </button>
                        <button @click="deleteAddress(a.id)" class="text-red-600 hover:underline">
                        Delete
                    </button>
                </div>
            </div>
            <button
                @click="openAddForm"
                class="mt-4 bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700"
            >
                Add New Address
            </button>

            <div v-if="showAddForm" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
                <div class="bg-white p-6 rounded-lg w-96">
                    <h3 class="text-lg font-semibold mb-4">{{ formAddress?.id? 'Edit Address' : 'Add Address' }}</h3>

                    <div class="space-y-3" v-if="formAddress">
                        <input v-model="formAddress.city" placeholder="City" class="w-full border rounded px-3 py-2" />
                        <input v-model="formAddress.state" placeholder="State" class="w-full border rounded px-3 py-2" />
                        <input v-model="formAddress.zipCode" placeholder="Zip" class="w-full border rounded px-3 py-2" />
                        <input v-model="formAddress.country" placeholder="Country" class="w-full border rounded px-3 py-2" />
                        <input v-model="formAddress.contactNumber" placeholder="Contact Number" class="w-full border rounded px-3 py-2" />
                    </div>

                    <div class="flex justify-end gap-2 mt-4">
                        <button @click="showAddForm = false" class="px-4 py-2 rounded border">Cancel</button>
                        <button @click="saveAddress" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">
                            Save
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
