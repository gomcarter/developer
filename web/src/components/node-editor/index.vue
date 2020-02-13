<template>
  <div>
    <v-dialog ref="editNodeDialog" :title="'配置节点，当前节点编号：' + nodeId" :ok="confirm" :width="1000">
      <el-form slot="body" ref="form" :model="node" label-width="8em" class="edit-node-dialog">
        <el-form-item prop="interfaceId" label="选择一个接口" required
                      :rules="[{ required: true, message: '请选择一个接口', trigger: ['blur', 'change'] }]">
          <v-selector :id="'id'" :text="'name'" style="width: 666px;"
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
        <el-form-item label="访问类型:">
          <div>{{node.method}}</div>
        </el-form-item>
        <el-form-item label="接口地址:">
          <div>{{node.url}}</div>
        </el-form-item>
        <el-form-item label="header:">
          <el-button type="primary" icon="el-icon-plus" @click="addHeader()" circle size="small"></el-button>
          <el-form v-if="node.headers">
            <el-form-item v-for="(h, index) of node.headers" v-bind:key="index">
              <el-input placeholder="请输入headers参数名" class="param-key" v-model="h.key"></el-input>=
              <el-input placeholder="请输入headers参数值，不填写则运行时手动输入" class="param-value" v-model="h.value"></el-input>
              <el-button type="danger" icon="el-icon-delete" @click="delHeader(index)" circle size="small"></el-button>
            </el-form-item>
          </el-form>
        </el-form-item>
        <el-form-item label="接口参数:">
          <el-form v-if="node.parameters && node.parameters.length > 0" class="params-form">
            <el-form-item label="" v-for="(param, index) in node.parameters" :key="index">
              <el-input placeholder="请输入参数名" class="param-key" v-model="param.key"></el-input>=
              <el-input :placeholder="param.comment || ''" class="param-value" v-if="param.inputType === 'textarea'"
                        :rows="15" type="textarea" v-model="param.defaults" :name="param.key">
              </el-input>
              <el-input v-else :placeholder="param.comment || ''"  class="param-value" v-model="param.defaults" :name="param.key">
              </el-input>
              <el-button type="primary" icon="el-icon-plus" @click="addParams()" circle size="small"></el-button>
              <el-button type="danger" icon="el-icon-delete" @click="delParam(index)" circle size="small"></el-button>
            </el-form-item>
          </el-form>
        </el-form-item>
        <el-form-item label="返回值:">
          <v-jsonformatter v-if="generatedReturns" :json="generatedReturns" :min-height="100"></v-jsonformatter>
        </el-form-item>
        <el-form-item label="返回值处理脚本:">
          <el-input :placeholder="`注：本接口的调用结果将存入$${nodeId}中。
此脚本可以对数据进行转换，在脚本最后使用 return xxx;    xxx（即为转换后的数据）将作为新值覆盖$${nodeId}
当然这里也可以不 return或者return null，那么$${nodeId}将保留接口请求返回的原始数据
您可以在后续流程中使用$${nodeId}来获取本节点的返回值，如$${nodeId}.id可获取到返回值中的id
另外可以获取上游节（以及上游的上游）点带来的数据，如：$anotherNode.xx
当然您可以可以在这里做一些返回值校验，如果发现错误了写如下代码： throw new Error('错误信息')，示例：

if ($${nodeId}.id == null) throw new Error('返回id不能为空！')
if ($${nodeId}.quantity > 0) throw new Error('数量必须大于等于零！')

如果报错了，那么整个流程将被终止。

return {
  id: $${nodeId}.id,
  name: $${nodeId}.name,
  quantity: $${nodeId}.quantity
}`" :rows="15" type="textarea" v-model="node.javascript">
          </el-input>
        </el-form-item>
      </el-form>
    </v-dialog>
  </div>
</template>

<script>
import { functionListApi, interfacesSimpleListApi, interfacesVersionedSimpleListApi, getInterfacesHeadersApi } from '@/config/api/inserv-api'
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
        interfaceId: null,
        interfaceName: null,
        functionId: null,
        history: false,
        hash: null,
        method: null,
        url: null,
        parameters: null,
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
    onSelectInterface (selections) {
      const selection = selections[0] || {}
      this.node.interfaceId = selection.id
      this.node.interfaceName = selection.name
      this.node.hash = null
      this.setInterfaces(selection)
    },
    onSelectHistoryInterface (selections) {
      const selection = selections[0] || {}
      this.node.hash = selection.hash
      this.setInterfaces(selection)
    },
    setInterfaces (selection) {
      this.node.url = selection.url
      this.node.method = selection.method
      this.node.java = selection.java
      this.node.parameters = generateParameters(selection.parameters)
      if (selection.returns) {
        this.node.returns = JSON.parse(selection.returns)
        this.generatedReturns = generateReturns(this.node.returns)
      } else {
        this.node.returns = null
        this.generatedReturns = null
      }

      if (selection.interfacesId) {
        getInterfacesHeadersApi(selection.interfacesId)
          .then(h => {
            this.node.headers = h ? [h] : []
          })
      } else {
        this.node.headers = []
      }
    },
    open (model, edges) {
      const data = model.data || {}
      this.nodeId = model.id
      // load
      this.node.interfaceId = data.interfaceId
      this.node.interfaceName = data.interfaceName
      this.node.functionId = data.functionId
      this.node.history = data.history || false
      this.node.hash = data.hash
      this.node.headers = data.headers || []
      this.node.parameters = data.parameters || []
      this.node.javascript = data.javascript
      this.node.java = data.java
      this.node.url = data.url
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
    addParams () {
      let obj = {key: '', value: '', type: ''}
      this.node.parameters.push(obj)
    },
    addHeader () {
      let obj = {key: '', value: ''}
      this.node.headers.push(obj)
    },
    delParam (i) {
      this.node.parameters.splice(i, 1)
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
    'v-jsonformatter': () => import('@/components/jsonformatter')
  }
}
</script>

<style type="text/css" lang="scss">
  @import 'index';
</style>
