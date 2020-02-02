<template>
  <div>
    <h4 class="title">执行用例</h4>
    <hr/>
    <el-form :model="form" ref="edit" label-width="8em">
      <el-form-item label="用例名称:">
        {{form.name}}
      </el-form-item>
      <el-form-item style="margin-left: -3em;">
        <v-runner ref='runner' :width="width" :height="height" style="line-height: 20px; "></v-runner>
      </el-form-item>
      <el-form-item>
        <el-button type="info" @click="$router.go(-1)" icon="el-icon-back">返回</el-button>
        <el-button type="success" @click="run" icon="el-icon-magic-stick">测试</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getTestCaseDetailApi, functionListApi } from '@/config/api/inserv-api'

export default {
  data () {
    return {
      width: 1000,
      height: 618,
      disabled: false,
      functionListApi,
      id: this.$route.params.id,
      form: {
        name: null,
        mark: null,
        workflow: null
      },
      presetParams: []
    }
  },
  computed: {},
  methods: {
    addPresetParams () {
      let obj = {key: '', value: '', functionId: '', fix: true}
      this.presetParams.push(obj)
    },
    delPresetParams (i) {
      this.presetParams.splice(i, 1)
    },
    init () {
      if (this.id) {
        getTestCaseDetailApi(this.id).then((res) => {
          this.form.name = res.name
          this.form.mark = res.mark
          this.form.workflow = JSON.parse(res.workflow)
          this.presetParams = JSON.parse(res.presetParams)

          const data = { workflow: this.form.workflow, presetParams: this.presetParams }
          setTimeout(() => this.$refs.runner.setData(data), 500)
        })
      }
    },
    run () {
      this.$refs.runner.run()
    }
  },
  components: {
    'v-runner': () => import('@/components/runner'),
    'v-dialog': () => import('@/components/dialog'),
    'v-selector': () => import('@/components/selector'),
    'v-workflow': () => import('@/components/workflow')
  },
  mounted () {
    window.that = this
    this.width = this.$el.clientWidth
    console.log(this.$el.clientWidth)
    this.init()
  }
}
</script>

<style type="text/css" lang="scss" scoped>
  @import 'index.scss';
</style>
