<template>
  <div>
    <h4 class="title">筛选条件</h4>
    <hr/>
    <div class="filters">
      <el-form :inline="true" :model="filter" label-width="6em">
        <el-form-item label="序号">
          <el-input v-model="filter.id" placeholder="请输入序号" @keypress.enter.native="search"></el-input>
        </el-form-item>
        <el-form-item label="模块名称">
          <el-input v-model="filter.name" placeholder="请输入模块名称" @keypress.enter.native="search"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search" icon="el-icon-search">搜索</el-button>
          <el-button type="info" @click="clear" icon="el-icon-delete">清空</el-button>
        </el-form-item>
      </el-form>
    </div>
    <h4 class="title">后端服务列表</h4>
    <hr/>
    <v-datagrid :columns="columns" :data-url="dataUrl" :count-url="countUrl" :params="params" :toolbar="toolbar"/>
  </div>
</template>

<script>
import { javaCountApi, javaListApi } from '@/config/api/inserv-api'
import { formatDate, removeBlank } from '@/config/utils'
import { ENV_DOMAIN_MAP } from '@/config/mapping'

export default {
  name: 'java',
  data () {
    return {
      filter: {
        id: null,
        name: null
      },
      dataUrl: javaListApi,
      countUrl: javaCountApi,
      params: {},
      toolbar: [{
        title: '新增',
        icon: 'el-icon-plus',
        handler: this.edit
      }],
      columns: [
        {
          field: 'action',
          header: '操作',
          width: 130,
          actions: [
            {
              text: '编辑',
              handler: (row) => {
                this.$router.push(`/provider/list/edit/${row.id}`)
              }
            }
          ]
        },
        {field: 'id', header: '序号', sort: 'id', width: 50},
        {field: 'name', header: '模块名称', sort: 'name', width: 200},
        {field: 'alias', header: '应用名', sort: 'alias', width: 200},
        {field: 'createTime', header: '添加时间', sort: 'create_time', width: 200, formatter: (row, index, value) => formatDate(value)}
      ]
    }
  },
  mounted () {
    // 插入环境
    for (const key in ENV_DOMAIN_MAP) {
      this.columns.splice(4, 0, {
        field: key,
        header: ENV_DOMAIN_MAP[key] + '域名',
        width: 180
      })
    }

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
    edit (r) {
      this.$router.push(`/provider/list/edit`)
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
