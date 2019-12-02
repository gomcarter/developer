<template>
  <div>
    <h4 class="title">{{title}}</h4>
    <hr/>
    <el-form :model="form" ref="edit" label-width="8em">
      <el-form-item label="模块名称:" :rules="[{ required: true, message: '请输入模块名称', trigger: ['blur', 'change'] }]" prop="name">
        <el-input v-model="form.name" placeholder="请输入模块名称" />
      </el-form-item>
      <el-form-item label="开发环境域名:"  :rules="[{ required: true, message: '请输入开发环境域名', trigger: ['blur', 'change'] }]" prop="devDomain">
        <el-input v-model="form.devDomain" placeholder="请输入开发环境域名" />
      </el-form-item>
      <el-form-item label="测试环境域名:"  :rules="[{ required: true, message: '请输入测试环境域名', trigger: ['blur', 'change'] }]" prop="testDomain">
        <el-input v-model="form.testDomain" placeholder="请输入测试环境域名" />
      </el-form-item>
      <el-form-item label="预发环境域名:"  :rules="[{ required: true, message: '请输入预发环境域名', trigger: ['blur', 'change'] }]" prop="prevDomain">
        <el-input v-model="form.prevDomain" placeholder="请输入预发环境域名" />
      </el-form-item>
      <el-form-item label="线上环境域名:"  :rules="[{ required: true, message: '请输入线上环境域名', trigger: ['blur', 'change'] }]" prop="onlineDomain">
        <el-input v-model="form.onlineDomain" placeholder="请输入线上环境域名" />
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
export default {
  data () {
    return {
      title: '新增模块',
      form: {
        name: '',
        devDomain: '',
        testDomain: '',
        prevDomain: '',
        onlineDomain: '',
        id: ''
      }
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
      this.title = this.$route.params.id ? '修改模块' : '新增模块'
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
                    link: '/module/list'
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
                    link: '/module/list'
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
  watch: {},
  components: {
  },
  beforeCreate () {
  },
  cteated () {
  },
  beforeMount () {
  },
  mounted () {
    this.init()
  },
  beforeUpdate () {
  },
  updated () {
  },
  beforeDestroy () {
  },
  destroyed () {
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style type="text/css" lang="scss" scoped>
  @import 'index.scss';
</style>
