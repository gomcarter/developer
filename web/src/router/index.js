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
      path: '/manage/list',
      name: 'manageList',
      component: r => require.ensure([], (require) => { r(require('@/page/manage')) }, 'manageList')
    },
    { // 接口管理---详情
      path: '/manage/list/view/:id',
      name: 'manageListView',
      component: r => require.ensure([], (require) => { r(require('@/page/manage/view')) }, 'manageListView')
    },
    { // 接口管理---详情
      path: '/customer/interfaces/:id',
      name: 'customerInterfaces',
      component: r => require.ensure([], (require) => { r(require('@/page/manage/customer')) }, 'customerInterfaces'),
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
    { // 流程控制---参数规则列表
      path: '/flow/param',
      name: 'flowParam',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/param')) }, 'flowParam')
    },
    { // 流程控制---参数规则列表-新增
      path: '/flow/param/add',
      name: 'flowParamAdd',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/param/add')) }, 'flowParamAdd')
    },
    { // 流程控制---参数规则列表-编辑
      path: '/flow/param/add/:id',
      name: 'flowParamAddId',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/param/add')) }, 'flowParamAddId')
    },
    { // 流程控制---用例列表
      path: '/flow/example',
      name: 'flowExample',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/example')) }, 'flowExample')
    },
    { // 流程控制---用例列表-新增
      path: '/flow/example/add',
      name: 'flowExampleAdd',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/example/add')) }, 'flowExampleAdd')
    },
    { // 流程控制---用例列表-编辑
      path: '/flow/example/add/:id',
      name: 'flowExampleAddId',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/example/add')) }, 'flowExampleAddId')
    },
    { // 流程控制---用例列表-详情-新增
      path: '/flow/testCaseItem/add/:fkTestCaseId',
      name: 'flowTestCaseItemAdd',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/testCaseItem/add/')) }, 'flowTestCaseItemAdd')
    },
    { // 流程控制---用例列表-详情-编辑
      path: '/flow/testCaseItem/add/:id/:fkTestCaseId',
      name: 'flowTestCaseItemAdd',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/testCaseItem/add/')) }, 'flowTestCaseItemAdd')
    },
    { // 流程控制---用例列表-详情
      path: '/flow/testCaseItem/:id',
      name: 'flowTestCaseItemId',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/testCaseItem')) }, 'flowTestCaseItemId')
    },
    { // 流程控制---用例列表-详情-新增（修改）-参数配置
      path: '/flow/testCaseItem/test/:id/:env',
      name: 'testCaseItemLink',
      component: r => require.ensure([], (require) => { r(require('@/page/flow/testCaseItem/test')) }, 'testCaseItemLink')
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
  if (to.path.indexOf('/manage/list') >= 0) {
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
