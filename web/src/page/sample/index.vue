<template>
  <div>
    <h4 class="title">创建表</h4>
    <hr/>
    <el-container>
      <el-main class="favorite-container" :style="{width: '15%'}">
        <v-tree :datas="myTableList" :id="'id'" :text="'name'"
                :onSelectionChanged="onTreeSelectionChanged" :editable="true"/>
      </el-main>
      <el-aside :style="{width: '84%'}" class="interfaces-list-container">
        <v-table-editor :width="width" :height="height" style="position: relative;"></v-table-editor>
      </el-aside>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'index',
  data () {
    return {
      width: 1000,
      height: 600,
      myTableList: [{
        id: 1,
        name: 'name1'
      }]
    }
  },
  computed: {
  },
  methods: {
    onTreeSelectionChanged (selected) {
      // tree只是单选可以, 所以这里使用selecte[0]
      // this.params = Object.assign({}, this.params, {favoriteCode: selected[0]})
    }
  },
  components: {
    'v-tree': () => import('@/components/tree'),
    'v-table-editor': () => import('@/components/table-editor')
  },
  mounted () {
  }
}
</script>

<!-- Add 'scoped' attribute to limit CSS to this component only -->
<style type="text/css" lang="scss" scoped>
  @import 'index.scss';
</style>
