<template>
  <div class="pager">
    <span>每页
      <select class="pager-rows" v-model="pageSize" @change="pageNo = 1; changed()">
        <option class="pager-rows-option" v-for="(v, n) of rowsSelections" :key="n">{{v}}</option>
      </select>
      条</span>
    <button class="pager-first" :disabled="pageNo <= 1"
            :class="{'pager-btn-disable':pageNo<=1, 'pager-btn-enable' : pageNo > 1}" @click="pageNo=1; changed()">首页
    </button>
    <button class="pager-previous" :disabled="pageNo <= 1"
            :class="{'pager-btn-disable':pageNo<=1, 'pager-btn-enable' : pageNo > 1}" @click="pageNo--; changed()">上一页
    </button>
    <input type="number" class="pager-current" v-model="pageNo" :max="totalPages" :min="1"
           @blur="changed" :disabled="totalPages <= 1"/>
    <button class="pager-next" :disabled="pageNo >= totalPages"
            :class="{'pager-btn-disable':pageNo >= totalPages, 'pager-btn-enable' : pageNo < totalPages}"
            @click="pageNo++; changed()">下一页
    </button>
    <button class="pager-last" :disabled="pageNo >= totalPages"
            :class="{'pager-btn-disable':pageNo >= totalPages, 'pager-btn-enable' : pageNo < totalPages}"
            @click="pageNo = totalPages; changed()">尾页
    </button>
    <span class="pager-page">
      共<span class="pager-page-num">{{  totalPages }}</span>页
    </span>
    <span class="pager-total">
      共<span class="pager-total-num">{{ totalCount }}</span>条记录
    </span>
  </div>
</template>

<script>
export default {
  name: 'pager',
  props: {
    // 分页数据总条数
    total: {
      type: Number,
      default: 0
    },
    // 当前页码
    page: {
      type: Number,
      default: 1
    },
    // 每页数据条数
    rows: {
      type: Number,
      default: 20
    }
  },
  data () {
    return {
      // 分页数据总条数
      totalCount: 0,
      // 当前页码
      pageNo: 1,
      // 每页数据条数
      pageSize: 20,
      rowsSelections: [5, 10, 20, 100]
    }
  },
  computed: {
    totalPages () {
      return window.parseInt(this.totalCount / this.pageSize) +
      ((this.totalCount % this.pageSize) ? 1 : 0)
    }
  },
  methods: {
    // 外部传入参数变更
    setTotal () {
      this.totalCount = this.total
    },
    setPage () {
      this.pageNo = this.page
    },
    setRows () {
      this.pageSize = this.rowsSelections.indexOf(this.rows) > -1 ? this.rows : 20
    },
    changed () {
      let pageNo = +this.pageNo
      const pageSize = +this.pageSize || 20
      if (isNaN(pageNo) || pageNo <= 0) {
        pageNo = 1
      }

      const totalPages = window.parseInt(this.totalCount / pageSize) +
      ((this.totalCount % pageSize) ? 1 : 0)
      if (pageNo > totalPages) {
        pageNo = totalPages
      }

      this.pageNo = pageNo
      this.pageSize = pageSize

      this.$emit('onChanged', { rows: pageSize, page: this.pageNo })
    }
  },
  watch: {
    total: 'setTotal',
    page: 'setPage',
    rows: 'setRows'
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
    this.setTotal()
    this.setPage()
    this.setRows()
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style type="text/css" lang="scss" scoped>
  @import 'index.scss';
</style>
