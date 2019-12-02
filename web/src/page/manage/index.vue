<template>
  <div>
    <h4 class="title">筛选条件</h4>
    <hr/>
    <div class="filters">
      <el-form :inline="true" :model="filter" label-width="6em">
        <el-form-item label="接口名称">
          <el-input v-model="filter.name" placeholder="请输入接口名称" />
        </el-form-item>
        <el-form-item label="URL">
          <el-input v-model="filter.url" placeholder="请输入URL" />
        </el-form-item>
        <el-form-item label="前端项目">
          <v-selector
            :id="'id'" :text="'name'"
            :onSelectionChanged="(d) => filter.endId = (d[0] || {}).id"
            :filterable="true" :remote="true"
            placeholder="请选择前端项目（可输入名称进行搜索）"
            :url="endListApi"
          ></v-selector>
        </el-form-item>
        <el-form-item label="java项目">
          <v-selector
            :id="'id'" :text="'name'"
            :onSelectionChanged="(d) => filter.javaId = (d[0] || {}).id"
            :filterable="true" :remote="true"
            placeholder="请选择java项目（可输入名称进行搜索）"
            :url="javaListApi"
          ></v-selector>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search" icon="el-icon-search">搜索</el-button>
          <el-button type="info" @click="clear" icon="el-icon-delete">清空</el-button>
        </el-form-item>
      </el-form>
    </div>
    <h4 class="title">接口列表</h4>
    <hr/>
    <v-datagrid :columns="columns" :data-url="dataUrl" :count-url="countUrl" :params="params" :toolbar="toolbar"/>

    <v-dialog ref="newAddDialog"
              title="更新接口"
              :ok="addInterfaces">
      <el-form slot="body" :model="form" label-width="7em" ref="addForm">
        <el-form-item prop="javaId" label="选择java项目" required
                      :rules="[{ required: true, message: '请选择一个java项目', trigger: ['blur', 'change'] }]">
          <v-selector  :onSelectionChanged="(d) => form.javaId = (d[0] || {}).id"
                       :id="'id'" :text="'name'"
                       :filterable="true" :remote="true"
                       :load="[form.javaId]" placeholder="请选择一个java项目（可输入商品名称进行搜索）"
                       :url="javaListApi"/>
          <el-input v-model="form.javaId" style="display: none" />
        </el-form-item>
      </el-form>
    </v-dialog>
  </div>
</template>

<script>
import { interfacesCountApi, interfacesListApi, addInterfaces, endListApi, javaListApi, deleteInterfaces } from '@/config/api/inserv-api'
import { formatDate, removeBlank } from '@/config/utils'

export default {
  name: 'interfaces',
  data () {
    return {
      form: {
        javaId: null
      },
      endListApi,
      javaListApi,
      filter: {
        id: null,
        name: null,
        url: null,
        javaId: null,
        endId: null
      },
      dataUrl: interfacesListApi,
      countUrl: interfacesCountApi,
      params: {},
      toolbar: [{
        title: '更新接口',
        icon: 'el-icon-plus',
        handler: this.openNewAddDialog
      }],
      columns: [
        {field: 'id', header: '序号', sort: 'id', width: 90},
        {field: 'name', header: '接口名称', sort: 'name', width: 200},
        {field: 'method', header: 'METHOD', sort: 'method', width: 80},
        {field: 'deprecated', header: '废弃', sort: 'deprecated', html: true, width: 80, formatter: (row, index, value) => value ? '是'.fontcolor('red') : '否'},
        {field: 'url', header: 'URL地址', sort: 'url', width: 120},
        {field: 'end', header: '前端项目', sort: 'fk_end_id', width: 160},
        {field: 'java', header: 'java模块', sort: 'fk_java_id', width: 120},
        {field: 'modifyTime', header: '更新时间', sort: 'modify_time', width: 200, formatter: (row, index, value) => formatDate(value)},
        {field: 'createTime', header: '添加时间', sort: 'create_time', width: 200, formatter: (row, index, value) => formatDate(value)},
        {
          field: 'action',
          header: '操作',
          width: 130,
          html: true,
          actions: [{
            text: (row) => `<a href="#/manage/list/view/${row.id}" target="_blank">查看</a>`,
            handler: (row) => {}
          }, {
            text: '删除',
            handler: (row) => {
              this.delete(row.id)
            }
          }]
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
    delete (id) {
      this.$confirm('删除将无法恢复，确认删除吗？', '提示', {type: 'warning'}).then(() => {
        deleteInterfaces(id)
          .then(() => {
            this.$success('删除成功！')
            this.search()
          })
      })
    },
    openNewAddDialog () {
      this.$refs.newAddDialog.open()
    },
    addInterfaces () {
      this.$refs.addForm.validate((valid) => {
        if (valid) {
          addInterfaces(this.form).then(d => {
            this.$success(`操作成功，更新接口条数：${d}条`)
            this.search()
            this.$refs.newAddDialog.close()
          })
        }
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
