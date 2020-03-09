<template>
  <div>
    <h4 class="title">基本信息</h4>
    <hr/>
    <el-form label-width="6em">
      <el-form-item label="编号：">{{data.id}}</el-form-item>
      <el-form-item label="名称：">{{data.name}}</el-form-item>
      <el-form-item label="创建人：">{{data.userName}}</el-form-item>
      <el-form-item label="备注："><div class="mark">{{data.mark}}</div></el-form-item>
    </el-form>
    <h4 class="title">包含接口列表</h4>
    <hr/>
    <v-datagrid v-if="data && data.id " :columns="columns" :data-url="dataUrl" :pageable="false" :params="params"/>
    <h4 class="title" style="margin-top: 40px">测试用例</h4>
    <hr/>
    <el-form label-width="6em" v-if="data.testCaseId && data.id">
      <el-form-item style="margin-left: -3em;">
        <v-runner ref='runner' :width="width" :height="height" style="line-height: 20px; "
                  :finished="finished" :prepared="finished"></v-runner>
      </el-form-item>
      <el-form-item>
        <el-button type="info" @click="$router.go(-1)" icon="el-icon-back">返回</el-button>
        <el-button type="success" @click="run(false)" :icon="disabled?'el-icon-loading':'el-icon-magic-stick'" :disabled="disabled">运行</el-button>
        <el-button type="success" @click="run(true)" :icon="disabled?'el-icon-loading':'el-icon-video-camera-solid'" :disabled="disabled">mock运行</el-button>
        <el-button type="success" @click="editTestCase(data.testCaseId)" icon="el-icon-video-camera-solid">编辑测试用例</el-button>
      </el-form-item>
    </el-form>
    <el-form label-width="6em" v-else-if="data.id">
      <el-form-item>
        <el-button type="info" @click="$router.go(-1)" icon="el-icon-back">返回</el-button>
        <el-button type="success" @click="bindTestCase(data.id)" icon="el-icon-circle-plus-outline">绑定测试用例</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getPackageApi, interfacesListApi, getTestCaseDetailApi } from '@/config/api/inserv-api'
import { formatDate } from '@/config/utils'

export default {
  name: 'packageView',
  data () {
    return {
      disabled: false,
      width: 1000,
      height: 618,
      data: {
        id: null,
        name: null,
        userName: null,
        mark: null
      },
      dataUrl: interfacesListApi,
      pageable: false,
      params: {},
      columns: [
        {field: 'id', header: '编号', sort: 'id', width: 80},
        {field: 'name', header: '接口名称', sort: 'name', width: 200},
        {field: 'method', header: 'METHOD', sort: 'method', width: 80},
        {field: 'deprecated', header: '废弃', sort: 'deprecated', html: true, width: 80, formatter: (row, index, value) => value ? '是'.fontcolor('red') : '否'},
        {field: 'controller', header: '控制器', sort: 'controller', width: 200},
        {field: 'url', header: 'URL', sort: 'url', width: 120},
        {field: 'java.name', header: '后端服务', sort: 'fk_java_id', width: 120},
        {field: 'end.name', header: '前端项目', sort: 'fk_end_id', width: 160},
        {field: 'modifyTime', header: '更新时间', sort: 'modify_time', width: 200, formatter: (row, index, value) => formatDate(value)},
        {
          field: 'action',
          header: '操作',
          width: 130,
          html: true,
          actions: [{
            text: (row) => `<a href="#/interfaces/view/${row.id}" target="_blank">查看</a>`
          }]
        }
      ]
    }
  },
  mounted () {
    this.width = this.$el.clientWidth - 100
    console.log(this.width)

    if (this.$route.params.id) {
      getPackageApi(this.$route.params.id).then((res) => {
        this.data = res
        this.params = {rows: 1000, idList: this.data.interfacesIdList}

        if (this.data.testCaseId) {
          getTestCaseDetailApi(this.data.testCaseId).then((res) => {
            const workflow = JSON.parse(res.workflow)
            const presetParams = JSON.parse(res.presetParams)
            console.log(workflow)
            setTimeout(() => this.$refs.runner.setData({ workflow, presetParams }), 500)
          })
        }
      })
    }
  },
  methods: {
    editTestCase (testCaseId) {
      this.$router.push(`/flow/testCase/edit/${testCaseId}`)
    },
    bindTestCase (packageId) {
      this.$router.push(`/flow/testCase/edit?packageId=${packageId}`)
    },
    run (mock) {
      this.disabled = true
      this.$refs.runner.run(mock)
    },
    finished () {
      this.disabled = false
    }
  },
  components: {
    'v-datagrid': () => import('@/components/datagrid'),
    'v-selector': () => import('@/components/selector'),
    'v-dialog': () => import('@/components/dialog'),
    'v-runner': () => import('@/components/runner')
  }
}
</script>

<style lang="scss" scoped>
  @import 'index.scss';
</style>
