<template>
  <div>
    <h4 class="title">筛选条件</h4>
    <hr/>
    <div class="filters">
      <el-form :inline="true" :model="filter" label-width="6em" @submit.native.prevent>
        <el-form-item label="用例名称">
          <el-input v-model="filter.name" placeholder="请输入用例名称" @keypress.enter.native="search"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search" icon="el-icon-search">搜索</el-button>
          <el-button type="info" @click="clear" icon="el-icon-delete">清空</el-button>
        </el-form-item>
      </el-form>
    </div>
    <h4 class="title">用例列表</h4>
    <hr/>
    <v-datagrid :columns="columns" :data-url="dataUrl" :count-url="countUrl" :params="params" :toolbar="toolbar"/>
  </div>
</template>

<script>
import { testCaseCountApi, testCaseListApi, copyTestCaseApi, deleteTestCaseApi } from '@/config/api/inserv-api'
import { formatDate, removeBlank } from '@/config/utils'

export default {
  data () {
    return {
      filter: {
        name: ''
      },
      dataUrl: testCaseListApi,
      countUrl: testCaseCountApi,
      params: {},
      toolbar: [{
        title: '新增',
        icon: 'el-icon-plus',
        handler: this.add
      }],
      columns: [
        {field: 'id', header: '用例id', sort: 'id', width: 200},
        {field: 'name', header: '用例名称', sort: 'name', width: 200},
        // {field: 'mark', header: '备注', sort: 'mark', width: 400},
        {field: 'userName', header: '操作人', sort: 'user_name', width: 200},
        {field: 'createTime', header: '添加时间', sort: 'create_time', width: 200, formatter: (row, index, value) => formatDate(value)},
        {field: 'modifyTime', header: '上次修改时间', sort: 'modify_time', width: 200, formatter: (row, index, value) => formatDate(value)},
        {
          field: 'action',
          header: '操作',
          width: 360,
          actions: [
            {
              text: '【编辑】',
              handler: (row) => {
                this.$router.push(`/flow/testCase/edit/${row.id}`)
              }
            },
            {
              text: '【复制】',
              handler: (row) => {
                this.copy(row.id)
              }
            },
            {
              text: '【删除】',
              handler: (row) => {
                this.delete(row.id)
              }
            },
            {
              text: '【执行】',
              handler: (row) => {
                this.$router.push(`/flow/testCase/run/${row.id}`)
              }
            }
          ]
        }
      ]
    }
  },
  methods: {
    delete (id) {
      this.$confirm('删除将无法恢复，确认删除吗？', '提示', {type: 'warning'}).then(() => {
        deleteTestCaseApi(id)
          .then(() => {
            this.$success('删除成功！')
            this.search()
          })
      })
    },
    copy (id) {
      this.$confirm('复制将产生一条新的记录，确认吗？', '提示', {type: 'info'}).then(() => {
        copyTestCaseApi(id)
          .then(() => {
            this.$success('复制成功！')
            this.search()
          })
      })
    },
    search () {
      this.params = removeBlank(this.filter)
    },
    clear () {
      this.params = {}
      this.filter = { name: '' }
    },
    add () {
      this.$router.push('/flow/testCase/edit')
    }
  },
  components: {
    'v-datagrid': () => import('@/components/datagrid')
  }
}
</script>

<style lang="scss" scoped>
  @import 'index.scss';
</style>
