<template>
  <div onselectstart="return false">
    <draggable v-model="nodes" :disabled="!editable" @end="onDragEnd" :group="dragKey">
      <transition-group>
        <div v-if="nodes && nodes.length > 0" v-for="(node, index) of nodes" :key="index + '_nodes'">
          <div class="tree-node list-group-item"
               :class="{ active: isSelected(node) }" :tabindex="-1" @click="onClick(node)">
            <span :style="lsIndent"></span>
            <span @click='toggle($event, node)' class="icon expand-icon"
                  :class="[ node.open ? 'el-icon-minus' : 'el-icon-plus' ]"
                  :style="{ 'visibility' : isFolder(node) ? 'visible' : 'hidden' }"></span>
            <span>{{node[text]}}</span>
            <span class="icon editable el-icon-edit-outline" @click="beginEdit($event, node)" v-if="editable"></span>
            <span class="icon editable el-icon-plus" style="right: 33px;" @click="beginAdd($event, node)" v-if="editable"></span>
          </div>

          <items :datas='node.nodes' :selected="selected" :multiple="multiple" :onSelected="onSelected" :onUnselected="onUnselected"
                 :onSelectionChanged="onSelectionChanged" :level="level + 1" v-show="node.open" :id="id" :text="text"
                 :editable="editable" :onCreate="onCreate" :onUpdate="onUpdate"
                 v-if='isFolder(node)'></items>
        </div>
      </transition-group>
    </draggable>
    <div v-if="level === 1 && editable" class="tree-node list-group-item" style="text-align: center;" @click="beginAdd($event)">
      <span class="el-icon-plus">新增节点</span>
    </div>
  </div>
</template>
/**
* @author ： 李银 on 2018年5月24日 23:55:12
*
* 入参：
* editable:Boolean    - 是否可以被编辑。默认为false（不可标记）
* multiple:Boolean    - 是否支持数据多行被选中， 默认为false（单选）
* nodes:Array         - 数据， 格式尽量为：{id : '主键', text : '显示的内容'}
* selected:Array      - 初始选中数据， 格式为：[1,2,3,4, '主键id']
*
* 回调：
* onSortChanged(node, newIndex) - 被拖动的节点，新的排序值
* onUpdate(node)                - 某个节点被选中回调，不设置则不触发，node为选中节点数据, selected当前已经选中的节点id
* onSelected(node, selected)    - 某个节点被选中回调，不设置则不触发，node为选中节点数据, selected当前已经选中的节点id
* onUnselected(node, selected)  - 某个节点被取消选中回调，不设置则不触发，node为取消选中节点数据, selected当前已经选中的节点id
* onSelectionChanged(selected)  - 选中项改变了触发，selected当前已经选中的节点id
**/
<script>
import draggable from 'vuedraggable'

/* eslint-disable */
export default {
  name: 'items',
  props: {
    editable: {
      type: Boolean,
      default: false
    },
    dragKey: {
      type: String,
      default: 'tree'
    },
    id: {
      type: String,
      default: 'id'
    },
    text: {
      type: String,
      default: 'text'
    },
    multiple: {
      type: Boolean,
      default: false
    },
    datas: {
      type: Array,
      default: () => null
    },
    // 私有参数
    level: {
      type: Number,
      default: 1
    },
    onSelected: {
      type: Function,
      default: null
    },
    onUnselected: {
      type: Function,
      default: null
    },
    onSelectionChanged: {
      type: Function,
      default: null
    },
    onUpdate: {
      type: Function,
      default: null
    },
    onCreate: {
      type: Function,
      default: null
    },
    onSortChanged: {
      type: Function,
      default: null
    },
    selected: {
      type: Array,
      default: () => []
    }
  },
  mounted () {
    this.init()

    if (!this.selected) {
      this.selected = []
    }
    console.log('mounted')
  },
  watch : {
    'datas': 'init'
  },
  data () {
    return {
      nodes: [],
      open: false,
      indent: {
        'margin-left': '10px',
        'margin-right': '10px'
      }
    }
  },
  computed: {
    lsIndent () {
      this.indent['margin-left'] = `${18 * this.level}px`
      return this.indent
    }
  },
  methods: {
    onDragEnd (e) {
      if (e.newIndex !== e.oldIndex) {
        const node = this.nodes[e.newIndex]
        const index = e.newIndex
        this.onSortChanged && this.onSortChanged(node, index)
      }
    },
    init () {
      if (this.datas) {
        this.$set(this, 'nodes', this.datas.map(s => s))
      }
    },
    isFolder (node) {
      return node.nodes && node.nodes.length
    },
    toggle (event, node) {
      if (this.isFolder(node)) {
        this.$set(node, 'open', !node.open)
      }
      event.stopPropagation()
    },
    beginAdd (e, parent) {
      e.stopPropagation()

      this.$prompt('新增节点', '', {
        inputType: 'text',
        inputPlaceholder: '请输入节点名称',
        inputValidator: (d) => (d + '' || '').trim().length > 0 || '请输入节点名称'
      }).then(async (r) => {
        let node = {}
        if (this.onCreate) {
          node = await this.onCreate(r.value, parent)
        } else {
          node[this.id] = new Date().getTime()
          node[this.text] = r.value
        }

        if (parent) {
          if (parent.nodes) {
            parent.nodes.push(node)
          } else {
            this.$set(parent, 'nodes', [node])
          }
        } else {
          this.nodes.push(node)
        }
      })
    },
    beginEdit (e, node) {
      e.stopPropagation()

      this.$prompt('编辑节点', '', {
        inputType: 'text',
        inputValue: node[this.text],
        inputPlaceholder: '请输入节点名称',
        inputValidator: (d) => (d + '' || '').trim().length > 0 || '请输入节点名称'
      }).then((r) => {
        if (node[this.text] != r.value) {
          this.$set(node, this.text, r.value)

          this.onUpdate && this.onUpdate(node)
        }
      })
    },
    isSelected (node) {
      return this.selected.indexOf(node[this.id]) > -1
    },
    getSelected () {
      return this.selected.map(s => s)
    },
    onClick (node) {
      // 此行被选择时，则再点击取消选择
      if (this.isSelected(node)) {
        const index = this.selected.indexOf(node[this.id])
        if (index > -1) {
          this.$set(this.selected, index, null)

          // 执行取消选择回调
          if (this.onUnselected) {
            this.onUnselected(node, this.selected.filter(s => s != null))
          }
        }
      } else {
        // 未被选中，则选中此行；
        // 多选的时push一条进去，单选时覆盖之前的。
        if (this.multiple) {
          const index = this.selected.indexOf(node[this.id])
          // 存在空的数组
          if (index > -1) {
            this.$set(this.selected, index, node[this.id])
          } else {
            this.selected.push(node[this.id])
          }
        } else {
          this.$set(this.selected, 0, node[this.id])
        }

        // 执行选择回调
        if (this.onSelected) {
          this.onSelected(node, this.selected.filter(s => s != null))
        }
      }

      // 执行选中项修改了
      if (this.onSelectionChanged) {
        this.onSelectionChanged(this.selected.filter(s => s != null))
      }
    }
  },
  components: {
    draggable
  }
}
</script>

<style lang="scss" scoped>
  @import 'index.scss';
</style>
