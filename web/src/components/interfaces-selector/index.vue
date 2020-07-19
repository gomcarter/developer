<template>
  <div>
    <v-dialog ref="editNodeDialog" title="选择鉴权接口" :ok="confirm" :width="1000">
      <el-form slot="body" ref="form" :model="node" label-width="8em" class="edit-node-dialog">
        <el-form-item prop="interfaceId" label="选择一个接口" required
                      :rules="[{ required: true, message: '请选择一个接口', trigger: ['blur', 'change'] }]">
          <v-selector :id="'id'" :text="'complexName'" :searchKey="'name'" style="width: 666px;"
                      :onSelectionChanged="onSelectInterface"
                      :filterable="true" :remote="true" placeholder="请选择一个接口（可输入名称或接口ID进行搜索）"
                      :load="node.interfaceId ? [node.interfaceId] : null"
                      :url="interfacesSimpleListApi"
          ></v-selector>
          <el-input v-model="node.interfaceId" class="hidden"></el-input>
        </el-form-item>
        <el-form-item label="访问类型:" v-if="node.interfaceId">
          <div>{{node.method}}</div>
        </el-form-item>
        <el-form-item label="接口地址:" v-if="node.interfaceId">
          <div>{{node.url}}</div>
        </el-form-item>
        <el-form-item label="请求Headers:" v-if="node.interfaceId">
          <el-button type="primary" icon="el-icon-plus" @click="addRequestHeader()" circle size="small"></el-button>
          <el-form v-if="node.requestHeaders">
            <el-form-item v-for="(h, index) of node.requestHeaders" v-bind:key="index">
              <el-input placeholder="请输入header名" class="param-key" v-model="h.key"></el-input>
              <span class="v-top">=</span>
              <el-input placeholder="请输入header值" class="param-value" v-model="h.value"></el-input>
              <el-button type="danger" icon="el-icon-delete" @click="delRequestHeader(index)" circle size="small"></el-button>
            </el-form-item>
          </el-form>
        </el-form-item>
        <el-form-item label="接口参数:" v-if="node.interfaceId">
          <el-tabs v-model="env" type="card" @tab-click="handleClickEnv">
            <el-tab-pane v-for="(value, key) in ENV_DOMAIN_MAP" :key="key" :label="value" :value="key" :name="key">
              <v-parameter-input :parameters="node.parameters[key]"></v-parameter-input>
            </el-tab-pane>
          </el-tabs>
        </el-form-item>
        <el-form-item label="返回值:" v-if="node.interfaceId">
          <v-jsonformatter v-if="generatedReturns" :json="generatedReturns" :min-height="100"></v-jsonformatter>
        </el-form-item>
        <el-form-item label="存储Headers:" v-if="node.interfaceId">
          <el-button type="primary" icon="el-icon-plus" @click="addHeader()" circle size="small"></el-button>
          <el-form v-if="node.headers">
            <el-form-item v-for="(h, index) of node.headers" v-bind:key="index">
              <el-input placeholder="请输入header名" class="param-key" v-model="h.key"></el-input>
              <span>=</span>
              <el-input placeholder="$data为返回值，如：$data.token将结果设置到header中" class="param-value" v-model="h.value"></el-input>
              <el-button type="danger" icon="el-icon-delete" @click="delHeader(index)" circle size="small"></el-button>
            </el-form-item>
          </el-form>
        </el-form-item>
      </el-form>
    </v-dialog>
  </div>
</template>

<script>
import { functionListApi, interfacesSimpleListApi } from '@/config/api/inserv-api'
import { generateParameters, generateReturns } from '@/config/utils'
import { ENV_DOMAIN_MAP } from '@/config/mapping'

export default {
  props: {
    onOk: {
      type: Function,
      default: () => {}
    }
  },
  data () {
    return {
      ENV_DOMAIN_MAP,
      env: 'testDomain',
      nodeId: null,
      node: {
        interfaceId: null,
        interfaceName: null,
        hash: null,
        method: null,
        url: null,
        parameters: {},
        headers: [],
        returns: null,
        java: null,
        requestHeaders: []
      },
      generatedReturns: null,
      interfacesSimpleListApi,
      functionListApi
    }
  },
  methods: {
    handleClickEnv (tab, event) {
    },
    onSelectInterface (selections) {
      const selection = selections[0] || {}
      this.node.interfaceId = selection.id
      this.node.interfaceName = selection.name
      this.node.hash = null
      this.setInterfaces(selection)
    },
    setInterfaces (selection) {
      this.node.url = selection.url
      this.node.method = selection.method
      this.node.java = selection.java

      for (const key in ENV_DOMAIN_MAP) {
        this.$set(this.node.parameters, key, generateParameters(selection.parameters))
      }

      if (selection.returns) {
        this.node.returns = JSON.parse(selection.returns)
        this.generatedReturns = generateReturns(this.node.returns)
      } else {
        this.node.returns = null
        this.generatedReturns = null
      }
      if (selection.end.header) {
        this.node.headers = JSON.parse(selection.end.header)
        this.node.requestHeaders = JSON.parse(selection.end.header)
      } else {
        this.node.headers = [{key: '', value: ''}]
      }
    },
    open (model) {
      // load
      this.node.interfaceId = model.interfaceId
      this.node.interfaceName = model.interfaceName
      this.node.hash = model.hash
      this.node.headers = model.headers || [{key: '', value: ''}]
      this.node.requestHeaders = model.requestHeaders || []
      this.node.parameters = Object.assign({}, model.parameters)
      this.node.java = model.java
      this.node.url = model.url
      this.node.method = model.method

      if (model.returns) {
        this.node.returns = model.returns
        this.generatedReturns = generateReturns(this.node.returns)
      } else {
        this.node.returns = null
        this.generatedReturns = null
      }
      this.$refs.editNodeDialog.open()
    },
    confirm () {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.onOk && this.onOk(Object.assign({}, this.node))
          this.$refs.editNodeDialog.close()
        }
      })
    },
    // paste (e) {
    // fillParamsFromClipboardData(e, this.node.parameters)
    // },
    addHeader () {
      let obj = {key: '', value: ''}
      this.node.headers.push(obj)
    },
    addRequestHeader () {
      let obj = {key: '', value: ''}
      this.node.requestHeaders.push(obj)
    },
    delHeader (i) {
      this.node.headers.splice(i, 1)
    },
    delRequestHeader (i) {
      this.node.requestHeaders.splice(i, 1)
    }
  },
  mounted () {
    // 绑定参数粘帖事件
    // this.$el.addEventListener('paste', this.paste)
  },
  components: {
    'v-dialog': () => import('@/components/dialog'),
    'v-selector': () => import('@/components/selector'),
    'v-jsonformatter': () => import('@/components/jsonformatter'),
    'v-parameter-input': () => import('@/components/parameter-input')
  }
}
</script>

<style type="text/css" lang="scss" scoped>
  @import 'index';
</style>
