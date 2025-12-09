import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/auth'

import Dashboard from '@/views/Dashboard.vue';
import Login from '@/components/Login.vue';
import Register from '@/components/Register.vue';
import Account from '@/views/Account.vue';
import Profile from '@/views/Profile.vue';
import Address from '@/views/Address.vue';
import MyPurchase from '@/views/MyPurchase.vue';
import Product from '@/views/Product.vue';

import AdminDashboard from '@/views/admin/AdminDashboard.vue';
import AddProduct from '@/components/AddProduct.vue';
import EditProduct from '@/components/EditProduct.vue'
import AdminOrders from '@/views/admin/AdminOrders.vue';


const routes = [
    // ROOT redirect handling
    {
        path: '/',
        redirect: () => {
            const auth = useAuthStore();
            if (!auth.isAuthenticated) return '/login';
            return auth.user?.role === 'ADMIN' ? '/admin' : '/dashboard';
        }
    },

    // DASHBOARD (fixed typo)
    {
        path: '/dashboard',
        name: 'dashboard',
        component: Dashboard,
        meta: { requiresAuth: true }
    },

    // AUTH
    {
        path: '/login',
        name: 'login',
        component: Login
    },
    {
        path: '/register',
        name: 'register',
        component: Register
    },

    // PRODUCT DETAIL
    {
        path: '/product/:id',
        name: 'product',
        component: Product
    },

    // USER ACCOUNT ROUTES
    {
        path: '/account',
        name: 'account',
        component: Account,
        meta: { requiresAuth: true },
        children: [
            { path: "profile", component: Profile },
            { path: "addresses", component: Address },
            { path: "purchase", component: MyPurchase }
        ]
    },

    // ADMIN ROUTES
    {
        path: '/admin',
        name: "admin-dashboard",
        component: AdminDashboard,
        meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
        path: '/admin/product/:id',
        name: "admin-edit-product",
        component: EditProduct,
        meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
        path: '/admin/products/add',
        name: "admin-add-product",
        component: AddProduct,
        meta: { requiresAuth: true, requiresAdmin: true }
    },
    {
        path: '/admin/orders',
        name: "admin-orders",
        component: AdminOrders,
        meta: { requiresAuth: true, requiresAdmin: true }
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

router.beforeEach(async (to) => {
    const auth = useAuthStore();

    // restore session once
    if (!auth.initialized) {
        await auth.restoreSession();
    }

    // requires login
    if (to.meta.requiresAuth && !auth.isAuthenticated) {
        return '/login';
    }

    // logged-in users cannot visit login page
    if (to.name === 'login' && auth.isAuthenticated) {
        return auth.account?.role === 'ADMIN' ? '/admin' : '/dashboard';
    }

    // admin guard
    if (to.meta.requiresAdmin) {
        if (!auth.account) return '/login';              // no session yet
        if (auth.account?.role !== "ADMIN") return '/dashboard'; // user but not admin
    }
});

export { router };