<template>
  <div>
    <h4 class="title">用例接口列表</h4>
    <hr/>
    <v-datagrid :columns="columns" :data-url="dataUrl" :count-url="countUrl" :params="params" :toolbar="toolbar"/>
  </div>
</template>

<script>
import { getInterTestCaseItemCount, getInterTestCaseItem } from '@/config/api/inserv-api'
import { formatDate, removeBlank } from '@/config/utils'

export default {
  data () {
    return {
      filter: {
        name: ''
      },
      dataUrl: getInterTestCaseItem,
      countUrl: getInterTestCaseItemCount,
      params: {},
      toolbar: [{
        title: '新增',
        icon: 'el-icon-plus',
        handler: this.add
      }],
      columns: [
        {field: 'name', header: '接口名称', sort: 'name', width: 200},
        {field: 'fkInterfacesId', header: '接口ID', sort: 'fkInterfacesId', width: 400},
        {field: 'parmConfig', header: '入参配置', sort: 'parmConfig', width: 400},
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
            },
            {
              text: '详情',
              handler: (row) => {
                this.$router.push(`/flow/example/detail/${row.id}`)
              }
            },
            {
              text: '测试',
              handler: (row) => {
                alert('开发中')
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
      this.$router.push(`/flow/testCaseItem/add`)
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
