<template>
  <div>
    <v-dialog ref="editNodeDialog" title="组合表" :ok="confirm" :width="1000">
      <div slot="body">
        <el-form ref="form" :model="node" label-width="8em" class="edit-node-dialog">
          <el-form-item prop="aggregation" label="聚合：" >
            <el-switch v-model="node.aggregation">
            </el-switch>
          </el-form-item>
          <el-form-item v-if="node.aggregation" label="聚合维度:" required
                        :rules="[{ required: true, message: '请至少选择一个聚合维度', trigger: ['blur', 'change'] }]">
            <v-selector v-if="aggregationDataList.length > 0"
                        :id="'column'" :text="'text'" :searchKey="'name'" style="width: 666px;"
                        :onSelectionChanged="onSelectAggregation" :multiple="true"
                        :filterable="true" placeholder="请选择聚合维度"
                        :load="node.tableId ? [node.tableId] : null"
                        :data="aggregationDataList"
            ></v-selector>
          </el-form-item>
          <el-form-item v-if="node.aggregation" prop="tableId" label="数据指标:"  required
                        :rules="[{ required: true, message: '请选择一个表', trigger: ['blur', 'change'] }]">
            <el-form>
              <el-form-item v-for="(fc, fi) in fixedColumns" :key="'fixedColumns_' + fi">
                <div>{{fc.text}}</div>
              </el-form-item>
              <el-form v-if="customColumns">
                <el-form-item v-for="(cc, ci) of customColumns" :key="'customColumns_' + ci">
                  <v-selector :id="'column'" :text="'text'" :searchKey="'name'" style="width: 200px"
                              :onSelectionChanged="onSelectCustomColumn"
                              :filterable="true" placeholder="请选择需要的指标"
                              :load="cc.column ? [cc.column] : null"
                              :data="aggregationDataList"
                  ></v-selector>
                  <v-selector style="width: 100px"
                              placeholder="选择转换算法"
                              :load="cc.column ? [cc.column] : ['不转换']"
                              :data="[{id: '不转换', text: '不转换'},{id: '转换', text: '转换'}, {id: '聚合', text: '聚合'}]"
                  ></v-selector>
                  <v-selector :onSelectionChanged="onSelectCustomColumn" style="width: 100px"
                              placeholder="选择转换算法"
                              :load="cc.mapper ? [cc.mapper] : ['算法1']"
                              :data="[{id: '算法1', text: '算法1'},{id: '算法2', text: '算法2'}, {id: '算法3', text: '算法3'}]"
                  ></v-selector>
                  <el-button type="danger" icon="el-icon-delete" @click="deleteCustomColumn(ci)" circle size="small"></el-button>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" icon="el-icon-plus" @click="addCustomColumn()" circle size="small"></el-button>
                </el-form-item>
              </el-form>
            </el-form>
          </el-form-item>
          <el-form-item v-if="node.aggregation" label="连接:" required
                        :rules="[{ required: true, message: '请至少选择一个聚合维度', trigger: ['blur', 'change'] }]">
            <el-form v-if="joinTypeList">
              <el-form-item v-for="(jl, ji) of joinTypeList" :key="'joinTypeList_' + ji">
                <v-selector :id="'column'" :text="'text'" :searchKey="'name'" style="width: 200px"
                            :onSelectionChanged="onSelectCustomColumn"
                            :filterable="true" placeholder="请选择需要的指标"
                            :load="jl.leftColumn ? [jl.leftColumn] : null"
                            :data="aggregationDataList"
                ></v-selector>
                <v-selector :onSelectionChanged="onSelectCustomColumn" style="width: 200px"
                            placeholder="选择连接方式"
                            :load="jl.joinType ? [jl.joinType] : null"
                            :data="[{id: '内联', text: '内联'},{id: '左联', text: '左联'}]"
                ></v-selector>
                <v-selector :id="'column'" :text="'text'" :searchKey="'name'" style="width: 200px"
                            :onSelectionChanged="onSelectCustomColumn"
                            :filterable="true" placeholder="请选择需要的指标"
                            :load="jl.rightColumn ? [jl.rightColumn] : null"
                            :data="aggregationDataList"
                ></v-selector>
                <el-button type="danger" icon="el-icon-delete" @click="deleteJoinType(jl)" circle size="small"></el-button>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="el-icon-plus" @click="addJoinType()" circle size="small"></el-button>
              </el-form-item>
            </el-form>
          </el-form-item>
          <el-form-item v-if="node.aggregation" label="条件:" required
                        :rules="[{ required: true, message: '请至少选择一个聚合维度', trigger: ['blur', 'change'] }]">
            <el-form v-if="conditionList">
              <el-form-item v-for="(cl, cli) of conditionList" :key="'conditionList_' + cli">
                <v-selector :id="'column'" :text="'text'" :searchKey="'name'" style="width: 200px"
                            :onSelectionChanged="onSelectCustomColumn"
                            :filterable="true" placeholder="请选择需要的指标"
                            :load="cl.leftColumn ? [cl.leftColumn] : null"
                            :data="aggregationDataList"
                ></v-selector>
                <v-selector :onSelectionChanged="onSelectCustomColumn" style="width: 200px"
                            placeholder="选择连接方式"
                            :load="cl.joinType ? [cl.joinType] : null"
                            :data="[{id: '>', text: '>'},{id: '=', text: '='},{id: '<', text: '<'},{id: 'in', text: 'in'}]"
                ></v-selector>
                <v-selector :id="'column'" :text="'text'" :searchKey="'name'" style="width: 200px"
                            :onSelectionChanged="onSelectCustomColumn"
                            :filterable="true" placeholder="请选择需要的指标"
                            :load="cl.rightColumn ? [cl.rightColumn] : null"
                            :data="aggregationDataList"
                ></v-selector>
                <el-button type="danger" icon="el-icon-delete" @click="deleteCondition(cl)" circle size="small"></el-button>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="el-icon-plus" @click="addCondition()" circle size="small"></el-button>
              </el-form-item>
            </el-form>
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
      tableInfo: null,
      aggregationDataList: [],
      fixedColumns: [],
      customColumns: [],
      joinTypeList: [],
      conditionList: [],
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
            text: '【删除】',
            handler: (row) => {}
          }]
        }
      ],
      node: {
        aggregation: false,
        tableId: null
      }
    }
  },
  methods: {
    onSelectCustomColumn () {

    },
    onLibraryClicked () {

    },
    addCustomColumn () {
      this.customColumns.push({})
    },
    deleteCustomColumn (index) {
      this.customColumns.splice(index, 1)
    },
    addJoinType () {
      this.joinTypeList.push({})
    },
    deleteJoinType (index) {
      this.joinTypeList.splice(index, 1)
    },
    addCondition () {
      this.conditionList.push({})
    },
    deleteCondition (index) {
      this.conditionList.splice(index, 1)
    },
    onSelectAggregation (selections) {
      console.log(selections)
      this.fixedColumns = selections.map(s => s)
    },
    async onSelectTable (selections) {
      const selection = selections[0] || {}
      this.node.tableId = selection.id
      this.node.name = selection.name

      tableInfoApi(this.node.tableId)
        .then(res => {
          this.tableInfo = res
        })
    },
    setInterfaces (selection) {
    },
    open (node) {
      this.n = node
      const model = node.getModel()
      console.log(node.getEdges())
      console.log(model)
      // 获取接入节点
      this.aggregationDataList = node.getEdges()
        .map(s => s.getSource())
        .filter(s => s !== node)
        .map(s => {
          const data = s.getModel().data
          if (data) {
            data.columns = data.columns.map(s => {
              s.text = data.name + '.' + s.name
              return s
            })
          }
          return data
        })
        .filter(s => s)
        .flatMap(s => s.columns)

      console.log(this.aggregationDataList)

      if (this.aggregationDataList.length === 0) {
        this.$alert('请先设置接入表！', '提示', {type: 'error'})
        return
      }
      console.log(this.aggregationDataList)

      this.$refs.editNodeDialog.open()
    },
    confirm () {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.onOk && this.onOk(Object.assign({}, this.node))
          this.$refs.editNodeDialog.close()
        }
      })
    }
  },
  mounted () {
    window.tt = this
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
