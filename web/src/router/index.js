import Vue from 'vue'
import Router from 'vue-router'
import { isLogin } from '@/config/login'

Vue.use(Router)

const router = new Router({
  // mode: 'history',
  routes: [
    {
      path: '/index',
      name: 'index',
      component: r => require.ensure([], (require) => { r(require('@/page/index')) }, 'index')
    },
    {
      path: '/login',
      name: 'login',
      component: r => require.ensure([], (require) => { r(require('@/page/login')) }, 'login'),
      meta: {
        menu: false
      }
    },
    { // 接口管理
      path: '/interfaces/list',
      name: 'interfacesList',
      component: r => require.ensure([], (require) => { r(require('@/page/interfaces')) }, 'interfacesList')
    },
    { // 接口管理---详情
      path: '/interfaces/view/:id',
      name: 'interfacesView',
      component: r => require.ensure([], (require) => { r(require('@/page/interfaces/view')) }, 'interfacesView')
    },
    { // 接口管理---详情
      path: '/interfaces/edit',
      name: 'interfacesEdit',
      component: r => require.ensure([], (require) => { r(require('@/page/interfaces/edit')) }, 'interfacesEdit')
    },
    { // 公共接口详情
      path: '/public/:key',
      name: 'publicInterfacesDetail',
      component: r => require.ensure([], (require) => { r(require('@/page/interfaces/customer')) }, 'publicInterfacesDetail'),
      meta: {
        menu: false
      }
    },
    { // 接口管理
      path: '/package/list',
      name: 'packageList',
      component: r => require.ensure([], (require) => { r(require('@/page/package')) }, 'packageList')
    },
    { // 接口管理---打包详情
      path: '/package/view/:id',
      name: 'packageView',
      component: r => require.ensure([], (require) => { r(require('@/page/package/view')) }, 'packageView')
    },
    { // 项目管理
      path: '/consumer/list',
      name: 'consumerList',
      component: r => require.ensure([], (require) => { r(require('@/page/consumer/list')) }, 'consumerList')
    },
    { // 项目管理---编辑
      path: '/consumer/list/edit/:id',
      name: 'consumer',
      component: r => require.ensure([], (require) => { r(require('@/page/consumer/edit')) }, 'consumerListEdit')
    },
    { // 项目管理---新增
      path: '/consumer/list/edit',
      name: 'consumerListEdit',
      component: r => require.ensure([], (require) => { r(require('@/page/consumer/edit')) }, 'consumerListEdit')
    },
    { // 模块管理
      path: '/provider/list',
      name: 'providerList',
      component: r => require.ensure([], (require) => { r(require('@/page/provider/list')) }, 'providerList')
    },
    { // 模块管理---编辑
      path: '/provider/list/edit/:id',
      name: 'providerListEditId',
      component: r => require.ensure([], (require) => { r(require('@/page/provider/edit')) }, 'providerListEdit')
    },
    { // 模块管理---新增
      path: '/provider/list/edit',
      name: 'providerListEdit',
      component: r => require.ensure([], (require) => { r(require('@/page/provider/edit')) }, 'providerListEdit')
    },
    { // 接口测试
      path: '/test',
      name: 'test',
      component: r => require.ensure([], (require) => { r(require('@/page/test')) }, 'test')
    },
    { // 接口测试跳转
      path: '/test/:id/:env',
      name: 'testLink',
      component: r => require.ensure([], (require) => { r(require('@/page/test')) }, 'test')
    },
    { // 流程控制--- 自定义参数列表
      path: '/flow/function',
      name: 'function',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/function')) }, 'function')
    },
    { // 流程控制---自定义参数编辑
      path: '/flow/function/edit',
      name: 'functionAdd',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/function/edit')) }, 'functionAdd')
    },
    { // 流程控制---自定义参数编辑
      path: '/flow/function/edit/:id',
      name: 'functionEdit',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/function/edit')) }, 'functionEdit')
    },
    { // 流程控制---用例列表
      path: '/flow/testCase',
      name: 'testCase',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/testCase')) }, 'testCase')
    },
    { // 流程控制---用例列表-新增
      path: '/flow/testCase/edit',
      name: 'testCaseAdd',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/testCase/edit')) }, 'testCaseAdd')
    },
    { // 我的接口测试列表
      path: '/flow/testapi',
      name: 'testApi',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/testapi')) }, 'testApi')
    },
    { // 我的接口测试批量执行
      path: '/flow/testapi/run/:cusInterfacesId',
      name: 'testApiRun',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/testapi/run')) }, 'testApiRun')
    },
    { // 流程控制---用例列表-编辑
      path: '/flow/testCase/edit/:id',
      name: 'testCaseEdit',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/testCase/edit')) }, 'testCaseEdit')
    },
    { // 流程控制---用例列表-编辑
      path: '/flow/testCase/run/:id',
      name: 'testCaseRun',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/testCase/run')) }, 'testCaseRun')
    },
    { // 流程控制---用例执行结果列表
      path: '/flow/testCase/history',
      name: 'testCaseHistory',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/testCase/history')) }, 'testCaseHistory')
    },
    { // 流程控制---用例执行结果详情
      path: '/flow/testCase/history/:id',
      name: 'testCaseHistoryDetail',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/testCase/history/detail')) }, 'testCaseHistoryDetail')
    },
    { // 系统管理---用户列表
      path: '/system/user',
      name: 'user',
      component: r => require.ensure([], (require) => { r(require('@/page/system/user')) }, 'user')
    },
    { // 系统管理---用户列表-新增
      path: '/system/user/edit',
      name: 'userAdd',
      component: r => require.ensure([], (require) => { r(require('@/page/system/user/edit')) }, 'userAdd')
    },
    { // 系统管理---用户列表-编辑
      path: '/system/user/edit/:id',
      name: 'userEdit',
      component: r => require.ensure([], (require) => { r(require('@/page/system/user/edit')) }, 'userEdit')
    },
    // 中转
    {
      path: '/transfer/:data',
      name: 'transferData',
      component: r => require.ensure([], require => r(require('@/page/transfer')), 'transfer')
    },
    // 中转
    {
      path: '/',
      redirect: '/index'
    }
  ]
})

router.beforeEach((to, from, next) => {
  if (to.path.indexOf('/public') >= 0) {
    next()
  } else if (to.meta.menu !== false && to.path !== '/login' && !isLogin()) {
    next({ path: '/login?redirect=' + encodeURIComponent(to.path) })
  } else {
    next()
    // 刷新登录状态
    // refresh()
  }
})

export default router
