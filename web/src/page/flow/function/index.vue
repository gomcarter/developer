<template>
  <div>
    <h4 class="title">筛选条件</h4>
    <hr/>
    <div class="filters">
      <el-form :inline="true" :model="filter" label-width="6em" @submit.native.prevent>
        <el-form-item label="规则名称">
          <el-input v-model="filter.name" placeholder="请输入规则名称" @keypress.enter.native="search"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search" icon="el-icon-search">搜索</el-button>
          <el-button type="info" @click="clear" icon="el-icon-delete">清空</el-button>
        </el-form-item>
      </el-form>
    </div>
    <h4 class="title">自定义参数列表</h4>
    <hr/>
    <v-datagrid :columns="columns" :data-url="dataUrl" :count-url="countUrl" :params="params" :toolbar="toolbar"/>
  </div>
</template>
<script>
import { functionCountApi, functionListApi } from '@/config/api/inserv-api'
import { formatDate, removeBlank } from '@/config/utils'
export default {
  data () {
    return {
      filter: {
        name: ''
      },
      dataUrl: functionListApi,
      countUrl: functionCountApi,
      fixParams: { 'customFunctionDto.isPublic': true },
      params: { 'customFunctionDto.isPublic': true },
      toolbar: [{
        title: '新增',
        icon: 'el-icon-plus',
        handler: this.add
      }],
      columns: [
        {
          field: 'action',
          header: '操作',
          width: 160,
          actions: [
            {
              text: '编辑',
              handler: (row) => {
                this.$router.push(`/flow/function/edit/${row.id}`)
              }
            }
          ]
        },
        {field: 'id', header: '编号', sort: 'id', width: 100},
        {field: 'name', header: '规则名称', sort: 'name', width: 200},
        {field: 'isPublic', header: '是否公用', sort: 'is_public', width: 100, html: true, formatter: (row, index, value) => this.formatIsPublic(row, value)},
        {field: 'scriptText', header: '脚本', sort: 'scriptText', width: 500},
        {field: 'userName', header: '创建人', sort: 'user_name', width: 100},
        {field: 'mark', header: '备注', sort: 'mark', width: 400},
        {field: 'createTime', header: '添加时间', sort: 'create_time', width: 200, formatter: (row, index, value) => formatDate(value)},
        {field: 'modifyTime', header: '上次修改时间', sort: 'modify_time', width: 200, formatter: (row, index, value) => formatDate(value)}
      ]
    }
  },
  mounted () {
  },
  methods: {
    search () {
      this.params = Object.assign({}, this.fixParams, removeBlank(this.filter))
    },
    clear () {
      this.params = Object.assign({}, this.fixParams)
      this.filter = { name: '' }
    },
    add () {
      this.$router.push(`/flow/function/edit`)
    },
    formatIsPublic (row, value) {
      if (value) {
        return '是 <i title=' + row['userName'] + ' class="el-icon-warning-outline"></i>'
      } else {
        return '否'
      }
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
