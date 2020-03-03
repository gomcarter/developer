<template>
  <div class="login">
    <div class="login-box">
      <img src="@/assets/img/logo.png" alt="developer center" class="logo">
      <p class="logo_title">开发者中心</p>
      <div class="form">
        <el-form ref="forms" :model="forms" label-width="0" :rules="rules">
          <el-form-item prop="username">
            <el-input v-model="forms.username" placeholder="请输入账号" @keypress.enter.native="onSubmit"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="forms.password" type="password" placeholder="请输入密码" @keypress.enter.native="onSubmit"></el-input>
          </el-form-item>
          <el-form-item>
            <el-checkbox v-model="remember" style="float: left;">长期登录</el-checkbox>
          </el-form-item>
        </el-form>
        <el-row>
        </el-row>
        <el-button type="primary" @click="onSubmit" :disabled="disClick" :icon="disClick?'el-icon-loading':''">{{disClick ? '登录中' : '登录'}}</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { loginApi } from '@/config/api/inserv-api'
import { login } from '@/config/login'
import { getUrlHashParams } from '@/config/utils'

export default {
  data () {
    return {
      remember: false,
      forms: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      },
      disClick: false
    }
  },
  computed: {},
  methods: {
    onSubmit () {
      this.$refs.forms.validate((valid) => {
        if (valid) {
          this.disClick = true
          loginApi(this.forms).then((res) => {
            let expire = 7200
            if (this.remember) {
              // 设置长期登录，过期时间: 10年
              expire = 315360000
            }
            login(res, expire)
            this.disClick = false
            if (this.redirect) {
              this.$router.push(decodeURIComponent(this.redirect))
            } else {
              this.$router.push('/')
            }
          }).catch(() => {
            this.disClick = false
          })
        }
      })
    }
  },
  mounted () {
    this.redirect = (getUrlHashParams() || {}).redirect
  }
}
</script>

<style lang='scss' scoped>
  @import '@/assets/css/mixin.scss';
  @import 'index.scss';
</style>
