<template>
  <div>
    <h4 class="title">{{title}}</h4>
    <hr/>
    <el-form :model="form" ref="edit">
      <el-form-item label="规则名称:" label-width="8em" :rules="[{ required: true, message: '请输入规则名称', trigger: ['blur', 'change'] }]" prop="name">
        <el-input v-model="form.name" placeholder="请输入SVN地址" />
      </el-form-item>
      <el-form-item label="生成规则脚本:" label-width="8em">
        <el-input v-model="form.generator" type="textarea" rows="10"/>
      </el-form-item>
      <el-form-item label="" label-width="8em">
        <el-button @click="action">执行</el-button>
      </el-form-item>
      <el-form-item label="执行结果:" label-width="8em">
        <div class="result">{{result}}</div>
      </el-form-item>
      <el-form-item label-width="8em">
        <el-button type="primary" @click="add" icon="el-icon-success">提交</el-button>
        <el-button type="info" @click="$router.go(-1)" icon="el-icon-back">返回</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { postInterRules, getInterRulesId } from '@/config/api/inserv-api'
export default {
  data () {
    return {
      title: '新增规则',
      form: {
        name: '',
        generator: ''
      },
      result: ''
    }
  },
  computed: {},
  methods: {
    init () {
      if (this.$route.params.id) {
        this.title = '修改规则'
        getInterRulesId(this.$route.params.id).then((res) => {
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
            postInterRules(this.form).then((res) => {
              this.$transfer({
                back: '继续添加',
                buttons: [{
                  text: '去列表',
                  link: '/class/list'
                }]
              })
            }).catch(() => {
            })
          }).catch(() => {
          })
        }
      })
    },
    action () {
      /* eslint-disable */
      this.result = new Function(this.form.generator)()
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
