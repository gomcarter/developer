<template>
  <div>
    <h4 class="title">{{title}}</h4>
    <hr/>
    <el-form :model="data" ref="edit" label-width="8em" >
      <el-form-item label="项目名称:"  :rules="[{ required: true, message: '请输入项目名称', trigger: ['blur', 'change'] }]" prop="name" class="min_width">
        <el-input v-model="data.name" placeholder="输入项目名称" ></el-input>
      </el-form-item>
      <el-form-item label="项目前缀:"  :rules="[{ required: true, message: '请输入项目前缀', trigger: ['blur', 'change'] }]" prop="prefix" class="min_width">
        <el-input v-model="data.prefix" placeholder="输入项目前缀" ></el-input>
      </el-form-item>
      <el-form-item label="jar包地址:" class="min_width">
        <el-input v-model="data.jarUrl" placeholder="输入jar包地址，比如maven仓库的地址" ></el-input>
      </el-form-item>
      <el-form-item label="类名:" class="min_width">
        <el-input v-model="data.kls" placeholder="输入类名" ></el-input>
      </el-form-item>
      <el-form-item label="方法:" prop="method" class="min_width">
        <el-input v-model="data.method" placeholder="输入方法，此方法必须是静态方法" ></el-input>
      </el-form-item>
      <el-form-item label="参数:" >
        <el-button type="primary" icon="el-icon-plus" @click="addArgs()" circle size="small"></el-button>
        <div v-if="args && args.length > 0" style="margin: 10px 0" v-for="(arg, index) in args" :key="index">
          <el-input placeholder="请输入参数名" class="half_min_width" v-model="arg.key">
            <template slot="prepend"><span class="table_title">参数类名</span></template>
          </el-input>
          <el-input placeholder="请输入参数值" class="hole_min_width" v-model="arg.value">
            <template slot="prepend"><span class="table_title">参数值</span></template>
          </el-input>
          <el-button type="danger" icon="el-icon-delete" @click="deleteArgs(index)" circle size="small"></el-button>
        </div>
      </el-form-item>
      <el-form-item label="Header:" class="min_width">
        <el-input v-model="data.header" placeholder="输入header名" ></el-input>
      </el-form-item>
      <el-form-item label="备注:"  class="min_width">
        <el-input v-model="data.mark" placeholder="输入header参数说明" ></el-input>
      </el-form-item>
      <el-form-item >
        <el-button type="primary" @click="add" icon="el-icon-success">提交</el-button>
        <el-button type="info" @click="$router.go(-1)" icon="el-icon-back">返回</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { addEndApi, getEndApi, updateEndApi } from '@/config/api/inserv-api'

export default {
  data () {
    return {
      title: '项目修改',
      args: [],
      data: {
        name: null,
        prefix: null,
        header: null,
        jarUrl: null,
        kls: null,
        method: null,
        args: null,
        mark: null
      }
    }
  },
  computed: {},
  methods: {
    deleteArgs (index) {
      this.args.splice(index, 1)
    },
    addArgs () {
      let obj = {key: '', value: ''}
      this.args.push(obj)
    },
    init () {
      if (this.$route.params.id) {
        getEndApi(this.$route.params.id).then((res) => {
          this.data = res
          this.args = res.args ? JSON.parse(res.args) : []
        }).catch((err) => {
          console.log(err)
        })
      }
      this.title = this.$route.params.id ? '修改项目' : '添加项目'
    },
    add () {
      this.$refs.edit.validate((valid) => {
        if (valid) {
          this.$confirm('确定保存？', '提示', {type: 'info'}).then(() => {
            this.data.args = JSON.stringify(this.args.filter(s => s.key && s.value))
            if (this.$route.params.id) {
              updateEndApi(this.$route.params.id, this.data).then((res) => {
                this.$transfer({
                  back: '返回编辑',
                  buttons: [{
                    text: '去列表',
                    link: '/consumer/list'
                  }]
                })
              }).catch((err) => {
                console.log(err)
              })
            } else {
              addEndApi(this.data).then((res) => {
                this.$transfer({
                  back: '继续添加',
                  buttons: [{
                    text: '去列表',
                    link: '/consumer/list'
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
