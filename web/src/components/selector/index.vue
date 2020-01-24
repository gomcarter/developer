<template>
  <el-select :multiple="multiple" :filterable="filterable" :remote="remote" :clearable="clearable"
             :placeholder="placeholder || (multiple ? '请输入关键字搜索' : '请选择')" :remote-method="remoteMethod" :loading="loading"
             @change="onChange" v-model="selected" :remove-tag="onChange" :clear="remoteMethod"
             class="form-control" style="padding: 0;border: 0;" :disabled="disabledBol">
    <el-option
      v-for="(item, index) of selections"
      :key="index"
      :label="item[text]"
      :value="item[id]"
      :disabled="disabled.indexOf(item[id]) > -1">
    </el-option>
  </el-select>
</template>

/**
* @author ： 李银 on 2018-10-31 09:18:18
* 依据element-ui改装，原版请访问：http://element-cn.eleme.io/#/zh-CN/component/select
*
* 入参：
* id:String             - 在数据中的主键名称，如data为：[{a:1,b:2}],如果id为a，那么1为这个数据的主键，默认：id
* text:String           - 在数据中的用于显示的名称，如data为：[{a:1,b:2}],如果text为b，那么2为这个数据的显示元素，默认text
* multiple:Boolean      - 是否多选，默认：false
* placeholder:String    - 占位符，默认：请选择
* filterable:Boolean    - 是否可搜索，默认：false
* remote:Boolean        - 是否为远程搜索，默认：false
* data:Array/Object     - 可选项数据：必须至少包含id，text字段，id用于唯一标识，text字段用于展示，在remote为false下生效，如果data是object，则系统自动转化为Array形式，key为id，value为text
* url:String            - 远程请求数据url，返回数据格式和data一致，必须接收@searchKey 作为搜索参数
* searchKey:String      - 远程请求数据的参数名，默认text
* extraParams           - 远程请求数据的额外参数
* load:Array            - 默认加载已选择项，id即可，如：[1,2,3,4]
*
* api:
* getSelection          - 获取当前选择的项
* clear                 - 清空选项
*
* 回调：
* onSelectionChanged()  - 当前选择项发生变化时触发，参数为当前所有选择的项；
**/
<script>
import { xhr } from '@/config/api/http'

export default {
  name: 'selector',
  props: {
    id: {
      type: String,
      default: 'id'
    },
    text: {
      type: String,
      default: 'text'
    },
    searchKey: {
      type: String,
      default: null
    },
    multiple: {
      type: Boolean,
      default: false
    },
    placeholder: {
      type: String,
      default: null
    },
    filterable: {
      type: Boolean,
      default: false
    },
    remote: {
      type: Boolean,
      default: false
    },
    data: null,
    load: {
      type: Array,
      default: null
    },
    url: null,
    extraParams: {
      type: Object,
      default: null
    },
    onSelectionChanged: {
      type: Function,
      default: null
    },
    disabledBol: {
      type: Boolean,
      default: false
    },
    disabled: {
      type: Array,
      default: () => []
    },
    clearable: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      searchKey0: null,
      selections: [],
      selected: null,
      loading: false,
      params: {},
      mappedData: {}
    }
  },
  mounted () {
    setTimeout(() => {
      this.init()
    }, 100)
  },
  watch: {
    load () {
      const load = (this.load || []).filter(d => d != null)
      const selected = this.multiple ? [].concat(this.selected) : (this.selected == null ? [] : [this.selected])
      if (load.sort().join('|_|') === selected.sort().join('|_|')) {
        // not changed
        return
      }
      this.loadChanged()
    },
    extraParams () {
      const extraParams = this.extraParams || {}
      const params = this.params || {}
      if (JSON.stringify(extraParams) !== JSON.stringify(params)) {
        this.params = extraParams
        this.loadChanged(-1)
      }
    }
  },
  methods: {
    loadParams (load) {
      const loadParams = {}
      if (load.length > 0) {
        loadParams['rows'] = load.length
        loadParams[this.id] = load
        loadParams[`${this.id}List`] = load
      }
      return loadParams
    },
    loadChanged (p) {
      const load = (this.load || []).filter(d => d != null)
      if (p === -1) {
        // 初始化
        this.remoteMethod('', this.loadParams(load))
      } else if (load.length > 0) {
        // 后面修改load, 先看自己数据里面是否有当前数据，如果有则直接加载，没有就请求数据
        const selections = this.selections || []
        const matched = selections.filter(s => load.indexOf(s[this.id]) >= 0) || []

        // 没有匹配完全，请求远端数据来完成
        if (matched.length !== load.length && this.remote && this.url) {
          this.remoteMethod('', this.loadParams(load))
        } else {
          this._setSelectedAsLoad(load)
        }
      } else {
        // 清空选择项
        this.clear()
      }
    },
    _setSelectedAsLoad (load) {
      setTimeout(() => {
        if (this.multiple) {
          this.selected = load
        } else {
          this.selected = load.length > 0 ? load[0] : null
        }
      }, 1)
    },
    init () {
      this.params = this.extraParams || {}
      this.searchKey0 = this.searchKey || this.text
      if (this.data) {
        this.loadData(this.data)
      } else if (this.remote && this.url) {
        this.loadChanged(-1)
      }
    },
    loadData (data) {
      if (!(data instanceof Array)) {
        this.selections = Object.entries(data)
          .map(d => {
            return {id: d[0], text: d[1]}
          })
      } else {
        this.selections = data
      }

      // 将数据转化成map
      this.mappedData = {}
      this.selections.forEach((d) => {
        this.mappedData[d[this.id]] = d
      })

      // 加载需要选择的项
      const load = (this.load || []).filter(d => d != null)
      this._setSelectedAsLoad(load)
    },
    getSelection () {
      let selected = []
      if (this.multiple) {
        // 多选
        this.selected.forEach(d => selected.push(this.mappedData[d]))
      } else {
        // 单选
        const s = this.mappedData[this.selected]
        selected = s ? [this.mappedData[this.selected]] : []
      }
      return selected
    },
    clear () {
      this.selected = this.multiple ? [] : null
    },
    remoteMethod (text, loadParams) {
      if (this.remote && this.url) {
        text = (text || '').trim()

        this.loading = true
        let params = {}
        params[this.searchKey0] = text
        params = Object.assign(params, loadParams, this.params);
        // 如果url是字符串，则自行请求结果，否则直接执行url方法
        (typeof this.url === 'string' ? xhr.get(this.url, {params}).then((d) => {
          this.loading = false
          this.loadData(d.extra.data)
        }) : this.url(params)
          .then((d) => {
            this.loading = false
            this.loadData(d)
          }))
          .catch(() => {
            this.loading = false
            this.loadData([])
          })
      }
    },
    onChange () {
      const selected = this.getSelection()
      if (selected.length === 0) {
        this.remoteMethod()
      }
      // 通知外部选择项已修改
      if (this.onSelectionChanged) {
        this.onSelectionChanged(selected)
      }
    }
  }
}
</script>

<style scoped>
  @import 'index.scss';
</style>
