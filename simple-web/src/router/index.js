import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter);

const routes = [
    {
        path: '/',
        redirect: '/login',
    },
    {
        path: '/home',
        meta: {
            requiresAuth: true
        },
        component: () => import('../views/Layout'),
        children: [
            {
                path: '/',
                component: () => import('../views/Home.vue')
            },
            {
                path: 'user',
                component: () => import('../views/User/Index'),
                children: [
                    {
                        path: '/',
                        component: () => import('../views/User/User')
                    },
                    {
                        path: 'add',
                        meta: {
                            role: ['admin']
                        },
                        component: () => import('../views/User/AddUser')
                    },
                    {
                        path: 'update/:id',
                        component: () => import('../views/User/UpdateUser')
                    }
                ]
            },
        ]
    },
    {
        path: '/login',
        component: () => import('../views/Login.vue')
    },
    {
        path: '/no-permission',
        component: () => import('../views/NoPermission')
    }
];

const router = new VueRouter({
    mode: 'history',
    scrollBehavior: (to, from, savedPosition) => {
        return savedPosition || {x: 0, y: 0}
    },
    routes
});

export default router
