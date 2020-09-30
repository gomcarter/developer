<template>
  <div>
    <h4 class="title">{{title}}</h4>
    <hr/>
    <el-form :model="data" ref="edit" label-width="8em" >
      <el-form-item label="项目名称:"  :rules="[{ required: true, message: '请输入项目名称', trigger: ['blur', 'change'] }]" prop="name" class="min_width">
        <el-input v-model="data.name" placeholder="输入项目名称" ></el-input>
      </el-form-item>
      <el-form-item label="项目前缀:"  :rules="[{ required: true, message: '请输入项目前缀', trigger: ['blur', 'change'] }]" prop="prefix" class="min_width">
        <el-input v-model="data.prefix" placeholder="输入项目前缀，给不同的前台统一一个接口的前缀" ></el-input>
        <div class="mark">示例：/end1/order/{id}中end1定义为某个前台系统，所有这个前台系统的接口都以end1开头</div>
      </el-form-item>
      <el-form-item label="鉴权Headers:">
        <el-button type="primary" icon="el-icon-plus" @click="addHeader()" circle size="small"></el-button>
      </el-form-item>
      <el-form-item v-for="(h, index) of header" v-bind:key="index">
        <el-input placeholder="请输入header名" class="param-key" v-model="h.key"></el-input>
        <span>=</span>
        <el-input placeholder="header的默认值，如果没有则不填写" class="param-value" v-model="h.value"></el-input>
        <el-button type="danger" icon="el-icon-delete" @click="delHeader(index)" circle size="small"></el-button>
      </el-form-item>
      <el-form-item label="鉴权接口设置:">
        <span v-if="config.interfaceId">{{ config.interfaceName }}</span>
        <span v-else>未设置</span>
        <el-button type="primary" @click="edit" icon="el-icon-setting" circle size="small"></el-button>
        <div class="mark">设置之后，系统在单接口测试且需要鉴权时自动完成鉴权</div>
      </el-form-item>
      <el-form-item label="备注:" class="min_width">
        <el-input v-model="data.mark" type="textarea" rows="5" :placeholder="`示例：
1，调用此接口必须在header中加入：platform=airmall
2，必须登录，且在cookie或者header中传airmallLoginToken=登录获取到的token`"></el-input>
      </el-form-item>
      <el-form-item >
        <el-button type="primary" @click="add" icon="el-icon-success">提交</el-button>
        <el-button type="info" @click="$router.go(-1)" icon="el-icon-back">返回</el-button>
      </el-form-item>
    </el-form>
    <v-interfaces-selector ref="interfacesSelector" :on-ok="saveNode"></v-interfaces-selector>
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
        mark: null
      },
      header: [],
      config: {}
    }
  },
  methods: {
    init () {
      if (this.$route.params.id) {
        getEndApi(this.$route.params.id).then((res) => {
          Object.assign(this.data, res)
          this.config = res.config ? JSON.parse(res.config) : {}
          console.log(JSON.parse(res.header))
          if (res.header) {
            this.header = JSON.parse(res.header)
          }
          console.log(this.header)
        }).catch((err) => {
          console.log(err)
        })
      }
      this.title = this.$route.params.id ? '修改前台项目' : '添加前台项目'
    },
    add () {
      this.$refs.edit.validate((valid) => {
        if (valid) {
          this.$confirm('确定保存？', '提示', {type: 'info'}).then(() => {
            this.data.config = JSON.stringify(this.config)
            this.data.header = JSON.stringify(this.header)
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
    },
    edit () {
      this.$refs.interfacesSelector.open(this.config)
    },
    saveNode (data) {
      this.$set(this, 'config', data)
    },
    addHeader () {
      let obj = {key: '', value: ''}
      this.header.push(obj)
    },
    delHeader (i) {
      this.header.splice(i, 1)
    }
  },
  mounted () {
    this.init()
  },
  components: {
    'v-interfaces-selector': () => import('@/components/interfaces-selector')
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style type="text/css" lang="scss" scoped>
  @import 'index.scss';
</style>
