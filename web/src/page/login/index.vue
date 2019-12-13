<template>
  <div class="login">
    <div class="login-box">
      <img src="@/assets/img/login_logo.png" alt="" class="logo">
      <p class="logo_title">开发者中心</p>
      <div class="form">
        <el-form ref="forms" :model="forms" label-width="0" :rules="rules">
          <el-form-item prop="user">
            <el-input v-model="forms.user" placeholder="请输入账号" @keypress.enter.native="onSubmit"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="forms.password" type="password" placeholder="请输入密码" @keypress.enter.native="onSubmit"></el-input>
          </el-form-item>
        </el-form>
        <el-row>
        </el-row>
        <el-button type="primary" @click="onSubmit" :disabled="disClick">{{disClick ? '登录中' : '登录'}}</el-button>
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
      forms: {
        user: '',
        password: ''
      },
      rules: {
        user: [
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
    onSubmit () { // 提交
      this.$refs.forms.validate((valid) => {
        if (valid) {
          this.disClick = true
          loginApi(this.forms).then((res) => {
            // 设置过期时间 2小时
            login(res, 2)
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
