<template>
  <span class="json-kvov json-objProp" v-if="formatted"
        :class="(children ? 'json-children' : 'json-container') + (selectedKeys.indexOf(formatted.lineage) >= 0 ? ' json-selected' : '')"
        :style="{
         width: children ? 'auto' : (width ? `${width}px`: '100%'),
         height: children ? `${height}px` : 'auto',
         'min-height': children ? 'auto' : `${minHeight}px`,
         'min-width': children ? 'auto' : `${minWidth}px`
        }"
    @click="onRowClicked"
  >
    <span v-if="formatted.key" class="json-key">"{{ formatted.key }}"<b>:</b></span>
    <span v-if="formatted.type === 'array' || formatted.type === 'object'">
      <span class="json-b">{{ formatted.type === 'array' ? '[' : '{' }}</span>
      <span class="json-expander" @click="onExpanderClicked"></span>
      <span class="json-ell"></span>
      <span class="json-blockInner">
        <jsons v-for="(f, i) in formatted.children" :formatted-data="f" :key="i" :children="true"
               :selectable="selectable" :selected-keys="selectedKeys" :multiple="multiple"
               :onSelectionChanged="onSelectionChanged">
        </jsons>
      </span>
      <span class="json-b">{{ formatted.type === 'array' ? ']' : '}' }}</span>
    </span>
    <span v-else :class="`json-${formatted.type} json-value`">{{ formatted.value }}</span>
    <span v-if="formatted.comma">,</span>
    <i v-if="formatted.lineage" class="json-comment"> // {{ formatted.lineage }} - {{ formatted.type }}</i>
<!--    <span v-if="formatted.comment" class="json-comment"> // {{ formatted.comment }}</span>-->
  </span>
</template>

/**
* @author ： 李银 on 2018年6月19日 21:11:04
*
* 入参：
* json:Object或者Array                - 对应的json数据
* width:Number                        - 显示宽度，默认100%
* height:Number                       - 显示高度，默认自适应
* onSelectionChanged:function         - 选中改变
**/
<script>
import { type } from '@/config/utils'
import draggable from 'vuedraggable'

export default {
  name: 'jsons',
  props: {
    json: null,
    children: false,
    minHeight: {
      type: Number,
      default: 600
    },
    minWidth: {
      type: Number,
      default: 600
    },
    width: {
      type: Number,
      default: null
    },
    height: {
      type: Number,
      default: null
    },
    formattedData: {
      type: Object,
      default: null
    },
    selectedKeys: {
      type: Array,
      default: () => []
    },
    multiple: {
      type: Boolean,
      default: false
    },
    selectable: {
      type: Boolean,
      default: false
    },
    onSelectionChanged: {
      type: Function,
      default: () => {}
    }
  },
  data () {
    return {
      formatted: null
    }
  },
  watch: {
    json: 'init'
  },
  mounted () {
    window.ccc = this
    if (this.formattedData) {
      this.formatted = this.formattedData
    } else {
      this.init()
    }
  },
  methods: {
    onRowClicked (e) {
      if (!this.selectable) {
        return
      }
      const lineage = this.formatted.lineage
      if (this.multiple) {
        const index = this.selectedKeys.indexOf(lineage)
        if (index >= 0) {
          this.selectedKeys.splice(index, 1)
        } else {
          this.selectedKeys.push(lineage)
        }
      } else {
        this.selectedKeys.length = 0
        this.selectedKeys.push(lineage)
      }
      this.onSelectionChanged && this.onSelectionChanged(this.selectedKeys)

      e.stopPropagation()
    },
    onExpanderClicked (e) {
      if (e.target.className.indexOf('json-expander') > -1) {
        const parent = e.target.parentNode
        if (parent.className.indexOf('json-collapsed') > -1) {
          parent.className = parent.className.replace('json-collapsed', '').trim()
        } else {
          parent.className += ' json-collapsed'
        }
      }
    },
    dataChanged () {
      this.formatted = Object.assign({}, this.formattedData)
    },
    init () {
      this.formatted = this.format(null, this.json)
    },
    getSelections () {
      return this.selectedKeys
    },
    format (key, data, comma, lineage) {
      lineage = lineage ? (lineage + (key ? ('.' + key) : '')) : key

      const t = type(data)
      switch (t) {
        // []类型
        case 'array':
          const arrays = []
          for (let i = 0; i < data.length; i++) {
            arrays.push(this.format(null, data[i], data.length !== i + 1, lineage))
          }
          return {
            lineage,
            key,
            type: t,
            comma,
            children: arrays
          }
        // {}类型
        case 'object':
          const objects = []
          const d = Object.entries(data)
          for (let i = 0; i < d.length; i++) {
            objects.push(this.format(d[i][0], d[i][1], d.length !== i + 1, lineage))
          }
          return {
            lineage,
            key,
            type: t,
            comma,
            children: objects
          }
        case 'undefined':
          return {
            lineage,
            key,
            type: t,
            comma,
            value: 'undefined'
          }
        case 'string':
        case 'boolean':
        case 'number':
        case 'null':
        default:
          return {
            lineage,
            key,
            type: t,
            comma,
            value: JSON.stringify(data)
          }
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
