import Vue from 'vue'
import App from './App.vue'
import router from './router'
import VueCookies from 'vue-cookies'
import store from './store'
import { BootstrapVue, BootstrapVueIcons } from 'bootstrap-vue'
import { ToastPlugin } from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import JwPagination from 'jw-vue-pagination'
import VueSweetalert2 from 'vue-sweetalert2'
import 'sweetalert2/dist/sweetalert2.min.css'
import VueI18n from 'vue-i18n'

Vue.use(VueI18n);

import {messages} from 'vue-bootstrap-calendar';

window.i18n = new VueI18n({
  locale: 'en',
  messages
});
Vue.component('jw-pagination', JwPagination)
Vue.use(ToastPlugin)
Vue.use(VueCookies)
Vue.use(BootstrapVue)
Vue.use(BootstrapVueIcons)

const options = {
  confirmButtonColor: "orange",
  cancelButtonColor: "lightgray",
};

Kakao.init("d5e6e1f1140f3ded1483ff360cb5a153");
Vue.use(VueSweetalert2,options)
Vue.use(VueSweetalert2)

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  i18n,
  components: { App },
  template: '<App/>'
})
