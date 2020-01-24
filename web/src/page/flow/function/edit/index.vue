<template>
  <div>
    <h4 class="title">{{title}}</h4>
    <hr/>
    <el-form :model="form" ref="edit" >
      <el-form-item label="规则名称:" label-width="8em" :rules="[{ required: true, message: '请输入规则名称', trigger: ['blur', 'change'] }]" prop="name">
        <el-input v-model="form.name" placeholder="请输入规则名称" />
      </el-form-item>
      <el-form-item label="生成规则脚本:" label-width="8em">
        <el-input v-model="form.script" type="textarea" rows="10" />
      </el-form-item>
      <el-form-item label="备注:" label-width="8em" prop="mark">
        <el-input v-model="form.mark" type="textarea" rows="3" />
      </el-form-item>
      <el-form-item label="" label-width="8em">
        <el-button @click="action">测试</el-button>
      </el-form-item>
      <el-form-item label="执行结果:" label-width="8em">
        <v-jsonformatter v-if="result" :json="result"></v-jsonformatter>
      </el-form-item>
      <el-form-item label-width="8em">
        <el-button type="primary" @click="add" icon="el-icon-success">提交</el-button>
        <el-button type="info" @click="$router.go(-1)" icon="el-icon-back">返回</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {addFunctionApi, getFunctionApi, modifyFunctionApi} from '@/config/api/inserv-api'
export default {
  data () {
    return {
      title: '新增自定义函数',
      form: {
        id: '',
        name: '',
        script: '',
        mark: ''
      },
      result: ''
    }
  },
  computed: {},
  methods: {
    init () {
      if (this.$route.params.id) {
        this.title = '编辑自定义函数'
        getFunctionApi(this.$route.params.id).then((res) => {
          this.form = res
        }).catch((err) => {
          console.log(err)
        })
      }
    },
    add () {
      this.$refs.edit.validate((valid) => {
        if (valid) {
          this.$confirm('确定保存？', '提示', {type: 'info'}).then(() => {
            if (this.$route.params.id) {
              modifyFunctionApi(this.form).then((res) => {
                this.$transfer({
                  back: '返回编辑',
                  buttons: [{
                    text: '去列表',
                    link: '/flow/function'
                  }]
                })
              }).catch(() => {
              })
            } else {
              addFunctionApi(this.form).then((res) => {
                this.$transfer({
                  back: '继续添加',
                  buttons: [{
                    text: '去列表',
                    link: '/flow/function'
                  }]
                })
              }).catch(() => {
              })
            }
          }).catch(() => {
          })
        }
      })
    },
    action () {
      /* eslint-disable */
      // this.result = eval(this.form.script)
      this.result = new Function(this.form.script)()
    }
  },
  components: {
    'v-jsonformatter': () => import('@/components/jsonformatter')
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
