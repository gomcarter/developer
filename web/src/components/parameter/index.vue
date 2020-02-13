<template>
  <div>
    <el-table
      :data="data || []"
      style="width: 100%;margin-bottom: 20px;"
      row-key="key"
      border
      default-expand-all>
      <el-table-column
        prop="key"
        label="参数名"
        width="150">
      </el-table-column>
      <el-table-column
        prop="type"
        label="类型"
        width="120">
      </el-table-column>
      <el-table-column
        prop="notNull"
        label="是否必填"
        :formatter="(row, column, cellValue) => cellValue ? '是' : '否'"
        width="120">
      </el-table-column>
      <el-table-column
        prop="defaults"
        label="默认值"
        :formatter="(row, column, cellValue) => cellValue === undefined ? '' : cellValue + ''"
        width="100">
      </el-table-column>
      <el-table-column
        prop="comment"
        label="说明"
        width="800">
        <template slot-scope="scope">
          <div v-html="scope.row.comment"></div>
          <v-jsonformatter v-if="scope.row.body" :json="scope.row.body" :minHeight="100"></v-jsonformatter>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

/**
* @author ： 李银 on 2019-06-20 17:05:03
*
* 数据结构：
* {
*     "key": "images",
*     "notNull": true,
*     "comment": "sku图片",
*     "type": "String",
*     "children": [{
*         "key": "images",
*         "notNull": true,
*         "comment": "sku图片",
*         "type": "String",
*         "children": [{...}]
*     }]
* }
*
* 入参：
* json:Array     - 数据结构见上面
*
**/
<script>

export default {
  name: 'parameter',
  props: {
    json: {
      type: Array,
      default: null
    }
  },
  data () {
    return {
      data: []
    }
  },
  mounted () {
    this.init()
  },
  methods: {
    init () {
      // 把json数据结构全部打平
      this.data = this.json.flatMap(s => {
        // FIXME: 第一层的Object参数名去掉
        if (s.type === 'Object' && !s.body) {
          s.key = undefined
        }
        return this.generateChild(s, undefined)
      })
    },
    generateBodyComment (node) {
      if (node.type === 'List') {
        return [this.generateBodyComment(node.children[0])]
      } else if (node.type === 'Object') {
        const o = {}
        node.children = node.children || []
        node.children.forEach(s => {
          o[s.key] = this.generateBodyComment(s)
        })
        return o
      } else if (node.type === undefined) {
        return '...'
      } else if (node.type === 'void') {
        return '无'
      } else {
        return `${node.comment ? node.comment + '； ' : ''} 数据类型：${node.type}； ${node.notNull ? '此项一定不为空；' : ''}`
      }
    },
    generateChild (node, key) {
      const k = (key ? (key + '.') : '') + (node.key || '')
      if (node.body) {
        const comment = node.comment || ''
        return [{
          key: k,
          notNull: node.notNull,
          comment: comment,
          body: this.generateBodyComment(node),
          defaults: node.defaults,
          type: 'body'
        }]
      } else {
        if (node.type === 'List') {
          let that = [{
            key: k,
            notNull: node.notNull,
            comment: node.comment,
            defaults: node.defaults,
            type: 'List<' + (node.children[0].type || 'Object') + '>'
          }]
          if (node.children[0].type === 'Object' || node.children[0].type === 'List') {
            that = that.concat(
              (node.children[0].children || []).flatMap(s => this.generateChild(s, k))
            )
          }
          return that
        } else if (node.type === 'Object') {
          return (node.children || []).flatMap(s => this.generateChild(s, k))
        } else {
          return {
            key: k,
            notNull: node.notNull,
            comment: node.comment,
            defaults: node.defaults,
            type: node.type
          }
        }
      }
    }
  },
  components: {
    'v-jsonformatter': () => import('@/components/jsonformatter')
  }
}
</script>

<style lang='scss' scoped>
  @import 'index';
</style>
