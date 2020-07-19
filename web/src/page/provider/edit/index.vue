<template>
  <div>
    <h4 class="title">{{title}}</h4>
    <hr/>
    <el-form :model="form" ref="edit" label-width="8em" class="min_width">
      <el-form-item label="服务名称:" :rules="[{ required: true, message: '请输入模块名称', trigger: ['blur', 'change'] }]" prop="name">
        <el-input v-model="form.name" placeholder="请输入模块名称" />
      </el-form-item>
      <el-form-item v-for="(value, key) in ENV_DOMAIN_MAP"
                    :key="key"
                    :label="value + '域名:'"
                    :rules="[{ required: true, message: '请输入' + value + '域名', trigger: ['blur', 'change'] }]"
                    :prop="key">
        <el-input v-model="form[key]" :placeholder="'请输入' + value + '域名'" ></el-input>
      </el-form-item>
      <el-form-item >
        <el-button type="primary" @click="add" icon="el-icon-success">提交</el-button>
        <el-button type="info" @click="$router.go(-1)" icon="el-icon-back">返回</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { addJavaApi, getJavaApi, updateJavaApi } from '@/config/api/inserv-api'
import { ENV_DOMAIN_MAP } from '@/config/mapping'

export default {
  data () {
    return {
      title: '新增模块',
      form: {
        id: '',
        name: ''
      },
      ENV_DOMAIN_MAP
    }
  },
  computed: {},
  methods: {
    init () {
      if (this.$route.params.id) {
        getJavaApi(this.$route.params.id).then((res) => {
          this.form = res
        }).catch((err) => {
          console.log(err)
        })
      }
      this.title = this.$route.params.id ? '修改后端服务' : '新增后端服务'

      // 插入环境
      for (const key in ENV_DOMAIN_MAP) {
        this.$set(this.form, key, '')
      }
    },
    add () {
      this.$refs.edit.validate((valid) => {
        if (valid) {
          this.$confirm('确定保存？', '提示', {type: 'info'}).then(() => {
            if (this.$route.params.id) {
              updateJavaApi(this.form).then((res) => {
                this.$transfer({
                  back: '返回编辑',
                  buttons: [{
                    text: '去列表',
                    link: '/provider/list'
                  }]
                })
              }).catch((err) => {
                console.log(err)
              })
            } else {
              addJavaApi(this.form).then((res) => {
                this.$transfer({
                  back: '继续添加',
                  buttons: [{
                    text: '去列表',
                    link: '/provider/list'
                  }]
                })
              }).catch((err) => {
                console.log(err)
              })
            }
          })
        }
      })
    }
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
