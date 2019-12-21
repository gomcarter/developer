<template>
  <div>
    <h4 class="title">筛选条件</h4>
    <hr/>
    <div class="filters">
      <el-form :inline="true" :model="filter" label-width="6em">
        <el-form-item label="用例名称">
          <el-input v-model="filter.name" placeholder="请输入用例名称" />
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
import { getInterTestcaseCount, getInterTestcase } from '@/config/api/inserv-api'
import { formatDate, removeBlank } from '@/config/utils'

export default {
  data () {
    return {
      filter: {
        name: ''
      },
      dataUrl: getInterTestcase,
      countUrl: getInterTestcaseCount,
      params: {},
      toolbar: [{
        title: '新增',
        icon: 'el-icon-plus',
        handler: this.add
      }],
      columns: [
        {field: 'name', header: '用例名称', sort: 'name', width: 200},
        {field: 'mark', header: '备注', sort: 'mark', width: 400},
        // {field: 'userName', header: '执行人', sort: 'userName', width: 200},
        {field: 'createTime', header: '添加时间', sort: 'create_time', width: 200, formatter: (row, index, value) => formatDate(value)},
        {field: 'modifyTime', header: '上次修改时间', sort: 'modify_time', width: 200, formatter: (row, index, value) => formatDate(value)},
        {
          field: 'action',
          header: '操作',
          width: 160,
          actions: [
            {
              text: '编辑',
              handler: (row) => {
                this.$router.push(`/flow/example/add/${row.id}`)
              }
            }
            // {
            //   text: (row) => {
            //     return row.type === '0' ? '<p style="color: red">已禁用</p>' : '<p>已启用</p>'
            //   },
            //   html: true,
            //   handler: (row) => {
            //     this.$confirm('确定修改？', '提示', {type: 'info'}).then(() => {
            //       postModuleChangeType({objId: row.id, type: row.type}).then((res) => {
            //         this.search()
            //       }).catch((err) => {
            //         console.log(err)
            //       })
            //     })
            //   }
            // }
          ]
        }
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
    add (r) {
      this.$router.push(`/flow/example/add`)
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
