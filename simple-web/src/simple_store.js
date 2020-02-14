import Vue from "vue";
import axios from "axios";

export const store = Vue.observable({
    currentUser: null
});

export const mutations = {
    async loadCurrentUser() {
        return new Promise((resolve) => {
            axios.get('/auth/me')
                .then(res => {
                    store.currentUser = res.data;
                })
                .finally(() => {
                    return resolve();
                });
        });
    },
    clearCurrentUser() {
        store.currentUser = null;
    }
};
