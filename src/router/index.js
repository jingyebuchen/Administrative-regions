import Vue from 'vue'
import Router from 'vue-router'
import RegionalLinkage from '@/components/RegionalLinkage/RegionalLinkage'
import GridMap from '@/components/GridMap/GridMap'
import wgMap from '@/components/GridMap/wgMap'

Vue.use(Router)

export default new Router({
  mode: 'history', // 后端支持可开
  routes: [
    {
      path: '/',
      name: 'RegionalLinkage',
      component: RegionalLinkage
    },
    {
      path: '/gridmap',
      name: 'gridmap',
      component: GridMap
    },
    {
      path: '/wgmap',
      name: 'wgmap',
      component: wgMap
    }    
  ]
})
