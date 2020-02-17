import axios from 'axios';
import qs from 'qs';

export default function (Vue) {
    Vue.auth = {
        login(email, password) {
            const params = {email, password};

            return new Promise((resolve, reject) => {
                axios.post(
                    '/auth/login',
                    qs.stringify(params),
                ).then(res => {
                    if (res.data.token !== undefined) {
                        localStorage.setItem('token', res.data.token)
                    }

                    return resolve(res);
                }).catch(err => {
                    reject(err);
                })
            });
        },

        async check() {
            let token = await this.getToken();

            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

            return !!token;
        },

        getToken() {
            let token = localStorage.getItem('token');

            if (!token) {
                return null;
            }
            return token;
        },

        logout() {
            localStorage.removeItem('token');
        }
    };

    Object.defineProperties(Vue.prototype, {
        $AuthPlugin: {
            get() {
                return Vue.auth;
            }
        }
    });
}
