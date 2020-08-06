<template>
  <div>
    <h4 class="title">筛选条件</h4>
    <hr/>
    <div class="filters">
      <el-form :inline="true" :model="filter" label-width="6em">
        <el-form-item label="编号">
          <el-input type='number' v-model="filter.id" placeholder="请输入编号" @keypress.enter.native="search"></el-input>
        </el-form-item>
        <el-form-item label="环境：">
          <el-select v-model="filter.env">
            <el-option v-for="(value, key) in ENV_DOMAIN_MAP" :key="key" :label="value" :value="key"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="filter.name" placeholder="请输入名称" @keypress.enter.native="search"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search" icon="el-icon-search">搜索</el-button>
          <el-button type="info" @click="clear" icon="el-icon-delete">清空</el-button>
        </el-form-item>
      </el-form>
    </div>
    <h4 class="title">用例执行历史列表</h4>
    <hr/>
    <v-datagrid ref="dg" :columns="columns" :data-url="dataUrl" :count-url="countUrl" :params="params"/>
  </div>
</template>

<script>
import { testCaseHistoryCountApi, testCaseHistoryListApi, deleteTestCaseHistoryApi } from '@/config/api/inserv-api'
import { formatDate, removeBlank } from '@/config/utils'
import { ENV_DOMAIN_MAP } from '@/config/mapping'

export default {
  name: 'testCaseHistory',
  data () {
    return {
      ENV_DOMAIN_MAP,
      filter: {
        id: null,
        env: null,
        name: null
      },
      dataUrl: testCaseHistoryListApi,
      countUrl: testCaseHistoryCountApi,
      params: {},
      columns: [
        {
          field: 'action',
          header: '操作',
          width: 120,
          html: true,
          actions: [{
            text: '查看',
            handler: (row) => this.view(row.id)
          }, {
            text: '删除',
            handler: (row) => this.delete(row.id)
          }]
        },
        {field: 'id', header: '编号', sort: 'id', width: 100},
        {field: 'env', header: '环境', sort: 'env', width: 120, formatter: (row, index, value) => ENV_DOMAIN_MAP[value]},
        {field: 'name', header: '名称', sort: 'name', width: 220},
        {field: 'total', header: '执行次数', sort: 'total', width: 120},
        {field: 'success', header: '成功次数', sort: 'success', width: 120},
        {field: 'failed', header: '失败次数', sort: 'failed', width: 120},
        {field: 'createTime', header: '执行时间', sort: 'create_time', width: 230, formatter: (row, index, value) => formatDate(value)}
      ]
    }
  },
  mounted () {
    window.that = this
  },
  methods: {
    view (id) {
      this.$router.push(`/flow/testCase/history/${id}`)
    },
    delete (id) {
      console.log(id)
      this.$confirm('删除将无法恢复，确认删除吗？', '提示', {type: 'warning'}).then(() => {
        deleteTestCaseHistoryApi(id)
          .then(() => {
            this.$success('删除成功！')
            this.search()
          })
      })
    },
    search () {
      this.params = removeBlank(this.filter)
    },
    clear () {
      this.params = {}
      this.filter = {}
    }
  },
  components: {
    'v-datagrid': () => import('@/components/datagrid'),
    'v-selector': () => import('@/components/selector')
  }
}
</script>

<style lang="scss" scoped>
  @import 'index';
</style>
