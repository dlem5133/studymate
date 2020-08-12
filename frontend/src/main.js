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
