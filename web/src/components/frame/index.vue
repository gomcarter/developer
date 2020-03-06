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
            <el-dropdown-item>
              <div @click="forgetPassword">修改密码</div>
            </el-dropdown-item>
            <el-dropdown-item>
              <div @click="doLogout">退出登录</div>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <v-dialog ref="forgetDialog" title="修改密码" :ok="savePassword">
      <el-form slot="body" :model="forget" ref="forgetForm" label-width="6em" >
        <el-form-item label="原密码" prop="oldPassword" :rules="[{ required: true, message: '请输入原密码', trigger: ['blur', 'change'] }]">
          <el-input v-model="forget.oldPassword"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword" :rules="[
                      { required: true, message: '请输入新密码', trigger: ['blur', 'change'] }
                      ]">
          <el-input v-model="forget.newPassword" placeholder="请输入新密码，长度随便设，只要你记得住"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword" :rules="[
                      { required: true, message: '再输入一次新密码', trigger: ['blur', 'change'] },
                      { validator: (rule, value, callback) => callback(value !== forget.newPassword ? '两次密码输入不一致，请重新输入' : undefined), trigger: ['blur', 'change']}
                      ]">
          <el-input v-model="forget.confirmPassword" placeholder="再输入一次新密码，要和新密码一样哟"></el-input>
        </el-form-item>
      </el-form>
    </v-dialog>
  </div>
</template>
<script>
import Logo from '@/assets/img/qa.png'
import { getPictureUrl } from '@/config/utils'
import { logout, user } from '@/config/login'
import { getUserSettingApi, updatePasswordApi } from '@/config/api/inserv-api'

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
      toRunSet: [],
      remote: null,
      notice: '当前已开启外部登录，修改密码功能被禁用！',
      forget: {
        oldPassword: null,
        newPassword: null,
        confirmPassword: null
      }
    }
  },
  computed: {},
  methods: {
    doLogout () {
      this.$confirm('确定要退出吗？', '提示', {type: 'warning'}).then(() => {
        // this.stopBadge()
        logout()
        this.$router.push('/login')
      })
    },
    forgetPassword () {
      if (this.remote == null) {
        getUserSettingApi()
          .then((d) => {
            this.remote = d.remote || false
            if (this.remote) {
              this.$alert(this.notice, '提示', {type: 'error'})
            } else {
              this.$refs.forgetDialog.open()
            }
          })
          .catch(() => {
            this.$alert('读取配置数据失败！', '提示', {type: 'error'})
          })
      } else if (this.remote) {
        this.$alert(this.notice, '提示', {type: 'error'})
      } else {
        this.$refs.forgetDialog.open()
      }
    },
    savePassword () {
      this.$refs.forgetForm.validate((valid) => {
        if (valid) {
          updatePasswordApi(this.forget.oldPassword, this.forget.newPassword)
            .then(() => {
              this.forget.oldPassword = null
              this.forget.newPassword = null
              this.forget.confirmPassword = null

              this.$success('密码修改成功！')
              this.$refs.forgetDialog.close()
            })
        }
      })
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
            {link: '/consumer/list', name: '前端项目列表', selected: false},
            {link: '/provider/list', name: '后端服务列表', selected: false},
            {link: '/interfaces/list', name: '接口列表', selected: false},
            {link: '/package/list', name: '打包列表', selected: false}
          ]
        },
        {
          name: '接口自动化测试',
          open: false,
          subItems: [
            {link: '/flow/function', name: '自定义函数', selected: false},
            {link: '/flow/testCase', name: '用例列表', selected: false}
          ]
        },
        {
          name: '系统管理',
          open: false,
          subItems: [
            {link: '/system/user', name: '用户管理', selected: false}
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
