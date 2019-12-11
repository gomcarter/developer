<template>
  <div class="json-container" v-html="formatted" @click="onClicked"
       :style="{ width: width ? `${width}px`: '100%', height: `${height}px`, 'min-height': `${minHeight}px`}" >
  </div>
</template>

/**
* @author ： 李银 on 2018年6月19日 21:11:04
*
* 入参：
* json:Object或者Array  - 对应的json数据
* width:Number         - 显示宽度，默认100%
* height:Number         - 显示高度，默认自适应
**/
<script>
import { toJsonHtml } from '@/config/utils'

export default {
  name: 'jsonformatter',
  props: {
    json: null,
    minHeight: {
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
    }
  },
  data () {
    return {
      formatted: ''
    }
  },
  watch: {
    json: 'init'
  },
  mounted () {
    this.init()
  },
  methods: {
    onClicked (e) {
      if (e.target.className.indexOf('expander') > -1) {
        const parent = e.target.parentNode
        if (parent.className.indexOf('collapsed') > -1) {
          parent.className = parent.className.replace('collapsed', '').trim()
        } else {
          parent.className += ' collapsed'
        }
      }
    },
    init () {
      this.formatted = toJsonHtml(this.json)
    }
  },
  components: {
  }
}
</script>

<style lang="scss">
  @import 'index.scss';
</style>
