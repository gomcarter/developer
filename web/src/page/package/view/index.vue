<template>
  <div>
    <h4 class="title">基本信息</h4>
    <hr/>
    <el-form label-width="6em">
      <el-form-item label="编号：">{{data.id}}</el-form-item>
      <el-form-item label="名称：">{{data.name}}</el-form-item>
      <el-form-item label="创建人：">{{data.userName}}</el-form-item>
      <el-form-item label="备注："><div class="mark">{{data.mark}}</div></el-form-item>
    </el-form>
    <h4 class="title">打包接口列表</h4>
    <hr/>
    <v-datagrid v-if="data && data.id " :columns="columns" :checkable="true"
                :pageable="false" :load-data="interfacesList" :toolbar="toolbar" ref="dg"/>
    <h4 class="title" style="margin-top: 40px">接口自动聚合</h4>
    <hr/>
    <el-form>
      <el-form-item>
        <v-aggregation-editor ref="editor" v-if="initialized" :workflow="workflow" :on-open="onOpen"></v-aggregation-editor>
      </el-form-item>
      <el-form-item>
        <el-button type="info" @click="$router.go(-1)" icon="el-icon-back">返回</el-button>
        <el-button type="success" @click="save" icon="el-icon-check">保存</el-button>
        <el-button type="success" @click="run" :icon="disabled?'el-icon-loading':'el-icon-magic-stick'" :disabled="disabled">测试运行</el-button>
      </el-form-item>
    </el-form>
    <v-dialog ref="chooseInterfacesDialog" title="选择接口" :ok="batchImport" :width="1300">
      <div slot="body" style="height: 600px; overflow: auto">
        <v-interfaces-list ref="interfacesList" :no-toolbar="true"></v-interfaces-list>
      </div>
    </v-dialog>
  </div>
</template>

<script>
import { getPackageApi, interfacesListApi, updatePackageApi } from '@/config/api/inserv-api'
import { formatDate } from '@/config/utils'

export default {
  name: 'packageView',
  data () {
    return {
      initialized: false,
      disabled: false,
      data: {
        id: null,
        name: null,
        userName: null,
        mark: null
      },
      interfacesList: [],
      workflow: null,
      pageable: false,
      params: {},
      toolbar: [{
        title: '导入接口',
        icon: 'el-icon-plus',
        handler: () => this.$refs.chooseInterfacesDialog.open()
      }, {
        title: '删除',
        icon: 'el-icon-delete',
        handler: this.delete
      }],
      columns: [
        {
          field: 'action',
          header: '操作',
          width: 130,
          html: true,
          actions: [{
            text: (row) => `<a href="#/interfaces/view/${row.id}" target="_blank">查看</a>`
          }, {
            text: '复制',
            handler: (row) => this.copy(row)
          }]
        },
        {field: 'id', header: '编号', sort: 'id', width: 80},
        {field: 'name', header: '接口名称', sort: 'name', width: 200},
        {field: 'method', header: 'METHOD', sort: 'method', width: 80},
        {field: 'deprecated', header: '废弃', sort: 'deprecated', html: true, width: 80, formatter: (row, index, value) => value ? '是'.fontcolor('red') : '否'},
        {field: 'controller', header: '控制器', sort: 'controller', width: 200},
        {field: 'url', header: 'URL', sort: 'url', width: 120},
        {field: 'java.name', header: '后端服务', sort: 'fk_java_id', width: 120},
        {field: 'end.name', header: '前端项目', sort: 'fk_end_id', width: 160},
        {field: 'modifyTime', header: '更新时间', sort: 'modify_time', width: 200, formatter: (row, index, value) => formatDate(value)}
      ]
    }
  },
  mounted () {
    window.tta = this
    this.width = this.$el.clientWidth - 100

    if (this.$route.params.id) {
      getPackageApi(this.$route.params.id).then((res) => {
        this.data = res
        this.workflow = this.data.config ? JSON.parse(this.data.config) : null
        this.initialized = true
        interfacesListApi({rows: 1000, idList: this.data.interfacesIdList})
          .then(d => {
            this.interfacesList = d
          })
      })
    }
  },
  methods: {
    copy (row) {
      // 复制url为固定格式（沙翼需求），如：/platform/search/productWord/delete => ,platformSearchProductWordDelete:'/platform/search/productWord/delete'
      const key = row.url.split('/').filter(s => s).map(s => s[0].toUpperCase() + s.substr(1)).join('')
      this.$copyText(`,${key[0].toLowerCase() + key.substr(1)}:'${row.url}'`)
        .then((e) => this.$success('复制成功！'), (e) => this.$success('复制失败！'))
    },
    delete () {
      const selections = this.$refs.dg.getSelected()
      if (selections.length === 0) {
        this.$alert('请至少选择一个接口', '提示', {type: 'error'})
        return
      }
      this.$confirm('你即将删除节点，图中节点也将被同步删除，是否继续？', '提示', {type: 'warning'}).then(() => {
        const toBeRemovedIdList = []
        selections.forEach(s => {
          const index = this.interfacesList.indexOf(s)
          if (index >= 0) {
            this.interfacesList.splice(index, 1)
            toBeRemovedIdList.push(s.id)
          }
        })
        this.$refs.editor.batchDeleteNodes(toBeRemovedIdList)
        this.$refs.dg.clearSelections()
      })
    },
    run () {
      this.disabled = true
      this.$refs.editor.run()
    },
    save () {
      const config = this.$refs.editor.getWorkflow()
      updatePackageApi(this.$route.params.id, {
        interfacesIdList: this.interfacesList.map(s => s.id),
        // 节点配置，最终呈现和接口聚合之后的规则
        config: config ? JSON.stringify(config) : null
      }).then(() => {
        this.$transfer({
          back: '返回编辑',
          buttons: [{
            text: '去列表',
            link: '/package/list'
          }]
        })
      })
    },
    finished () {
      this.disabled = false
    },
    onOpen () {
      this.$refs.editor.batchAddNodes(this.interfacesList)
    },
    batchImport () {
      const selections = this.$refs.interfacesList.getSelections()
      if (selections.length === 0) {
        this.$alert('请至少选择一个接口', '提示', {type: 'error'})
        return
      }

      const newIdList = selections.map(s => s.id)
      const existIdList = this.interfacesList.map(s => s.id)
      const toBeImportIdList = []
      newIdList.forEach(s => {
        if (existIdList.indexOf(s) < 0) {
          toBeImportIdList.push(s)
        }
      })
      if (toBeImportIdList.length > 0) {
        interfacesListApi({rows: 1000, idList: toBeImportIdList})
          .then(d => {
            this.$success('接口导入成功，重复接口被去除。')
            this.$refs.interfacesList.clearSelections()
            this.interfacesList = this.interfacesList.concat(d)

            this.$refs.editor.batchAddNodes(d)
            this.$refs.chooseInterfacesDialog.close()
          })
      }
    }
  },
  components: {
    'v-datagrid': () => import('@/components/datagrid'),
    'v-selector': () => import('@/components/selector'),
    'v-dialog': () => import('@/components/dialog'),
    'v-aggregation-editor': () => import('@/components/aggregation-editor'),
    'v-interfaces-list': () => import('@/page/interfaces')
  }
}
</script>

<style lang="scss" scoped>
  @import 'index.scss';
</style>
