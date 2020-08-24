<template>
  <div>
    <h4 class="title">筛选条件</h4>
    <hr/>
    <div class="filters">
      <el-form :inline="true" :model="filter" label-width="6em">
        <el-form-item label="编号">
          <el-input
            type='number' v-model="filter.interfacesId" placeholder="请输入接口编号" @keypress.enter.native="search"></el-input>
        </el-form-item>
        <el-form-item label="接口名称">
          <el-input v-model="filter.name" placeholder="请输入接口名称" @keypress.enter.native="search"></el-input>
        </el-form-item>
        <el-form-item label="控制器">
          <el-input v-model="filter.controller" placeholder="请输入控制器" @keypress.enter.native="search"></el-input>
        </el-form-item>
        <el-form-item label="URL">
          <el-input v-model="filter.url" placeholder="请输入URL" @keypress.enter.native="search"></el-input>
        </el-form-item>
        <el-form-item label="是否废弃">
          <v-selector ref="status" :data="{true: '是', false: '否'}" :onSelectionChanged="(d) => filter.deprecated = (d[0] || {}).id"
                      placeholder="请选择是否废弃"></v-selector>
        </el-form-item>
        <el-form-item label="后端服务">
          <v-selector ref="back"
            :id="'id'" :text="'name'"
            :onSelectionChanged="(d) => filter.javaId = (d[0] || {}).id"
            :filterable="true" :remote="true"
            placeholder="请选择后端服务（可输入名称进行搜索）"
            :url="javaListApi"
          ></v-selector>
        </el-form-item>
        <el-form-item label="前端项目">
          <v-selector ref="front"
            :id="'id'" :text="'name'"
            :onSelectionChanged="(d) => filter.endId = (d[0] || {}).id"
            :filterable="true" :remote="true"
            placeholder="请选择前端项目（可输入名称进行搜索）"
            :url="endListApi"
          ></v-selector>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search" icon="el-icon-search">搜索</el-button>
          <el-button type="info" @click="clear" icon="el-icon-delete">清空</el-button>
        </el-form-item>
      </el-form>
    </div>
    <h4 class="title">我的接口列表</h4>
    <hr/>
    <el-container>
      <el-main class="favorite-container" :style="{width: '20%'}">
        <v-tree :datas="favoriteTree" :id="'code'" :onSelectionChanged="onTreeSelectionChanged" :editable="true"
                :onCreate="onCreate" :onUpdate="onUpdate" :onSortChanged="onSortChanged"/>
      </el-main>
      <el-aside :style="{width: '79%'}" class="interfaces-list-container">
        <v-datagrid ref="dg" :columns="columns" :data-url="dataUrl" :count-url="countUrl" :params="params" :toolbar="toolbar" :checkable="true"/>
      </el-aside>
    </el-container>

    <v-dialog ref="favoriteBindingDialog"
              title="绑定收藏夹"
              :width="450"
              :ok="bindFavorite">
      <div slot="body" style="height: 300px; overflow-y: auto">
        <v-tree :datas="toBeSelectedTree" :id="'code'" ref="favoriteTree"/>
      </div>
    </v-dialog>
  </div>
</template>

<script>
/* eslint-disable */
import { cusInterfacesCountApi, cusInterfacesListApi, endListApi, javaListApi,
  deleteCusInterfaces, favoriteCreateApi, favoriteUpdateApi, favoriteTreeApi,
  sortFavoriteApi, bindFavoriteApi } from '@/config/api/inserv-api'
import { removeBlank } from '@/config/utils'

export default {
  name: 'interfaces',
  data () {
    return {
      form: {
        name: null,
        mark: null
      },
      endListApi,
      javaListApi,
      filter: {
        interfacesId: null,
        name: null,
        deprecated: null,
        url: null,
        controller: null,
        javaId: null,
        endId: null
      },
      interfacesList: [],
      dataUrl: cusInterfacesListApi,
      countUrl: cusInterfacesCountApi,
      favoriteTree: null,
      toBeSelectedTree: null,
      params: {},
      toolbar: [{
        title: '设置',
        icon: 'el-icon-setting',
        handler: () => {
          const selections = this.$refs.dg.getSelected()
          if (selections.length === 0) {
            this.$alert('请至少选择一个接口', '提示', {type: 'error'})
            return
          }
          this.editingIdList = selections.map(s => s.id)
          favoriteTreeApi()
            .then(d => {
              this.toBeSelectedTree = d || []
              this.$refs.favoriteBindingDialog.open()
            })
        }
      }],
      editingIdList: null,
      columns: [
        {
          field: 'action',
          header: '操作',
          width: 180,
          html: true,
          actions: [{
            text: (r) => `<a href="#/test/${r.interfacesId}/testDomain" target="_blank">执行</a>`
          }, {
            text: '删除',
            handler: (row) => this.delete(row.id)
          }]
        },
        {field: 'interfacesId', header: '接口编号', width: 80},
        {field: 'name', header: '接口名称', width: 200, html: true, formatter: (row, index, value) => '<a href="#/interfaces/view/' + row.interfacesId + '" target="_blank">' + value + '</a>'},
        {field: 'method', header: 'METHOD', width: 80},
        {field: 'deprecated', header: '废弃', html: true, width: 80, formatter: (row, index, value) => value ? '是'.fontcolor('red') : '否'},
        {field: 'url', header: 'URL', width: 120},
        {field: 'java.name', header: '后端服务', width: 160},
        {field: 'end.name', header: '前端项目', width: 160}
      ]
    }
  },
  mounted () {
    favoriteTreeApi()
      .then(d => {
        this.favoriteTree = d || []
      })
  },
  methods: {
    bindFavorite () {
      const favoriteCode = this.$refs.favoriteTree.getSelected()[0]

      bindFavoriteApi(this.editingIdList, favoriteCode)
        .then(d => {
          this.$success('操作成功！')
          this.$refs.favoriteBindingDialog.close()
          this.search()

          this.editingIdList.length = 0
          this.$refs.dg.clearSelections()
        })
    },
    onTreeSelectionChanged (selected) {
      // tree只是单选可以, 所以这里使用selecte[0]
      this.params = Object.assign({}, this.params, {favoriteCode: selected[0]})
    },
    onSortChanged (node, newIndex) {
      sortFavoriteApi(node.id, newIndex)
        .then(d => {
          console.log(d)
        })
        .catch(e => {
          console.log(e)
        })
    },
    async onCreate (text, parent) {
       return await favoriteCreateApi(parent ? parent.id : null, text)
    },
    async onUpdate (node) {
      console.log(node)
      await favoriteUpdateApi(node.id, node.text)
    },
    delete (id) {
      this.$confirm('删除将无法恢复，确认删除吗？', '提示', {type: 'warning'}).then(() => {
        deleteCusInterfaces(id)
          .then(() => {
            this.$success('删除成功！')
            this.search()
          })
      })
    },
    search () {
      this.params = Object.assign({favoriteCode: this.params.favoriteCode}, removeBlank(this.filter))
    },
    clear () {
      this.params = Object.assign({favoriteCode: this.params.favoriteCode})
      this.filter = {}
      this.$refs.back.clear()
      this.$refs.front.clear()
      this.$refs.status.clear()
    }
  },
  components: {
    'v-tree': () => import('@/components/tree'),
    'v-datagrid': () => import('@/components/datagrid'),
    'v-selector': () => import('@/components/selector'),
    'v-dialog': () => import('@/components/dialog')
  }
}
</script>

<style lang="scss" scoped>
  @import 'index';

  .time {
    font-size: 13px;
    color: #999;
  }

  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    width: 100%;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }
</style>
