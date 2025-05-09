import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/styles/index.css'

Vue.config.productionTip = false

// 使用ElementUI
Vue.use(ElementUI, {
  size: 'medium' // 设置组件默认尺寸
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
