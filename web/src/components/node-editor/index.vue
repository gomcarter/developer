<template>
  <div>
    <v-dialog ref="editNodeDialog" :title="'配置节点，当前节点编号：' + nodeId" :ok="confirm" :width="1000">
      <el-form slot="body" ref="form" :model="node" label-width="8em" class="edit-node-dialog">
        <el-form-item prop="interfaceId" label="选择一个接口" required
                      :rules="[{ required: true, message: '请选择一个接口', trigger: ['blur', 'change'] }]">
          <v-selector :id="'id'" :text="'complexName'" :searchKey="'name'" style="width: 666px;"
                      :onSelectionChanged="onSelectInterface"
                      :filterable="true" :remote="true" placeholder="请选择一个接口（可输入名称进行搜索）"
                      :load="node.interfaceId ? [node.interfaceId] : null"
                      :url="interfacesSimpleListApi"
          ></v-selector>
          <el-input v-model="node.interfaceId" class="hidden"></el-input>
          <el-checkbox v-model="node.history">使用历史版本</el-checkbox>
        </el-form-item>
        <el-form-item label="历史接口" v-if="node.history">
          <v-selector :id="'hash'"  :text="'name'"
                      :onSelectionChanged="onSelectHistoryInterface" placeholder="请选择一个历史版本（不选择，当前接口作为副本）"
                      :remote="true" :load="node.hash ? [node.hash] : null"
                      :url="interfacesVersionedSimpleListApi" :extraParams="node.interfaceId ? {interfacesId: node.interfaceId} : {interfacesId: -1}"
          ></v-selector>
        </el-form-item>
        <el-form-item label="访问类型:" v-if="node.interfaceId">
          <div>{{node.method}}</div>
        </el-form-item>
        <el-form-item label="接口地址:" v-if="node.interfaceId">
          <div>{{node.url}}</div>
        </el-form-item>
        <el-form-item label="休眠" v-if="node.interfaceId">
          <el-input v-model="node.sleep" style="display: inline-block; width: 100px" placeholder="0"></el-input>&#12288;秒之后运行
        </el-form-item>
        <el-form-item label="必备Header:" v-if="node.interfaceId">
          <el-button type="primary" icon="el-icon-plus" @click="addHeader()" circle size="small"></el-button>
          <el-form v-if="node.headers">
            <el-form-item v-for="(h, index) of node.headers" v-bind:key="index">
              <el-input placeholder="请输入headers参数名" class="param-key" v-model="h.key">
                <template slot="prepend"><span>参数名</span></template>
              </el-input>
              <span class="v-top">=</span>
              <el-input placeholder="请输入headers参数值" class="param-value" v-model="h.value">
                <template slot="prepend"><span>参数值</span></template>
              </el-input>
              <el-button type="danger" icon="el-icon-delete" @click="delHeader(index)" circle size="small"></el-button>
            </el-form-item>
          </el-form>
        </el-form-item>
        <el-form-item label="预置参数:" v-if="node.interfaceId">
          <el-button type="primary" icon="el-icon-plus" @click="addPreParams()" circle size="small"></el-button>
          <v-parameter-input :parameters="node.preParams"></v-parameter-input>
        </el-form-item>
        <el-form-item label="接口参数:" v-if="node.interfaceId">
          <v-parameter-input :parameters="node.parameters"></v-parameter-input>
        </el-form-item>
        <el-form-item label="返回值数据结构:" v-if="node.interfaceId">
          <v-jsonformatter v-if="generatedReturns" :json="generatedReturns" :min-height="100"></v-jsonformatter>
        </el-form-item>
        <el-form-item label="检查点:" v-if="node.interfaceId">
          <el-input :placeholder="`注：本接口的调用结果将存入$${nodeId}和$this中。
另外可以获取上游节（以及上游的上游）点带来的数据，如：$anotherNode.xx
在这里写入一些检查点脚本，示例：

assert($this.name == null, '返回名称不能为空！')
assert($${nodeId}.id == null, '返回id不能为空！')
assert($${nodeId}.quantity > 0, '数量必须大于等于零！')

如果报错了，那么整个流程将被终止。`" :rows="18" type="textarea" v-model="node.javascript">
          </el-input>
        </el-form-item>
      </el-form>
    </v-dialog>
  </div>
</template>

<script>
import { functionListApi, interfacesSimpleListApi, interfacesVersionedSimpleListApi, getCusInterfacesApi } from '@/config/api/inserv-api'
import { generateParameters, fillParamsFromClipboardData, generateReturns } from '@/config/utils'

export default {
  props: {
    onOk: {
      type: Function,
      default: () => {}
    }
  },
  data () {
    return {
      nodeId: null,
      node: {
        sleep: 0,
        interfaceId: null,
        interfaceName: null,
        history: false,
        hash: null,
        method: null,
        url: null,
        parameters: null,
        preParams: null,
        headers: [],
        javascript: null,
        returns: null,
        java: null
      },
      generatedReturns: null,
      interfacesVersionedSimpleListApi,
      interfacesSimpleListApi,
      functionListApi
    }
  },
  methods: {
    async onSelectInterface (selections) {
      const selection = selections[0] || {}
      this.node.interfaceId = selection.id
      this.node.interfaceName = selection.name
      this.node.hash = null

      this.setInterfaces(await this.getConfiguredParameters(selection.id, selection))
    },
    async getConfiguredParameters (interfacesId, selection) {
      if (!interfacesId) {
        return
      }

      const cus = await getCusInterfacesApi(interfacesId)
      if (cus && cus.cusParameters) {
        selection.parameters = JSON.parse(cus.cusParameters)
        selection.javascript = cus.javascript != null ? JSON.parse(cus.javascript) : null
        selection.preParams = JSON.parse(cus.preParams)
        console.log(selection.javascript, JSON.stringify(selection.javascript))
      } else {
        selection.parameters = generateParameters(selection.parameters)
      }

      return selection
    },
    async onSelectHistoryInterface (selections) {
      const selection = selections[0] || {}
      this.node.hash = selection.hash

      this.setInterfaces(await this.getConfiguredParameters(selection.id, selection))
    },
    setInterfaces (selection) {
      this.node.url = selection.url
      this.node.method = selection.method
      this.node.java = selection.java
      this.node.sleep = selection.sleep
      this.node.parameters = selection.parameters
      this.node.javascript = selection.javascript
      this.node.preParams = selection.preParams || []
      if (selection.returns) {
        this.node.returns = JSON.parse(selection.returns)
        this.generatedReturns = generateReturns(this.node.returns)
      } else {
        this.node.returns = null
        this.generatedReturns = null
      }

      this.$set(this.node, 'headers', selection.end.header ? JSON.parse(selection.end.header) : [])
    },
    open (model, edges) {
      const data = model.data || {}
      this.nodeId = model.id
      // load
      this.node.interfaceId = data.interfaceId
      this.node.interfaceName = data.interfaceName
      this.node.history = data.history || false
      this.node.hash = data.hash
      this.node.headers = data.headers || []
      this.node.parameters = data.parameters || []
      this.node.preParams = data.preParams || []
      this.node.javascript = data.javascript
      this.node.java = data.java
      this.node.url = data.url
      this.node.sleep = data.sleep
      this.node.method = data.method

      if (data.returns) {
        this.node.returns = data.returns
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
    paste (e) {
      fillParamsFromClipboardData(e, this.node.parameters)
    },
    addPreParams () {
      this.node.preParams.push({key: '', value: '', type: 'text'})
    },
    addHeader () {
      let obj = {key: '', value: ''}
      this.node.headers.push(obj)
    },
    delHeader (i) {
      this.node.headers.splice(i, 1)
    }
  },
  mounted () {
    // 绑定参数粘帖事件
    this.$el.addEventListener('paste', this.paste)
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
