<template>
  <table class="datagrid-container datagrid-frame" :style="{'width': width ? width + 'px' : 'auto'}">
    <thead class="datagrid-header">
    <tr class="datagrid-title" v-if=" (title||'').trim().length > 0">
      <th :colspan="columns.length + checkable + (+!!children)">
        <p class="datagrid-title-text">{{ title }}</p>
      </th>
    </tr>
    <tr class="datagrid-toolbar" v-if="toolbar && toolbar.length > 0">
      <th :colspan="columns.length + checkable + (+!!children)">
        <el-button type="primary" v-for="(item, index) in toolbar" @click="item.handler && item.handler()" :key="index" :icon="item.icon"
                   class="datagrid-toolbar-button" :disabled="(item.disabled && item.disabled()) || t*0 !== 0">{{ item.title }}</el-button>
      </th>
    </tr>
    <tr class="datagrid-header-row" v-if="columns != null  && columns.length > 0">
      <th class="datagrid-row-expander-all" v-if="children">
        <span :class="{'el-icon-plus': !expandAll, 'el-icon-minus': expandAll}" @click="expandAllToggle"></span>
      </th>
      <th class="datagrid-header-check" v-if="checkable">
        <el-checkbox v-model="checkAll" v-if="!singleCheck && t" :disabled="(this.data || []).length === 0"></el-checkbox>
      </th>
      <th class="datagrid-header-cell" v-for="(column, index) in columns" :key="index"
          :style="{ width: column.width > 0 ? (column.width + 'px') : 'auto'}"
          @click="onHeaderClicked(column)">
        {{ column.header }}
        <span :class="{'el-icon-caret-top' : sort === column.sort && (order ||'').toUpperCase() === 'ASC',
            'el-icon-caret-bottom': sort === column.sort && (order ||'').toUpperCase() === 'DESC',
            'el-icon-d-caret': column.sort && sort !== column.sort}"></span>
      </th>
    </tr>
    </thead>
    <tbody class="datagrid-body" >
    <template v-for="(row, rowIndex) in data"
              v-if="onBeforeRenderRow == null || onBeforeRenderRow(row, rowIndex) !== false">
      <tr class="datagrid-body-row" @dblclick="expandRowToggle(row)" :key="'r_' + rowIndex">
        <td class="datagrid-row-expander" v-if="children">
          <span :class="{'el-icon-plus': !row.$expanded, 'el-icon-minus': !!row.$expanded}"
                @click="expandRowToggle(row)"></span>
        </td>
        <td class="datagrid-row-check" v-if="checkable">
          <el-checkbox v-model="row.$checked" @change="onRowCheckChanged(row)"></el-checkbox>
        </td>

        <td class="datagrid-body-td" v-for="(col, colIndex) in columns"
            :style="{ width: col.width > 0 ? (col.width + 'px') : 'auto'}" :key="colIndex">
          <div class="datagrid-body-cell" v-if="col.html" v-html="computeValue(col, row, rowIndex)"></div>
          <div class="datagrid-body-cell" v-if="!col.html"> {{computeValue(col, row, rowIndex)}}</div>
          <div class="datagrid-body-cell" v-if="col.actions != null && col.actions.length > 0">
            <a class="datagrid-action" v-for="(action, index) in col.actions"
               v-if="action.show != null ? action.show(row) === true: true"
               @click="action.handler != null && action.handler(row, rowIndex)" :key="index">
              <el-badge :value="action.badge && action.badge.value(row, rowIndex)"
                        :hidden="!(action.badge && action.badge.show && action.badge.show(row))"
                        :is-dot="action.badge && action.badge.isDot"
                        :max="action.badge && action.badge.max">
                <span v-html="typeof action.text === 'string' ? action.text : action.text(row, rowIndex)"></span>
              </el-badge>
            </a>
          </div>
        </td>
      </tr>
      <tr class="datagrid-body-row-child" :key="'d_' + rowIndex" v-if="children && row.$expanded" >
        <td class="datagrid-body-td" v-if="children"></td>
        <td class="datagrid-body-td" v-if="checkable"></td>
        <td class="datagrid-body-td" :colspan="columns.length">
          <v-datagrid :width="children.width" :checkable="children.checkable" :singleCheck="children.singleCheck"
                      :pageable="children.pageable" :title="children.title" :sortBy="children.sortBy"
                      :orderBy="children.orderBy" :toolbar="children.toolbar" :columns="children.columns"
                      :data-url="children.dataUrl" :loadData="children.loadData" :onBeforeLoad="children.onBeforeLoad"
                      :count-url="children.countUrl" :params="(children.params && children.params(row, rowIndex)) || {}" ></v-datagrid>
        </td>
      </tr>
    </template>
    <tr v-if="!data || data.length === 0" class="datagrid-body-row">
      <td class="datagrid-body-td" :colspan="columns.length + checkable + (+!!children)">暂无数据</td>
    </tr>
    <tr class="datagrid-footer">
      <td :colspan="columns.length + checkable + (+!!children)">
        <pager @onChanged="onPagerChanged" v-if="pageable" :page="page" :total="total" :rows="rows"></pager>
      </td>
    </tr>
    </tbody>
  </table>
</template>

/**
* @author ： 李银 on 2018年5月24日 23:55:12
*
* 入参：
* width:String,       - 具体宽度: 如：100px、100%、auto（默认）
* children:Object     - 行的子节点，和本身的参数一样。
* checkable:Boolean   - 是否支持数据行被check选中， 默认为false（不支持）
* singleCheck:Boolean - 是否单选，默认false（多选）
* pageable:Boolean    - 是否支持分页，默认为true（支持）
* title:String        - title，默认没有
* sortBy:String       - 按什么字段排序，默认不排序
* orderBy:String      - ASC（正序）/DESC（倒序）排序，默认不排序
* toolbar:Array       - 工具栏，格式为：{title:'toolbar',disabled: () => {}, handler: () => { doSomething }}
* dataUrl:String      - 数据请求的url路径，不设置则不请求数据
* countUrl:String     - 数据条数请求的url路径，当pageable为true时有效，不设置则不请求总条数（分页不起效）
* params:String       - 请求参数，当改变时，自动请求数据
* loadData:Array      - 外部传入数据（不使用自带请求远程接口获取数据），当此项不为空时，以上参数皆失效
* columns:Array       - 必填项，数据表格的头信息格式如下：
*                     -{
*                     -  field: '表示接口返回的字段标识, 支持a.b.c格式，如：返回数据为{a : {b : {c : 1}}}, 那么field写为 a.b.c即可',
*                     -  header: '显示头的名称',
*                     -  width: '宽度，整数',
*                     -  html: true/false -- 表示当前内容上文本还是html代码,
*                     -  formatter(row, index, value) { 如果返回值需要格式化，可以使用此方法。比如时间戳转时间字符串等等，可以返回html
*                     -   return value
*                     -  },
*                     -  sort: '排序字段，基于field字段的下划线命名法（UnderScoreCase），点击头部将重新请求数据'
*                     -  actions: [{
*                     -   text: '按钮显示的内容，可以写html代码',
*                     -   show(row) {}, return true 表示这个按钮要显示，否则不显示, row为当前行数据
*                     -   handler(row, index) { }, 处理器，参数：row-当前行数据，index当前行所属数据的第几行
*                     -  }, {...}]
*                     -}
*
* api:
* getSelected()       - 返回checked的数据
* reload()            - 刷新
* clearSelections()   - 清除选择项
*
* 回调：
* onSelected(row)               - 用户选择一行时，回调此方法，参数：选中行。
* onUnSelected(row)             - 用户取消选择一行时，回调此方法，参数：取消选中行。
* onSelectionChanged(row)       - 选择项发生变化时，回调此方法，参数：选中行。
* onBeforeLoad(params)          - 在即将请求远程数据数据之前触发此事件，params是参数，return false将取消加载数据
* onLoadSuccess(data)           - 在请求远程数据完毕时触发此事件，data是请求回来的数据，return false将取消渲染列表
* onBeforeRenderRow(row, index) - 在渲染某一行数据之前触发此事件，row是该行数据，index是数据第几行，return false将需要渲染该行数据
**/
<script>
import pager from '@/components/pager'
import { Loading } from 'element-ui'
import md5 from 'js-md5'

export default {
  props: {
    width: null,
    height: null,
    // 子节点
    children: {
      type: Object,
      default: null
    },
    // 是否可以有checkbox
    checkable: {
      type: Boolean,
      default: false
    },
    // 是否单选，默认false（多选）
    singleCheck: {
      type: Boolean,
      default: false
    },
    // 是否支持分页
    pageable: {
      type: Boolean,
      default: true
    },
    // datagrid的标题
    title: {
      type: String,
      default: null
    },
    // 根据那个字段排序
    sortBy: {
      type: String,
      default: null
    },
    // 排序方式：ASC-正序，DESC-倒序
    orderBy: {
      type: String,
      default: null
    },
    // 格式为：{title:'toolbar',icon:'icon',handler(){ doSomething }}
    toolbar: {
      type: Array,
      default: () => []
    },
    // 头部 格式：{ field: 'id', header: '序号', sort: 'id', width: 50 }
    columns: {
      type: Array,
      default: () => [],
      required: true
    },
    // 请求数据的url
    dataUrl: {
      type: Function,
      default: null
    },
    // 计算总数的url， pageable为false时，此项无效
    countUrl: {
      type: Function,
      default: null
    },
    // 外部传入的参数
    params: {
      type: Object,
      default: null
    },
    loadData: {
      type: Array,
      default: null
    },
    onBeforeLoad: {
      type: Function,
      default: null
    },
    onLoadSuccess: {
      type: Function,
      default: null
    },
    onBeforeRenderRow: {
      type: Function,
      default: null
    },
    onSelected: {
      type: Function,
      default: null
    },
    onUnSelected: {
      type: Function,
      default: null
    },
    onSelectionChanged: {
      type: Function,
      default: null
    }
  },
  data () {
    return {
      expandAll: false,
      t: 1,
      // 分页总条数
      total: 0,
      // 当前页数
      page: 1,
      // 当前页数据条数
      rows: 20,
      // 根据那个字段排序
      sort: null,
      // 排序方式：ASC-正序，DESC-倒序
      order: null,
      requestParams: {},
      data: [],
      selected: {},
      // 参数是否已经改变，如果没改变，就不需要去重新请求总条数
      paramsChanged: true
    }
  },
  mounted () {
    this.sort = this.sortBy
    this.order = this.orderBy

    const p = this.getPagerParams()
    const o = this.getOrderParams()
    // 修改参数，datagrid将重新拉取数据
    this.requestParams = Object.assign(p, o, this.params)

    this.formatData(this.loadData)
  },
  computed: {
    checkAll: {
      get () {
        return this.data && this.data.length > 0 && this.data.filter(s => !s.$checked).length === 0
      },
      set (checked) {
        let changed = false
        this.data.forEach(s => {
          if (s.$checked !== checked) {
            this.$set(s, '$checked', checked)
            if (checked) {
              this.selected[s.$hash] = s
              if (this.onSelected) {
                this.onSelected(s)
              }
            } else {
              delete this.selected[s.$hash]

              if (this.onUnSelected) {
                this.onUnSelected(s)
              }
            }

            changed = true
          }
        })
        if (changed) {
          this.t = this.t + 1
          if (this.onSelectionChanged) {
            this.onSelectionChanged(this.getSelected())
          }
        }
      }
    }
  },
  watch: {
    params () {
      this.paramsChanged = true
      this.requestParams = Object.assign({}, this.params,
        this.getPagerParams())
      // 这里需要pager初始化；
      this.total = 0
      this.page = 1
    },
    loadData () {
      this.formatData(this.loadData)
    },
    requestParams () {
      // 重新请求数据
      if (this.loadData === null) {
        this.loadRemoteData()
      }
    }
  },
  methods: {
    clearSelections () {
      this.data.forEach(s => {
        s.$checked = false
      })
      Object.entries(this.selected).forEach(s => delete this.selected[s[0]])
      if (this.onSelectionChanged) {
        this.onSelectionChanged([])
      }
      this.t = this.t + 1
    },
    formatData (input) {
      // 清空原来数据
      this.data.length = 0;
      // 把新数据加入
      (input || []).forEach(s => {
        if (this.checkable) {
          s.$hash = md5(JSON.stringify(s))
          s.$checked = !!this.selected[s.$hash]
        }
        this.data.push(s)
      })
      this.t += 1
    },
    onRowCheckChanged (row) {
      if (row.$checked) {
        // 勾中，如果是单选，需要把其他选中的删除
        if (this.singleCheck) {
          this.data.forEach(s => {
            if (row.$hash !== s.$hash) {
              if (s.$checked && this.onUnSelected) {
                this.onUnSelected(row)
              }

              s.$checked = false
            }
          })

          // 清空所有 selected
          Object.entries(this.selected).forEach(s => delete this.selected[s[0]])
        }
        this.selected[row.$hash] = row

        if (this.onSelected) {
          this.onSelected(row)
        }
      } else {
        delete this.selected[row.$hash]

        if (this.onUnSelected) {
          this.onUnSelected(row)
        }
      }
      this.t += 1

      if (this.onSelectionChanged) {
        this.onSelectionChanged(this.getSelected())
      }
    },
    expandAllToggle () {
      this.expandAll = !this.expandAll
      this.data.map(r => {
        this.$set(r, '$expanded', this.expandAll)
      })
    },
    expandRowToggle (row) {
      this.$set(row, '$expanded', !row.$expanded)
      // row.$expanded = !row.$expanded
      this.expandAll = this.data && this.data.length > 0 && this.data.filter(s => !s.$expanded).length === 0
    },
    getSelected () {
      return Object.entries(this.selected).map(s => s[1])
    },
    reload () {
      this.paramsChanged = true
      this.loadRemoteData()
    },
    computeValue (col, row, index) {
      const field = col.field || ''
      const fieldSplits = field.split('.')
      const formatter = col.formatter
      let value = row

      if (field) {
        fieldSplits.forEach((s) => {
          value = value[s] != null ? value[s] : ''
        })
      } else {
        value = ''
      }
      if (formatter) {
        value = formatter(row, index, value)
      }
      return value
    },
    getPagerParams () {
      // 支持分页的时候才有分页参数
      return this.pageable ? ({ page: this.page, rows: this.rows }) : ({})
    },
    getOrderParams () {
      const p = {}
      if (this.sort) {
        p.sort = this.sort
      }

      if (this.order) {
        p.order = this.order
      }

      return p
    },
    loadRemoteData () {
      // 查询数据
      if (this.dataUrl) {
        if (this.onBeforeLoad != null && this.onBeforeLoad(this.requestParams) === false) {
          return
        }
        const loading = Loading.service({
          text: '数据加载中...',
          target: this.$el
        })
        this.dataUrl(this.requestParams)
          .then((res) => {
            loading.close()
            if (this.onLoadSuccess != null && this.onLoadSuccess(res) === false) {
              return
            }
            this.formatData(res)
            this.expandAll = false
          }).catch(() => {
            loading.close()
          })
      }

      // 查询总条数
      if (this.pageable && this.countUrl && this.paramsChanged) {
        this.countUrl(this.requestParams)
          .then((t) => {
            this.total = t
            this.paramsChanged = false
          }).catch(() => {
            this.paramsChanged = false
            this.total = 0
          })
      }
    },
    // pager通知分页参数改变
    onPagerChanged (p = {}) {
      if (this.rows !== p.rows) {
        this.paramsChanged = true
      }

      if (this.rows === p.rows && this.page === p.page) {
        // change nothing
        return
      }

      this.rows = p.rows
      this.page = p.page
      this.requestParams = Object.assign({}, this.requestParams, this.getPagerParams())
    },
    onHeaderClicked (column) {
      const sort = column.sort
      // 每页设置sort，那么就不sort
      if (!sort) {
        return
      }
      if (this.sort === sort) {
        this.order = (this.order || '').toUpperCase() === 'ASC' ? 'DESC' : 'ASC'
      } else {
        this.sort = sort
        this.order = 'DESC'
      }
      this.requestParams = Object.assign({}, this.requestParams, this.getOrderParams())
    }
  },
  components: {
    pager,
    'v-datagrid': () => import('@/components/datagrid')
  }
}

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style type="text/css" lang="scss" scoped>
  @import 'index.scss';
</style>
