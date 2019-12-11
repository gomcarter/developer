<template>
    <table>
      <thead>
        <tr>
          <th :style="{width: column.width +'px'}" v-for="(column, index) in columns" :key="index">{{column.name}}</th>
        </tr>
      </thead>
      <tbody v-if="data_list.length">
        <tr v-for="(d, i) in data_list" :key="i">
          <td v-for="(c, j) in columns" :key="j">
            <el-checkbox v-if="c.field === 'required'" v-model="d[c.field]" @change="getData"></el-checkbox>
            <el-select v-else-if="c.field === 'type'" placeholder="请选择类型" v-model="d[c.field]" @blur="getData">
              <el-option
                v-for="(v, i) in type"
                :key="i"
                :label="v"
                :value="v">
              </el-option>
            </el-select>
            <el-select v-else-if="c.field === 'fkRulesid'" placeholder="请选择规则" v-model="d[c.field]" @blur="getData" @change="getData">
              <el-option
                v-for="(v, i) in rules"
                :key="i"
                :label="v.name"
                :value="v.id">
              </el-option>
            </el-select>
            <el-button v-else-if="c.field === 'action'" size="small" :type="c.fn.type" @click="del(d, i)">{{c.fn.title}}</el-button>
            <el-input v-else v-model="d[c.field]" :placeholder="'请输入' + c.name" @blur="getData"></el-input>
          </td>
        </tr>
      </tbody>
      <tfoot>
        <tr>
          <td :colspan="columns.length" @click="add()" class="add"><i class="el-icon-plus"></i>添加</td>
        </tr>
      </tfoot>
    </table>
</template>

<script>
export default {
  props: {
    columns: {
      type: Array,
      default: () => [
        {name: '必选', field: 'required', width: 100},
        {name: '参数名', field: 'key', width: 200},
        {name: '操作', field: 'action', width: 240, fn: {title: '删除', type: 'danger', delete: () => {}}
        }
      ]
    },
    dataList: {
      type: Array,
      default: () => []
    },
    name: {
      type: String,
      default: ''
    },
    rulesUrl: {
      type: Function,
      default: () => []
    }
  },
  data () {
    return {
      data_list: [],
      data_name: '',
      type: ['string', 'number', 'object', 'date', 'boolean', 'list', 'file', 'json', 'null'],
      rules: []
    }
  },
  computed: {},
  methods: {
    del (row, index) {
      this.data_list.splice(index, 1)
      this.getData()
    },
    add () {
      let key = {}
      for (let i = 0; i < this.columns.length; i++) {
        if (this.columns[i].field !== 'action') {
          key[this.columns[i].field] = this.columns[i].field === 'type' ? 'string' : ''
        }
      }
      let l = this.data_list.length
      if (l === 0 || (l > 0 && !!this.data_list[l - 1]['key'])) {
        this.data_list.push(key)
      } else {
        this.$notify.warning({
          title: '提示',
          message: '上一条数据未输入参数名',
          position: 'top-right'
        })
      }
      this.getData()
    },
    getData () {
      this.$emit('getData', this.data_list, this.name)
    }
  },
  watch: {
    dataList: {
      handler (newValue, oldValue) {
        this.data_list = newValue
      }
    }
  },
  components: {
  },
  beforeCreate () {
  },
  cteated () {
  },
  beforeMount () {
  },
  mounted () {
    this.data_list = this.dataList
    this.data_name = this.name
    if (this.rulesUrl.name !== '_default') {
      this.rulesUrl().then((r) => {
        r.unshift({name: '无', id: 0})
        this.rules = r
      })
    }
  },
  beforeUpdate () {
  },
  updated () {
  },
  beforeDestroy () {
  },
  destroyed () {
  }
}
</script>

<style type="text/css" lang="scss">
  @import 'index.scss';
</style>
