import Vue from 'vue'
import Router from 'vue-router'
import RegionalLinkage from '@/components/RegionalLinkage'

Vue.use(Router)

export default new Router({
  mode: 'history', // 后端支持可开
  routes: [
    {
      path: '/',
      name: 'RegionalLinkage',
      component: RegionalLinkage
    }
  ]
})
