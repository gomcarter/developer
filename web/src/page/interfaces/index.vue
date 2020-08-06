<template>
  <div>
    <h4 class="title">筛选条件</h4>
    <hr/>
    <div class="filters">
      <el-form :inline="true" :model="filter" label-width="6em">
        <el-form-item label="编号">
          <el-input type='number' v-model="filter.id" placeholder="请输入编号" @keypress.enter.native="search"></el-input>
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
          <v-selector :data="{true: '是', false: '否'}" :onSelectionChanged="(d) => filter.deprecated = (d[0] || {}).id"
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
    <h4 class="title">接口列表</h4>
    <hr/>
    <v-datagrid ref="dg" :columns="columns" :data-url="dataUrl" :count-url="countUrl" :params="params"
                :checkable="true" :toolbar="toolbar" :onSelectionChanged="onSelectionChanged" :onLoadSuccess="onLoadSuccess"/>

    <v-dialog ref="packageDialog"
              title="接口打包"
              :width="1000"
              :ok="commitPackage">
      <el-form slot="body" :model="form" label-width="7em" ref="packageForm">
        <div class="mark margin-bottom20">一个功能（或者需求）的多个接口打包在一起，方便集中管理和查阅</div>
        <el-form-item prop="name" label="打包名称" required
                      :rules="[{ required: true, message: '请输入打包名称', trigger: ['blur', 'change'] }]">
          <el-input v-model="form.name" placeholder="请输入打包名称" ></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.mark" type="textarea" placeholder="请输入备注" rows="4"></el-input>
        </el-form-item>
        <v-datagrid :columns="selectedColumns" :load-data="selected" :pageable="false" :height="400"/>
        <hr/>
      </el-form>
    </v-dialog>
  </div>
</template>

<script>
import { interfacesCountApi, transferTocustomerApi, cusInterfacesFavorites, interfacesListApi, endListApi, javaListApi, deleteInterfaces, addPackageApi } from '@/config/api/inserv-api'
import { formatDate, removeBlank } from '@/config/utils'

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
        id: null,
        name: null,
        deprecated: null,
        url: null,
        controller: null,
        javaId: null,
        endId: null
      },
      dataUrl: interfacesListApi,
      countUrl: interfacesCountApi,
      params: {},
      selected: [],
      toolbar: [{
        title: '手动录入接口',
        icon: 'el-icon-plus',
        handler: () => this.$router.push('/interfaces/edit')
      }, {
        title: '接口打包',
        icon: 'el-icon-box',
        disabled: () => this.selected.length === 0,
        handler: () => this.$refs.packageDialog.open()
      }],
      async onLoadSuccess (data) {
        if (data.length === 0) {
          return
        }
        const favoritesIdList = await cusInterfacesFavorites(data.map(s => s.id))
        console.log(favoritesIdList)
        data.forEach(s => {
          s.transfered = favoritesIdList.indexOf(s.id) >= 0
        })
      },
      columns: [
        {
          field: 'action',
          header: '操作',
          width: 160,
          html: true,
          actions: [{
            text: (r) => `<a href="#/test/${r.id}/testDomain" target="_blank">执行</a>`
          }, {
            text: '删除',
            handler: (row) => this.delete(row.id)
          }, {
            text: '复制',
            handler: (row) => this.copy(row)
          }, {
            text: (r) => r.transfered ? '' : '收藏',
            handler: (row) => this.transfer(row)
          }]
        },
        {field: 'id', header: '编号', sort: 'id', width: 80},
        {field: 'name', header: '接口名称', sort: 'name', width: 200, html: true, formatter: (row, index, value) => '<a href="#/interfaces/view/' + row.id + '" target="_blank">' + value + '</a>'},
        {field: 'method', header: 'METHOD', sort: 'method', width: 80},
        {field: 'deprecated', header: '废弃', sort: 'deprecated', html: true, width: 80, formatter: (row, index, value) => value ? '是'.fontcolor('red') : '否'},
        {field: 'controller', header: '控制器', sort: 'controller', width: 180},
        {field: 'url', header: 'URL', sort: 'url', width: 120},
        {field: 'java.name', header: '后端服务', sort: 'fk_java_id', width: 120},
        {field: 'end.name', header: '前端项目', sort: 'fk_end_id', width: 160},
        {field: 'modifyTime', header: '更新时间', sort: 'modify_time', width: 200, formatter: (row, index, value) => formatDate(value)}
      ],
      selectedColumns: [
        {field: 'id', header: '编号', width: 80},
        {field: 'name', header: '接口名称', width: 200},
        {field: 'url', header: 'URL', width: 350},
        {field: 'java.name', header: '后端服务', width: 120},
        {field: 'end.name', header: '前端项目', width: 160}
      ]
    }
  },
  mounted () {
    window.that = this
  },
  methods: {
    transfer (row) {
      if (row.transfered) {
        return
      }

      transferTocustomerApi(row.id).then(() => {
        this.$success('收藏成功，可以在【接口自动化测试】>【我的接口列表】查看！')
        row.transfered = true
        this.$refs.dg.render()
      })
    },
    copy (row) {
      // 复制url为固定格式（沙翼需求），如：/platform/search/productWord/delete => ,platformSearchProductWordDelete:'/platform/search/productWord/delete'
      const key = row.url.split('/').filter(s => s).map(s => s[0].toUpperCase() + s.substr(1)).join('')
      this.$copyText(`,${key[0].toLowerCase() + key.substr(1)}:'${row.url}'`)
        .then((e) => this.$success('复制成功！'), (e) => this.$success('复制失败！'))
    },
    delete (id) {
      this.$confirm('删除将无法恢复，确认删除吗？', '提示', {type: 'warning'}).then(() => {
        deleteInterfaces(id)
          .then(() => {
            this.$success('删除成功！')
            this.search()
          })
      })
    },
    commitPackage () {
      this.$refs.packageForm.validate((valid) => {
        if (valid) {
          addPackageApi(Object.assign({interfacesIdList: this.selected.map(s => s.id)}, this.form))
            .then(res => {
              this.form.name = null
              this.form.mark = null
              this.$refs.dg.clearSelections()
              this.$refs.packageDialog.close()

              this.$confirm('打包成功！', '提示', {
                type: 'success',
                cancelButtonText: '关闭',
                confirmButtonText: '去查看'
              }).then(() => {
                this.$router.push(`/package/view/${res}`)
              })
            })
        }
      })
    },
    onSelectionChanged (selected) {
      this.selected.length = 0;
      (selected || []).forEach(s => {
        this.selected.push(s)
      })
    },
    search () {
      this.params = removeBlank(this.filter)
    },
    clear () {
      this.params = {}
      this.filter = {}
      this.$refs.back.clear()
      this.$refs.front.clear()
    }
  },
  components: {
    'v-datagrid': () => import('@/components/datagrid'),
    'v-selector': () => import('@/components/selector'),
    'v-dialog': () => import('@/components/dialog')
  }
}
</script>

<style lang="scss" scoped>
  @import 'index';
</style>
