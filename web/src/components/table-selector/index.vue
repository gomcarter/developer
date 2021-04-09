<template>
  <div>
    <v-dialog ref="editNodeDialog" title="选择表" :ok="confirm" :width="1000">
      <div slot="body">
        <el-form ref="form" :model="tableInfo" label-width="8em" class="edit-node-dialog">
          <el-form-item>
            <el-radio v-model="library" label="publics">公共库</el-radio>
            <el-radio v-model="library" label="privates">私有库</el-radio>
          </el-form-item>
          <el-form-item prop="id" label="请选择一个表:"  required
                        :rules="[{ required: true, message: '请选择一个表', trigger: ['blur', 'change'] }]">
            <v-selector v-if="library === 'publics'"
                        :id="'id'" :text="'name'" :searchKey="'name'" style="width: 666px;"
                        :onSelectionChanged="onSelectTable"
                        :filterable="true" :remote="true" placeholder="请从公共库选择一个表（可输入名称进行搜索）"
                        :load="tableInfo.id ? [tableInfo.id] : null"
                        :url="tableListApi"
            ></v-selector>
            <v-selector v-if="library === 'privates'"
                        :id="'id'" :text="'name'" :searchKey="'name'" style="width: 666px;"
                        :onSelectionChanged="onSelectTable"
                        :filterable="true" :remote="true" placeholder="请从私有库选择一个表（可输入名称进行搜索）"
                        :load="tableInfo.id ? [tableInfo.id] : null"
                        :url="tableListApi"
            ></v-selector>
            <el-input v-model="tableInfo.id" class="hidden"></el-input>
          </el-form-item>
          <el-form-item label="表说明:">
            <i>{{ tableInfo && tableInfo.description }}</i>
          </el-form-item>
          <el-form-item label="字段详情:">
            <v-datagrid :load-data="tableInfo.columns" :columns="columns" :pageable="false"></v-datagrid>
          </el-form-item>
        </el-form>
      </div>
    </v-dialog>
  </div>
</template>

<script>
import { tableListApi, tableInfoApi } from '@/config/api/data-api'

export default {
  props: {
    onOk: {
      type: Function,
      default: () => {}
    }
  },
  data () {
    return {
      library: 'publics',
      tableListApi,
      tableInfoApi,
      tableInfo: {},
      columns: [
        {field: 'name', header: '字段名', width: 100},
        {field: 'type', header: '类型', width: 100},
        {field: 'comment', header: '说明', width: 300},
        {
          field: 'action',
          header: '操作',
          width: 80,
          html: true,
          actions: [{
            text: '【取消字段】',
            handler: (row) => {}
          }]
        }
      ]
    }
  },
  methods: {
    onLibraryClicked () {

    },
    async onSelectTable (selections) {
      const selection = selections[0] || {}
      if (selection.id) {
        tableInfoApi(selection.id)
          .then(res => {
            this.tableInfo = res
            this.tableInfo.id = selection.id
          })
      } else {
        this.tableInfo = {}
        this.tableInfo.id = null
      }
    },
    setInterfaces (selection) {
    },
    open (node) {
      const model = node.getModel()
      console.log(node, model)
      this.tableInfo = Object.assign({}, model.data)
      this.tableInfo.id = model.data ? model.data.id : null
      this.$refs.editNodeDialog.open()
    },
    confirm () {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.onOk && this.onOk(Object.assign({}, this.tableInfo))
          this.$refs.editNodeDialog.close()
        }
      })
    }
  },
  mounted () {
  },
  components: {
    'v-dialog': () => import('@/components/dialog'),
    'v-selector': () => import('@/components/selector'),
    'v-datagrid': () => import('@/components/datagrid')
  }
}
</script>

<style type="text/css" lang="scss" scoped>
  @import 'index';
</style>
