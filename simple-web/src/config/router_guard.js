import Vue from 'vue';
import router from '../router';
import {store, mutations} from "../simple_store";

router.beforeEach(async (to, from, next) => {
    const requiredAuth = to.matched.some(record => record.meta?.requiresAuth);
    const isAuthenticated = await Vue.prototype.$AuthPlugin.check();

    if (requiredAuth && !isAuthenticated) {
        return next('/login');
    }

    //load current user if store doesnt have
    if (requiredAuth && isAuthenticated && !store.currentUser) {
        await mutations.loadCurrentUser();
    }

    //check whether user has permission to access next page
    const isRoleSet = to.matched.some(record => record.meta?.role);
    if (isRoleSet) {
        const hasPermission = to.matched.some(record => {
            if (record.meta?.role) {
                return record.meta?.role.includes(store.currentUser.role);
            }
        });

        if (!hasPermission) {
            return next('/no-permission');
        }
    }

    return next();
});

router.onReady(async (route) => {
    const requiredAuth = route.matched.some(record => record.meta.requiresAuth || false);
    const isAuthenticated = await Vue.prototype.$AuthPlugin.check();

    if (requiredAuth && !isAuthenticated && route.path !== '/login') {
        return router.push('/login');
    }

    if (isAuthenticated && route.path === '/login') {
        return router.push('/home');
    }
});
