<template>
  <div>
    <h4 class="title">筛选条件</h4>
    <hr/>
    <div class="filters">
      <el-form :inline="true" :model="filter" label-width="6em">
        <el-form-item label="编号">
          <el-input v-model="filter.id" placeholder="请输入编号" ></el-input>
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="filter.name" placeholder="请输入名称" ></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="filter.mark" placeholder="请输入备注" ></el-input>
        </el-form-item>
        <el-form-item label="创建人">
          <el-input v-model="filter.userName" placeholder="请输入账号" ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search" icon="el-icon-search">搜索</el-button>
          <el-button type="info" @click="clear" icon="el-icon-delete">清空</el-button>
        </el-form-item>
      </el-form>
    </div>
    <h4 class="title">接口打包列表</h4>
    <hr/>
    <v-datagrid :columns="columns" :data-url="dataUrl" :count-url="countUrl" :params="params"/>
  </div>
</template>

<script>
import { packageCountApi, packageListApi, deletePackageApi } from '@/config/api/inserv-api'
import { formatDate, removeBlank } from '@/config/utils'

export default {
  name: 'packageList',
  data () {
    return {
      filter: {
        id: null,
        name: null,
        userName: null,
        mark: null
      },
      dataUrl: packageListApi,
      countUrl: packageCountApi,
      params: {},
      columns: [
        {field: 'id', header: '编号', sort: 'id', width: 80},
        {field: 'userName', header: '创建人', sort: 'user_name', width: 200},
        {field: 'name', header: '名称', sort: 'name', width: 300},
        {field: 'mark', header: '备注', sort: 'mark', width: 500},
        {field: 'createTime', header: '创建时间', sort: 'create_time', width: 200, formatter: (row, index, value) => formatDate(value)},
        {
          field: 'action',
          header: '操作',
          width: 130,
          html: true,
          actions: [{
            text: '查看',
            handler: (row) => this.$router.push(`/package/view/${row.id}`)
          }, {
            text: '删除',
            handler: (row) => this.delete(row.id)
          }]
        }
      ]
    }
  },
  mounted () {
    window.that = this
  },
  methods: {
    delete (id) {
      this.$confirm('删除将无法恢复，确认删除吗？', '提示', {type: 'warning'}).then(() => {
        deletePackageApi(id)
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
    'v-selector': () => import('@/components/selector'),
    'v-dialog': () => import('@/components/dialog')
  }
}
</script>

<style lang="scss" scoped>
  @import 'index.scss';
</style>
