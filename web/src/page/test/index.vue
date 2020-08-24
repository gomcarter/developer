<template>
  <div>
    <h4 class="title">
      接口测试: {{data.name}}
      <v-selector :id="'id'" :text="'name'" :searchKey="'name'"
                  style="width: 35%; text-indent: 0; margin-left: 20px;"
                  :onSelectionChanged="onSelectCusInterfacesItem" placeholder="请选择一个历史保存的用例"
                  :remote="true" :filterable="true" :url="getCusInterfacesItemListApi"
                  :load="currentId ? [currentId] : null" ref="selector"
                  :extraParams="customInterfacesId ? {customInterfacesId: customInterfacesId, rows: 20} : {customInterfacesId: -1}"
      ></v-selector>
      <el-button type="danger" @click="deleteItem" circle size="small" icon="el-icon-delete" :disabled="currentId == null"></el-button>
    </h4>
    <hr/>
    <el-form :inline="true">
      <el-form-item label="" style="width: 140px">
        <el-select v-model="env">
          <el-option v-for="(value, key) in ENV_DOMAIN_MAP" :key="key" :label="value" :value="key"></el-option>
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
        <el-input v-model="showUrl" style="width: 575px"></el-input>
      </el-form-item>
      <el-form-item label="">
        <el-button type="danger" @click="testUrl">发射!!!</el-button>
        <el-button type="info" @click="configLocalEnv" v-if="data.id && this.env === 'localDomain'">配置本地环境</el-button>
        <el-button type="primary" @click="save" :disabled="currentId == null">保存</el-button>
        <el-button type="primary" @click="saveAs">另存为</el-button>
        <el-button type="success" @click="run" v-if="customInterfacesId">批量执行</el-button>
      </el-form-item>
    </el-form>
    <div>
      <div class="left">
        <h4 class="title" style="display: inline-block;">调用结果</h4>
        <span v-if="end" style="display: inline-block; margin-left: 20px;">
          <b>耗时：</b>{{ `${end - start}` }}ms；
        </span>
        <hr/>
        <el-form>
          <el-form-item label="">
            <v-jsonformatter :json="result" style="max-height: 500px; overflow-x: hidden;"></v-jsonformatter>
          </el-form-item>
        </el-form>
        <h4 class="title">检查点结果</h4>
        <hr/>
        <span v-if="checkResult != null" v-html="checkResultMessage"></span>
        <span v-else>未设置检查点</span>
      </div>
      <div class="right">
        <h4 class="title" style="height: 22px">header
          <el-button type="primary" icon="el-icon-plus" @click="addHeader()" circle size="small"></el-button>
          <el-button type="primary" icon="el-icon-setting" v-if="data.end.id" @click="openInterfacesSelectorDialog" size="small">设置授权账号</el-button>
          <el-button type="primary" icon="el-icon-refresh" v-if="data.end.id" @click="loadHeaders(true)" size="small"></el-button>
        </h4>
        <hr/>
        <el-form v-if="headers">
          <el-form-item v-for="(h, index) of headers" v-bind:key="index">
            <el-input placeholder="请输入headers参数名" class="half_min_width" v-model="h.key">
              <template slot="prepend"><span>参数名</span></template>
            </el-input>
            <span class="v-top">=</span>
            <el-input placeholder="请输入headers参数值" class="half_max_width" v-model="h.value">
              <template slot="prepend"><span>参数值</span></template>
            </el-input>
            <el-button type="danger" icon="el-icon-delete" @click="delHeader(index)" circle size="small"></el-button>
          </el-form-item>
        </el-form>
        <h4 class="title">预置参数
          <el-button type="primary" icon="el-icon-plus" @click="addPreParams()" circle size="small"></el-button>
        </h4>
        <hr/>
        <v-parameter-input :parameters="preParams" :need-add-params="false"></v-parameter-input>
        <h4 class="title">参数
          <el-button type="primary" icon="el-icon-plus" @click="addParams()" circle size="small"></el-button>
        </h4>
        <hr/>
        <v-parameter-input :parameters="parameters" :need-add-params="false"></v-parameter-input>
        <h4 class="title">检查点</h4>
        <hr/>
        <el-input :placeholder="`注：本接口的调用结果将存入this中。
在这里写入一些检查点脚本，示例：

assert(this.name != null, '返回名称不能为空！')
assert(this.data.id != null, '返回id不能为空！')
assert(this.data.id.quantity > 0, '数量必须大于等于零！')

如果报错了，那么整个流程将被终止。`" :rows="18" type="textarea"  v-model="javascript" >
        </el-input>
      </div>
    </div>
    <v-interfaces-selector ref="interfacesSelector" :on-ok="savePrivatesAuth"></v-interfaces-selector>
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
import { getCusInterfacesByInterfacesIdApi, getInterfacesApi, getInterfacesHeadersApi, generateUrl, getPrivatesEndAuthApi,
  setPrivatesEndAuthApi, saveCusInterfacesItemApi, addCusInterfacesItemApi, mockXhr, getCusInterfacesItemListApi,
  interfacesSimpleListApi, deleteCusInterfacesItemApi } from '@/config/api/inserv-api'
import { generateParameters, fillParamsFromClipboardData, generateReturns, unionParams } from '@/config/utils'
import { ENV_DOMAIN_MAP, ENV_DOMAIN_LOG_MAP } from '@/config/mapping'
import { store } from '@/config/cache'

export default {
  props: {},
  data () {
    return {
      ENV_DOMAIN_MAP: (() => {
        const domain = Object.assign({}, ENV_DOMAIN_MAP)
        delete domain['onlineDomain']
        domain['localDomain'] = '本地环境'
        return domain
      })(),
      ENV_DOMAIN_LOG_MAP,
      interfacesSimpleListApi,
      data: {
        id: null,
        hash: null,
        name: null,
        url: null,
        method: null,
        parameters: null,
        returns: null,
        createTime: null,
        modifyTime: null,
        deprecated: null,
        end: {
          id: null,
          name: null,
          prefix: null,
          header: null
        },
        java: {
          id: null,
          name: null
        }
      },
      customInterfacesId: null,
      currentId: null,
      getCusInterfacesItemListApi,
      preParams: [],
      checkResult: null,
      checkResultMessage: null,
      javascript: null,
      method: 'GET',
      parameters: [],
      headers: [],
      env: 'testDomain',
      result: null,
      bodyParams: null,
      localDomainKey: 'LOCAL_DOMAIN_KEY',
      defaultLocalDomain: 'http://127.0.0.1:8080',
      start: null,
      end: null
    }
  },
  watch: {
    env (value) {
      this.loadHeaders()
    }
  },
  computed: {
    showUrl () {
      return generateUrl(this.data.java, this.env, this.data.url, this.parameters)
    }
  },
  mounted () {
    this.init()

    // 绑定参数粘帖事件
    this.$el.addEventListener('paste', this.paste)
    window.that = this
  },
  methods: {
    onSelectCusInterfacesItem (selections) {
      const item = selections[0] || {}
      this.loadItem(item)
    },
    configLocalEnv () {
      const key = this.localDomainKey + '_' + this.data.java.id
      this.$prompt('配置本地环境地址', '', {
        inputType: 'text',
        inputValue: store.get(key),
        inputPlaceholder: '请输入配置本地环境地址',
        inputValidator: (d) => (d || '').trim().length > 0 || '请输入配置本地环境地址'
      }).then((d) => {
        store.set(key, d.value)
        this.data.java.localDomain = d.value
      }).catch((d) => {
      })
    },
    paste (e) {
      fillParamsFromClipboardData(e, this.parameters)
    },
    async init () {
      this.env = this.$route.params.env || this.env
      this.data = await getInterfacesApi(this.$route.params.id)
      this.method = this.data.method || this.method
      this.data.java.localDomain = store.get(this.localDomainKey + '_' + this.data.java.id) || this.defaultLocalDomain
      this.parameters = generateParameters(this.data.parameters)
      const cus = await getCusInterfacesByInterfacesIdApi(this.$route.params.id)
      if (cus) {
        this.customInterfacesId = cus.id
        const itemList = await getCusInterfacesItemListApi({customInterfacesId: this.customInterfacesId})
        if (itemList.length > 0) {
          const item = itemList[0]
          this.loadItem(item)
        }
      }

      // 返回数据结构填充
      this.result = generateReturns(JSON.parse(this.data.returns))
    },
    loadItem (item) {
      this.currentId = item.id
      this.preParams = item.preParams != null ? JSON.parse(item.preParams) : []
      this.javascript = item.javascript != null ? JSON.parse(item.javascript) : null
      this.headers = JSON.parse(item.cusHeaders || '[]') || []

      if (item.cusParameters) {
        const cusParameters = JSON.parse(item.cusParameters)
        this.parameters = unionParams(cusParameters, this.parameters)
      } else {
        this.parameters = generateParameters(this.data.parameters)
      }
      this.loadHeaders()
    },
    loadHeaders (forceUpdate) {
      if (this.data.end.id) {
        getInterfacesHeadersApi(this.env, this.data.end.id, forceUpdate)
          .then(h => {
            this.headers = unionParams(h, this.headers, true)
          })
      }
    },
    async testUrl () {
      // method, parameters, headers
      this.end = null
      this.start = Date.now()
      const res = await mockXhr(this.env, this.showUrl, this.method, this.parameters, this.headers, this.preParams, this.javascript)
      this.end = Date.now()
      this.result = res.result.data
      this.checkResult = res.check
      this.checkResultMessage = res.message
    },
    openInterfacesSelectorDialog () {
      getPrivatesEndAuthApi(this.data.end.id)
        .then((r) => {
          const config = (r && r.config) ? JSON.parse(r.config) : {}
          this.$refs.interfacesSelector.open(config)
        })
    },
    savePrivatesAuth (data) {
      setPrivatesEndAuthApi(this.data.end.id, JSON.stringify(data))
        .then(() => {
          this.loadHeaders(true)
        })
    },
    save () {
      saveCusInterfacesItemApi(this.currentId, JSON.stringify(this.parameters), JSON.stringify(this.javascript), JSON.stringify(this.preParams), JSON.stringify(this.headers))
        .then((r) => {
          this.$success('保存成功！')
        })
    },
    saveAs () {
      this.$prompt('另存为', '', {
        inputType: 'text',
        inputPlaceholder: '请输入用例名称（不填自动命名 )'
      }).then((d) => {
        addCusInterfacesItemApi(this.data.id, d.value, JSON.stringify(this.parameters), JSON.stringify(this.javascript), JSON.stringify(this.preParams), JSON.stringify(this.headers))
          .then((r) => {
            this.currentId = r.id
            this.$success('另存为【' + r.name + '】成功！')
          })
      }).catch((d) => {
      })
    },
    run () {
      this.$router.push(`/flow/testapi/run/${this.customInterfacesId}`)
    },
    deleteItem () {
      this.$confirm('即将删除选中用例且无法恢复，确认删除吗？', '提示', {type: 'warning'}).then(() => {
        deleteCusInterfacesItemApi(this.currentId)
          .then(async () => {
            this.$success('删除成功！')

            const itemList = await getCusInterfacesItemListApi({customInterfacesId: this.customInterfacesId})
            if (itemList.length > 0) {
              const item = itemList[0]
              this.$refs.selector.deleteItem(this.currentId)

              this.loadItem(item)
            }
          })
      })
    },
    addParams () {
      this.parameters.push({key: '', value: '', type: 'text'})
    },
    addPreParams () {
      this.preParams.push({key: '', value: '', type: 'text'})
    },
    addHeader () {
      let obj = {key: '', value: ''}
      this.headers.push(obj)
    },
    delHeader (i) {
      this.headers.splice(i, 1)
    }
  },
  components: {
    'v-jsonformatter': () => import('@/components/jsonformatter'),
    'v-selector': () => import('@/components/selector'),
    'v-interfaces-selector': () => import('@/components/interfaces-selector'),
    'v-parameter-input': () => import('@/components/parameter-input')
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style type="text/css" lang="scss" scoped>
  @import 'index.scss';
</style>
