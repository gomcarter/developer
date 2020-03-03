<template>
  <div>
    <h4 class="title">{{title}}</h4>
    <hr/>
    <el-form :model="form" ref="edit" label-width="8em">
      <el-form-item label="账号" :rules="[{ required: true, message: '请输入账户', trigger: ['blur', 'change'] }]" prop="username">
        <el-input v-model="form.username" placeholder="请输入账户，长度不超过200个字符，否则数据库会报错" :disabled="id" ></el-input>
      </el-form-item>
      <el-form-item v-if="id" label="密码">
        <el-input v-model="form.password" placeholder="不输入此项表示不修改"></el-input>
      </el-form-item>
      <el-form-item v-else label="密码" :rules="[{ required: true, message: '请输入密码', trigger: ['blur', 'change'] }]" prop="password">
        <el-input v-model="form.password" placeholder="请输入密码，长度随便设，只要你记得住"></el-input>
      </el-form-item>
      <el-form-item label="姓名" >
        <el-input v-model="form.name" placeholder="请输入姓名，长度不超过200个字符，否则数据库会报错"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" >
        <el-input v-model="form.mail" placeholder="请输入邮箱，长度不超过200个字符，否则数据库会报错"></el-input>
      </el-form-item>
      <el-form-item label="联系电话" >
        <el-input v-model="form.cellphone" placeholder="请输入联系电话，长度不超过20个字符，否则数据库会报错"></el-input>
      </el-form-item>
      <el-form-item >
        <el-button type="primary" @click="submit" :disabled="disabled" :icon="disabled?'el-icon-loading':'el-icon-success'" >提交</el-button>
        <el-button type="info" @click="$router.go(-1)" icon="el-icon-back">返回</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {addUserApi, getUserApi, updateUserApi} from '@/config/api/inserv-api'

export default {
  data () {
    return {
      disabled: true,
      title: '新建用户',
      id: null,
      form: {
        username: null,
        password: null,
        name: null,
        mail: null,
        cellphone: null
      }
    }
  },
  computed: {},
  methods: {
    init () {
      this.id = this.$route.params.id
      if (this.id) {
        this.title = '编辑用户'
        this.edit = true
        getUserApi(this.$route.params.id).then((res) => {
          this.form = res
          this.disabled = false
        }).catch(() => {
        })
      } else {
        this.disabled = false
      }
    },
    submit () {
      this.$refs.edit.validate((valid) => {
        if (valid) {
          this.$confirm('确定保存？', '提示', {type: 'info'}).then(() => {
            this.disabled = true
            if (this.id) {
              updateUserApi(this.id, this.form).then((res) => {
                this.disabled = false
                this.$transfer({
                  back: '返回编辑',
                  buttons: [{
                    text: '去列表',
                    link: '/system/user'
                  }]
                })
              }).catch(() => {
                this.disabled = false
              })
            } else {
              addUserApi(this.form).then((res) => {
                this.disabled = false
                this.$transfer({
                  back: '继续添加',
                  buttons: [{
                    text: '去列表',
                    link: '/system/user'
                  }]
                })
              }).catch(() => {
                this.disabled = false
              })
            }
          }).catch(() => {
          })
        }
      })
    }
  },
  components: {
  },
  mounted () {
    this.init()
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style type="text/css" lang="scss" scoped>
  @import 'index.scss';
</style>
