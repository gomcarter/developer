<template>
  <div>
    <h4 class="title">筛选条件</h4>
    <hr/>
    <div class="filters">
      <el-form :inline="true" :model="filter" label-width="6em">
        <el-form-item label="序号">
          <el-input v-model="filter.id" placeholder="请输入序号" @keypress.enter.native="search"></el-input>
        </el-form-item>
        <el-form-item label="项目名称">
          <el-input v-model="filter.name" placeholder="输入项目名称" @keypress.enter.native="search"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search" icon="el-icon-search">搜索</el-button>
          <el-button type="info" @click="clear" icon="el-icon-delete">清空</el-button>
        </el-form-item>
      </el-form>
    </div>
    <h4 class="title">前端项目列表</h4>
    <hr/>
    <v-datagrid :columns="columns" :data-url="dataUrl" :count-url="countUrl" :params="params" :toolbar="toolbar"/>

    <v-interfaces-selector ref="interfacesSelector" :on-ok="savePrivatesAuth"></v-interfaces-selector>
  </div>
</template>

<script>
import { endCountApi, endListApi, getPrivatesEndAuthApi, setPrivatesEndAuthApi } from '@/config/api/inserv-api'
import { formatDate, removeBlank } from '@/config/utils'

export default {
  name: 'end',
  data () {
    return {
      filter: {
        id: null,
        name: null
      },
      dataUrl: endListApi,
      countUrl: endCountApi,
      params: {},
      editingId: null,
      toolbar: [{
        title: '新增',
        icon: 'el-icon-plus',
        handler: () => this.$router.push(`/consumer/list/edit`)
      }],
      columns: [
        {
          field: 'action',
          header: '操作',
          width: 230,
          actions: [{
            text: '编辑',
            handler: (row) => {
              this.$router.push(`/consumer/list/edit/${row.id}`)
            }
          }, {
            text: '个性设置鉴权',
            handler: (row) => {
              this.openInterfacesSelectorDialog(row)
            }
          }]
        },
        {field: 'id', header: '序号', sort: 'id', width: 80},
        {field: 'name', header: '项目名称', sort: 'name', width: 300},
        {field: 'prefix', header: '前缀', sort: 'prefix', width: 100},
        {
          field: 'action1',
          header: '鉴权接口',
          width: 120,
          actions: [{
            text: (row) => {
              return row.config ? `<a href="#/interfaces/view/${JSON.parse(row.config).interfaceId}" target="_blank">点击查看</a>` : ''
            }
          }]
        },
        {field: 'mark', header: '备注', sort: 'mark', width: 220},
        {field: 'createTime', header: '添加时间', sort: 'create_time', width: 200, formatter: (row, index, value) => formatDate(value)}
      ]
    }
  },
  mounted () {
    if (this.action) {
      this.columns.push({field: 'action', header: '操作', sort: 'id', width: 230, actions: this.action || []})
    }
  },
  methods: {
    search () {
      this.params = removeBlank(this.filter)
    },
    clear () {
      this.params = {}
      this.filter = { name: '' }
    },
    openInterfacesSelectorDialog (row) {
      this.editingId = row.id
      getPrivatesEndAuthApi(row.id)
        .then((r) => {
          const config = (r && r.config) ? JSON.parse(r.config) : {}
          this.$refs.interfacesSelector.open(config)
        })
    },
    savePrivatesAuth (data) {
      setPrivatesEndAuthApi(this.editingId, JSON.stringify(data))
    }
  },
  components: {
    'v-datagrid': () => import('@/components/datagrid'),
    'v-interfaces-selector': () => import('@/components/interfaces-selector')
  }
}
</script>
<style lang="scss" scoped>
  @import 'index.scss';
</style>
