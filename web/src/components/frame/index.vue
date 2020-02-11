<template>
  <div>
    <div class="header">
      <a href="">
        <img src="@/assets/img/logo.png" class="logo"/>
      </a>
      <el-menu :default-active="activeIndex" class="menu"
               mode="horizontal"
               background-color="#4063ff"
               text-color="#fff"
               active-text-color="gold">
        <template v-for="(menu, index) of items">
          <el-submenu v-if="menu.subItems && menu.subItems.length > 0" :index="index + ''" :key="index">
            <template slot="title">{{menu.name}}</template>
            <el-menu-item v-if="menu.subItems && menu.subItems.length > 0"
                          v-for="(child, j) of menu.subItems" :index="index + '_' + j" :key="index + '_' + j" >
              <router-link :to="child.link" class="link">
                {{child.name}}
              </router-link>
            </el-menu-item>
          </el-submenu>
          <el-menu-item v-else :index="index + ''" :key="index">
            <router-link :to="menu.link" class="link">
              {{menu.name}}
            </router-link>
          </el-menu-item>
        </template>
      </el-menu>
      <div class="user">
<!--        <img style="border-radius: 50%;" class="logo" :src="portrait ? getPictureUrl(portrait) :Logo"/>-->
        <el-dropdown>
          <span class="el-dropdown-link" style="color: white;cursor: pointer">
            欢迎，{{username}}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
<!--            <el-dropdown-item @click="forgetPassword">修改密码</el-dropdown-item>-->
            <el-dropdown-item @click="doLogout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <v-dialog ref="forget" title="修改密码">
      <el-form slot="body">
        <el-form-item label="当前密码:" label-width="6em">
          <el-input></el-input>
        </el-form-item>
        <el-form-item label="修改密码:" label-width="6em">
          <el-input></el-input>
        </el-form-item>
        <el-form-item label="确认密码:" label-width="6em">
          <el-input></el-input>
        </el-form-item>
      </el-form>
    </v-dialog>
  </div>
</template>
<script>
import Logo from '@/assets/img/qa.png'
import {getPictureUrl} from '@/config/utils'
import {logout, user} from '@/config/login'

export default {
  name: 'frame',
  data () {
    return {
      activeIndex: '1',
      getPictureUrl,
      Logo,
      username: user() || 'nobody',
      portrait: null,
      rule: [],
      items: [],
      timer: null,
      toRunSet: []
    }
  },
  computed: {},
  methods: {
    // 阻止冒泡
    // stopproPagation () {
    //   try {
    //     window.event.cancelBubble = true
    //   } catch (e) {
    //   }
    // },
    doLogout () {
      this.$confirm('确定要退出吗？', '提示', {type: 'warning'}).then(() => {
        // this.stopBadge()
        logout()
        this.$router.push('/login')
      })
    },
    forgetPassword () {
      this.$refs.forget.open()
    },
    initMenu () {
      this.items = [
        {
          name: '首页',
          link: '/',
          selected: false
        },
        {
          name: '接口管理',
          open: false,
          subItems: [
            {link: '/project/list', name: '前端项目', selected: false},
            {link: '/module/list', name: 'JAVA项目', selected: false},
            {link: '/manage/list', name: '接口列表', selected: false}
          ]
        },
        {
          name: '接口自动化测试',
          open: false,
          subItems: [
            {link: '/flow/function', name: '自定义函数', selected: false},
            {link: '/flow/testCase', name: '用例列表', selected: false}
          ]
        }
        // {
        //   name: '接口测试',
        //   link: '/test',
        //   selected: false
        // }
      ]
      this.selectMenuByRoute()
    },
    selectMenuByRoute () {
      setTimeout(() => {
        let path = this.$route.path
        this.items.forEach((o, index) => {
          if (o.subItems) {
            o.subItems.forEach((s, j) => {
              if (path.indexOf(s.link) === 0) {
                this.activeIndex = index + '_' + j
              }
            })
          } else if (path.indexOf(o.link) === 0) {
            this.activeIndex = index + ''
          }
        })
      }, 100)
    }
  },
  mounted () {
    this.initMenu()
  },
  components: {
    'v-dialog': () => import('@/components/dialog')
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style type="text/css" lang="scss" scoped>
  @import '../../assets/css/mixin.scss';
  @import 'index.scss';
</style>
