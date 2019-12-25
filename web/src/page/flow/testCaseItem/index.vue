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
      params: {'fkTestCaseId': this.$route.params.id},
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
                this.$router.push(`/flow/testCaseItem/add/${row.id}/` + this.$route.params.id)
              }
            }
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
      this.$router.push(`/flow/testCaseItem/add/` + this.$route.params.id)
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
