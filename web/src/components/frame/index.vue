<template>
  <div>
    <div class="harder">
      <a href="">
        <img src="@/assets/img/login_logo.png" class="logo"/>
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
        <img style="border-radius: 50%;" class="logo" :src="portrait ? getPictureUrl(portrait) :Logo"/>
<!--        <el-dropdown>-->
<!--          <span class="el-dropdown-link" style="color: white;cursor: pointer">-->
<!--            欢迎，{{username}}<i :style="{'visibility': portrait ? 'visible' :'hidden'}" class="el-icon-arrow-down el-icon&#45;&#45;right"></i>-->
<!--          </span>-->
<!--          <div style="visibility: hidden;">-->
<!--            <span class="el-icon-arrow-down el-icon&#45;&#45;right"></span>-->
<!--          </div>-->
<!--          <el-dropdown-menu slot="dropdown" v-if="portrait">-->
<!--            <el-dropdown-item @click="forgetPassword">修改密码</el-dropdown-item>-->
<!--            <el-dropdown-item @click="logout">退出登录</el-dropdown-item>-->
<!--          </el-dropdown-menu>-->
<!--        </el-dropdown>-->
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
import {logout, getUsername, getLogo} from '@/config/login'

export default {
  name: 'frame',
  data () {
    return {
      activeIndex: '1',
      getPictureUrl,
      Logo,
      username: getUsername() || 'nobody',
      portrait: getLogo(),
      rule: [],
      items: [],
      timer: null,
      toRunSet: []
    }
  },
  computed: {},
  methods: {
    // 阻止冒泡
    stopproPagation () {
      try {
        window.event.cancelBubble = true
      } catch (e) {
      }
    },
    logout () {
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
            {link: '/project/list', name: '项目列表', selected: false},
            {link: '/module/list', name: '模块列表', selected: false},
            {link: '/manage/list', name: '接口列表', selected: false}
          ]
        },
        {
          name: '流程控制',
          open: false,
          subItems: [
            {link: '/flow/param', name: '参数规则列表', selected: false},
            {link: '/flow/example', name: '用例列表', selected: false}
          ]
        },
        {
          name: '接口测试',
          link: '/test',
          selected: false
        },
        {
          name: '商品中心',
          selected: false,
          open: false,
          subItems: [
            {link: '/item/search', name: '商品搜索测试', selected: false}
          ]
        }
      ]
      this.selectMenuByRoute()
    },
    selectMenuByRoute () {
      setTimeout(() => {
        let path = this.$route.path
        this.items.forEach((o, index) => {
          if (o.subItems) {
            o.subItems.forEach((s, j) => {
              if (path.indexOf(s.link) >= 0) {
                this.activeIndex = index + '_' + j
              }
            })
          } else if (path.indexOf(o.link) >= 0) {
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
