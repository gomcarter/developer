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
    <h4 class="title">包含接口列表</h4>
    <hr/>
    <v-datagrid v-if="data && data.id " :columns="columns" :data-url="dataUrl" :pageable="false" :params="params"/>
  </div>
</template>

<script>
import { getPackageApi, interfacesListApi } from '@/config/api/inserv-api'
import { formatDate } from '@/config/utils'

export default {
  name: 'packageView',
  data () {
    return {
      data: {
        id: null,
        name: null,
        userName: null,
        mark: null
      },
      dataUrl: interfacesListApi,
      pageable: false,
      params: {},
      columns: [
        {field: 'id', header: '编号', sort: 'id', width: 80},
        {field: 'name', header: '接口名称', sort: 'name', width: 200},
        {field: 'method', header: 'METHOD', sort: 'method', width: 80},
        {field: 'deprecated', header: '废弃', sort: 'deprecated', html: true, width: 80, formatter: (row, index, value) => value ? '是'.fontcolor('red') : '否'},
        {field: 'controller', header: '控制器', sort: 'controller', width: 200},
        {field: 'url', header: 'URL', sort: 'url', width: 120},
        {field: 'java', header: '后端服务', sort: 'fk_java_id', width: 120},
        {field: 'end', header: '前端项目', sort: 'fk_end_id', width: 160},
        {field: 'modifyTime', header: '更新时间', sort: 'modify_time', width: 200, formatter: (row, index, value) => formatDate(value)},
        {
          field: 'action',
          header: '操作',
          width: 130,
          html: true,
          actions: [{
            text: (row) => `<a href="#/interfaces/view/${row.id}" target="_blank">查看</a>`
          }]
        }
      ]
    }
  },
  mounted () {
    if (this.$route.params.id) {
      getPackageApi(this.$route.params.id).then((res) => {
        this.data = res
        this.params = {rows: 1000, idList: this.data.interfacesIdList}
      })
    }
  },
  methods: {
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
