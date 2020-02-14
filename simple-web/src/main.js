import Vue from 'vue'
import App from './App.vue'
import axios from 'axios';
import router from './router'
import {BootstrapVue, IconsPlugin} from 'bootstrap-vue'

import AuthPlugin from './plugin/auth';

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.config.productionTip = false;

Vue.use(BootstrapVue);
Vue.use(IconsPlugin);

Vue.use(AuthPlugin);

require('./config/router_guard');
require('./config/interceptors');

axios.defaults.baseURL = process.env.VUE_APP_SERVER_URL;

new Vue({
    router,
    render: h => h(App)
}).$mount('#app');
