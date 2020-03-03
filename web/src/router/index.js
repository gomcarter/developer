import Vue from 'vue'
import Router from 'vue-router'
import { isLogin, refresh } from '@/config/login'

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
    { // 接口管理---详情
      path: '/customer/interfaces/:id',
      name: 'customerInterfaces',
      component: r => require.ensure([], (require) => { r(require('@/page/interfaces/customer')) }, 'customerInterfaces'),
      meta: {
        menu: false
      }
    },
    { // 项目管理
      path: '/project/list',
      name: 'projectList',
      component: r => require.ensure([], (require) => { r(require('@/page/project/list')) }, 'projectList')
    },
    { // 项目管理---编辑
      path: '/project/list/edit/:id',
      name: 'projectListeEitId',
      component: r => require.ensure([], (require) => { r(require('@/page/project/edit')) }, 'projectListeEit')
    },
    { // 项目管理---新增
      path: '/project/list/edit',
      name: 'projectListeEit',
      component: r => require.ensure([], (require) => { r(require('@/page/project/edit')) }, 'projectListeEit')
    },
    { // 模块管理
      path: '/module/list',
      name: 'moduleList',
      component: r => require.ensure([], (require) => { r(require('@/page/module/list')) }, 'moduleList')
    },
    { // 模块管理---编辑
      path: '/module/list/edit/:id',
      name: 'moduleListeEitId',
      component: r => require.ensure([], (require) => { r(require('@/page/module/edit')) }, 'moduleListeEit')
    },
    { // 模块管理---新增
      path: '/module/list/edit',
      name: 'moduleListeEit',
      component: r => require.ensure([], (require) => { r(require('@/page/module/edit')) }, 'moduleListeEit')
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
    { // 流程控制--- 自定义函数列表
      path: '/flow/function',
      name: 'function',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/function')) }, 'function')
    },
    { // 流程控制---自定义函数编辑
      path: '/flow/function/edit',
      name: 'functionAdd',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/function/edit')) }, 'functionAdd')
    },
    { // 流程控制---自定义函数编辑
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
    { // 流程控制---用例列表
      path: '/system/user',
      name: 'user',
      component: r => require.ensure([], (require) => { r(require('@/page/system/user')) }, 'user')
    },
    { // 流程控制---用例列表-新增
      path: '/system/user/edit',
      name: 'userAdd',
      component: r => require.ensure([], (require) => { r(require('@/page/system/user/edit')) }, 'userAdd')
    },
    { // 流程控制---用例列表-编辑
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
    {
      path: '/',
      redirect: '/index'
    }
  ]
})

router.beforeEach((to, from, next) => {
  if (to.path.indexOf('/interfaces/view') >= 0) {
    next()
  } else if (to.meta.menu !== false && to.path !== '/login' && !isLogin()) {
    next({ path: '/login?redirect=' + encodeURIComponent(to.path) })
  } else {
    next()
    // 刷新登录状态
    refresh()
  }
})

export default router
