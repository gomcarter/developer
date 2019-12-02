<template>
  <div>
    <h4 class="title">接口测试: {{data.name}}</h4>
    <hr/>
    <el-form :inline="true">
      <el-form-item label="" style="width: 100px">
        <el-select v-model="env">
          <!--<el-option label="开发" value="devDomain"></el-option>-->
          <el-option label="测试" value="testDomain"></el-option>
          <!--<el-option label="预发" value="prevDomain"></el-option>-->
          <el-option label="线上" value="onlineDomain"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="" style="width: 100px">
        <el-select v-model="method" value="POST">
          <el-option label="POST" value="POST"></el-option>
          <el-option label="GET" value="GET"></el-option>
          <el-option label="PATCH" value="PATCH"></el-option>
          <el-option label="PUT" value="PUT"></el-option>
          <el-option label="DELETE" value="DELETE"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="">
        <el-input v-model="showUrl" class="min_width"></el-input>
      </el-form-item>
      <el-form-item label="">
        <el-button type="primary" @click="testUrl">发射!!!</el-button>
      </el-form-item>
    </el-form>
    <div>
      <div class="left">
        <h4 class="title">返回结果</h4>
        <hr/>
        <el-form>
          <el-form-item label="">
            <v-jsonformatter :json="result" style="max-height:  600px; overflow-x: hidden;"></v-jsonformatter>
          </el-form-item>
        </el-form>
      </div>
      <div class="right">
        <h4 class="title">header    <el-button type="primary" icon="el-icon-plus" @click="addHeader()" circle size="small"></el-button></h4>
        <hr/>
        <el-form v-if="headers">
          <el-form-item v-for="(h, index) of headers" v-bind:key="index">
            <el-input placeholder="请输入headers参数名" class="half_min_width" v-model="h.key">
              <template slot="prepend"><span class="table_title">参数名</span></template>
            </el-input>
            <el-input placeholder="请输入headers参数值" class="half_min_width" v-model="h.value">
              <template slot="prepend"><span class="table_title">参数值</span></template>
            </el-input>
            <el-button type="danger" @click="delHeader(index)">删除</el-button>
          </el-form-item>
        </el-form>
        <h4 class="title">参数</h4>
        <hr/>
        <el-form v-if="parameters && parameters.length > 0">
          <el-form-item label="" v-for="(param, index) in parameters" :key="index">
            <el-input placeholder="请输入参数名" class="half_min_width" v-model="param.key">
              <template slot="prepend"><span class="table_title">参数名</span></template>
            </el-input>
            <el-input placeholder="请输入参数值" class="half_max_width" v-if="param.inputType === 'textarea'" :rows="15" type="textarea" v-model="param.defaults">
              <template slot="prepend"><span class="table_title">参数值</span></template>
            </el-input>
            <el-input v-else placeholder="请输入参数值" class="half_max_width" v-model="param.defaults">
              <template slot="prepend"><span class="table_title">参数值</span></template>
            </el-input>
            <el-button type="primary" icon="el-icon-plus" @click="addParams()" circle size="small"></el-button>
            <el-button type="danger" icon="el-icon-delete" @click="delParam(index)" circle size="small"></el-button>
          </el-form-item>
        </el-form>
        <el-form v-else>
          <el-form-item label="">
            <el-button type="primary" icon="el-icon-plus" @click="addParams()" circle size="small"></el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

/**
待实现:
    1,根据参数类型判断输入是否正确;
    2,支持参数选择文件
    3,支持POST输入body
    4,支持检查参数是否必填
*/
<script>
import { xhr } from '@/config/api/http'
import { getInterfacesApi, getInterfacesHeadersApi } from '@/config/api/inserv-api'
import { toQueryString, formatDate } from '@/config/utils'

export default {
  props: {},
  data () {
    return {
      data: {
        id: null,
        hash: null,
        name: null,
        url: null,
        method: null,
        parameters: null,
        returns: null,
        createTime: null,
        deprecated: null,
        end: {
          id: null,
          name: null,
          prefix: null,
          header: null
        },
        java: {
          id: null,
          name: null,
          devDomain: null,
          testDomain: null,
          prevDomain: null,
          onlineDomain: null
        }
      },
      method: 'GET',
      parameters: null,
      headers: [],
      env: 'testDomain',
      result: null,
      bodyParams: null
    }
  },
  computed: {
    showUrl () {
      if (!this.data.id) {
        return ''
      }
      let url = this.data.java[this.env] + this.data.url
      this.parameters.filter(s => url.indexOf('{' + s.key + '}') >= 0)
        .forEach(s => {
          if (s.defaults) {
            url = url.replace('{' + s.key + '}', s.defaults)
          }
        })
      return url
    }
  },
  mounted () {
    this.init()
  },
  methods: {
    generateBodyPlaceholder (node) {
      if (node.type === 'List') {
        return [this.generateBodyPlaceholder(node.children[0])]
      } else if (node.type === 'Object') {
        const o = {}
        node.children = node.children || []
        node.children.forEach(s => {
          o[s.key] = this.generateBodyPlaceholder(s)
        })
        return o
      } else if (node.type === undefined || node.type === 'void') {
        return null
      } else if (node.type === 'boolean') {
        return false
      } else if (node.type === 'Date') {
        return formatDate(new Date())
      } else if (node.type === 'Integer') {
        return 0
      } else {
        return '改这里'
      }
    },
    init () {
      this.env = this.$route.params.env || this.env
      getInterfacesApi(this.$route.params.id).then(r => {
        this.data = r
        this.method = this.data.method || this.method

        getInterfacesHeadersApi(this.data.id)
          .then(h => {
            this.headers = h ? [h] : []
          })

        // 把json数据结构全部打平
        this.parameters = JSON.parse(this.data.parameters).flatMap(s => {
          // 第一层的Object参数名去掉
          if (s.type === 'Object' && !s.body) {
            s.key = undefined
          }
          return this.generateChild(s, undefined)
        })
      })
    },
    generateChild (node, key) {
      const k = (key ? (key + '.') : '') + (node.key || '')
      if (node.body) {
        this.bodyParams = k
        return {
          key: k,
          notNull: node.notNull,
          comment: node.comment,
          defaults: JSON.stringify(this.generateBodyPlaceholder(node), null, 4),
          type: node.type,
          inputType: 'textarea'
        }
      } else {
        if (node.type === 'List') {
          let that = []
          if (node.children[0].type === 'Object' || node.children[0].type === 'List') {
            that = that.concat(
              (node.children[0].children || []).flatMap(s => this.generateChild(s, k))
            )
          }
          return that
        } else if (node.type === 'Object') {
          return (node.children || []).flatMap(s => this.generateChild(s, k))
        } else {
          return {
            key: k,
            notNull: node.notNull,
            comment: node.comment,
            defaults: node.defaults,
            type: node.type
          }
        }
      }
    },
    testUrl () {
      let params
      let method = this.method.toLowerCase()
      let res
      let obj
      if (this.parameters.length === 0) {
        obj = ''
      } else {
        obj = {}
        this.parameters.forEach(e => {
          // 同一key的时候，需要合并
          if (obj[e['key']] === undefined) {
            obj[e['key']] = e['defaults']
          } else {
            obj[e['key']] = [e['defaults']].concat(obj[e['key']])
          }
        })
      }
      switch (method) {
        case 'put':
        case 'post':
        case 'patch':
          params = obj
          break
        case 'delete':
        case 'get':
          params = {
            params: obj
          }
          break
      }

      // 清除不必要的headers
      Object.entries(xhr.defaults.headers)
        .forEach(s => {
          if (['common', 'delete', 'get', 'head', 'patch', 'post', 'put'].indexOf(s[0]) < 0) {
            delete xhr.defaults.headers[s[0]]
          }
        })

      if (this.headers && this.headers.length > 0) {
        this.headers.forEach(s => {
          xhr.defaults.headers[s.key] = s.value
        })
      }

      console.log(this.bodyParams)
      if (this.bodyParams) {
        xhr.defaults.headers['content-type'] = 'application/json'
        const body = JSON.stringify(JSON.parse(obj[this.bodyParams]))
        delete obj[this.bodyParams]
        res = xhr[method](this.showUrl + '?' + toQueryString(obj), body, {type: 'postWithBody'})
      } else {
        res = xhr[method](this.showUrl, params)
      }
      res.then(r => {
        this.result = r.data
      }).catch(e => {
        console.log(e)
      })
    },
    addParams () {
      let obj = {key: '', value: '', type: ''}
      this.parameters.push(obj)
    },
    addHeader () {
      let obj = {key: '', value: ''}
      this.headers.push(obj)
    },
    delParam (i) {
      this.parameters.splice(i, 1)
    },
    delHeader (i) {
      this.headers.splice(i, 1)
    }
  },
  components: {
    'v-jsonformatter': () => import('@/components/jsonformatter')
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style type="text/css" lang="scss">
  @import 'index.scss';
</style>
