// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import 'element-ui/lib/theme-chalk/index.css'
import ElementUI from 'element-ui'
import VueClipboard from 'vue-clipboard2'

import Vue from 'vue'
import App from './App'
import router from './router'
import { transfer, success } from './config/utils'
import VueQuillEditor from 'vue-quill-editor'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

Vue.config.productionTip = false
Vue.prototype.$transfer = transfer
Vue.prototype.$success = success

Vue.use(ElementUI)
Vue.use(VueClipboard)
Vue.use(VueQuillEditor)
/* eslint-disable no-new */
export const that = new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: { App }
})
