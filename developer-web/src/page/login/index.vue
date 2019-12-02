<template>
  <div class="login">
    <div class="login-box">
      <img src="@/assets/img/login_logo.png" alt="" class="logo">
      <p class="logo_title">亿安永道开发者中心</p>
      <div class="form">
        <el-form ref="forms" :model="forms" label-width="0" :rules="rules">
          <el-form-item prop="username">
            <el-input v-model="forms.username" placeholder="请输入账号" @keypress.enter.native="onSubmit"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="forms.password" type="password" placeholder="请输入密码" @keypress.enter.native="onSubmit"></el-input>
          </el-form-item>
          <el-form-item prop="checknumber">
            <el-col :span="16" gutter="10">
              <el-input v-model="forms.checknumber" placeholder="请输入验证码" maxlength="4" @keypress.enter.native="onSubmit">
              </el-input>
            </el-col>
            <el-col :span="8" class="validate">
              <img :src="codeImg" alt="" class="code-pic" width="116px" height="40px" @click="changeCode">
            </el-col>
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
import { BASE_URL, DOMAIN } from '@/config/api/env'
import { userLogin } from '@/config/api/base-api'
import { login } from '@/config/login'
import { getUrlHashParams } from '@/config/utils'

export default {
  data () {
    return {
      forms: {
        username: '',
        password: '',
        checknumber: ''
      },
      codeImg: `${BASE_URL}publics/checknumber.jpg?t=${new Date().getTime()}&domain=${DOMAIN}`,
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        checknumber: [
          { required: true, message: '请输入验证码', trigger: 'blur' }
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
          userLogin(this.forms).then((res) => {
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
    },
    changeCode () { // 验证码
      this.codeImg = `${BASE_URL}publics/checknumber.jpg?t=${new Date().getTime()}&domain=${DOMAIN}`
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
