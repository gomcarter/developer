<template>
  <div>
    <h4 class="title">{{title}}</h4>
    <hr/>
    <el-form :model="form" ref="edit" >
      <el-form-item label="参数名称:" label-width="8em" :rules="[{ required: true, message: '请输入规则名称', trigger: ['blur', 'change'] }]" prop="name" class="min_width">
        <el-input v-model="form.name" placeholder="请输入自定义参数名称"></el-input>
      </el-form-item>
      <el-form-item label="生成规则脚本:" label-width="8em" class="min_width" style="width: 700px">
        <el-tabs v-model="env" type="card" @tab-click="handleClickEnv" style="width: 700px;">
          <el-tab-pane v-for="(value, key) in ENV_DOMAIN_MAP" :key="key" :label="value" :value="key" :name="key">
            <el-input v-model="form.scriptText[key]" type="textarea" rows="15" :placeholder="`示例：随机n-m整数的脚本如下
var n = +arguments[0];
var m = +arguments[1];
return n + Math.floor(Math.random() * (m - n));
注：arguments为输入参数，在示例参数列输入：n,m（如：200,400）`"></el-input>
<!--            <v-parameter-input :parameters="node.parameters[key]"></v-parameter-input>-->
          </el-tab-pane>
        </el-tabs>
      </el-form-item>
      <el-form-item label="示例参数:" label-width="8em" class="min_width">
        <el-input v-model="form.arguments" placeholder="请输入示例入参，多参数逗号隔开，如：200,400"></el-input>
      </el-form-item>
      <el-form-item label="备注:" label-width="8em" prop="mark" class="min_width">
        <el-input v-model="form.mark" type="textarea" rows="3"></el-input>
      </el-form-item>
      <el-form-item label="" label-width="8em">
        <el-checkbox v-model="form.isPublic" label="是否公用" :checked="form.isPublic"></el-checkbox>
      </el-form-item>
      <el-form-item label="" label-width="8em">
        <el-button @click="action">测试</el-button>
      </el-form-item>
      <el-form-item label="执行结果:" label-width="8em" style="width: 60%">
        <v-jsonformatter :json="result"></v-jsonformatter>
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
import { ENV_DOMAIN_MAP } from '@/config/mapping'

export default {
  data () {
    return {
      ENV_DOMAIN_MAP,
      title: '新增自定义参数',
      env: 'testDomain',
      form: {
        id: '',
        name: '',
        scriptText: {},
        mark: '',
        arguments: '',
        isPublic: false
      },
      result: ''
    }
  },
  computed: {},
  methods: {
    init () {
      if (this.$route.params.id) {
        this.title = '编辑自定义参数'
        getFunctionApi(this.$route.params.id).then((res) => {
          this.form = res
          try {
            this.form.scriptText = JSON.parse(res.scriptText)
          } catch (e) {
            // 兼容老数据
            this.form.scriptText = {
              testDomain: res.scriptText
            }
          }
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
              const data = Object.assign({}, this.form, {scriptText: JSON.stringify(this.form.scriptText)})
              modifyFunctionApi(data).then((res) => {
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
              const data = Object.assign({}, this.form, {scriptText: JSON.stringify(this.form.scriptText)})
              addFunctionApi(data).then((res) => {
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
      try {
        this.result = new Function((this.form.scriptText[this.env] || '').replace(/\n/g, ' '))(...(this.form.arguments || '').split(','))
        console.log('result', this.result)
      } catch (e) {
        console.log(e)
        this.result = e.toString()
      }
    },
    handleClickEnv () {

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
