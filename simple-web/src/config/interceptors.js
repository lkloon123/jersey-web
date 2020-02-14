import Vue from 'vue';
import axios from 'axios';
import router from "../router";
import {mutations} from "../simple_store";

axios.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response.status === 401 && router.currentRoute.path !== '/login') {
            alert('Your session has expired, please login again');
            mutations.clearCurrentUser();
            Vue.prototype.$AuthPlugin.logout();
            router.replace('/login');
        }
    }
);
