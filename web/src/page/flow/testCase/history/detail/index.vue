<template>
  <div>
    <h4 class="title">用例执行历史</h4>
    <hr/>
    <el-form ref="edit" label-width="8em">
      <el-form-item label="名称:">
        {{name}}
      </el-form-item>
      <el-form-item style="margin-left: -3em;">
        <v-runner ref='runner' :width="width" :height="height" style="line-height: 20px; "
                  :finished="finished" :prepared="finished"></v-runner>
      </el-form-item>
      <el-form-item>
        <el-button type="info" @click="$router.go(-1)" icon="el-icon-back">返回</el-button>
        <el-input v-model="times" placeholder="运行次数" type="number" :min="1" :max="1000" style="width: 88px"></el-input>
        <el-button type="success" @click="run(false)" :icon="disabled?'el-icon-loading':'el-icon-magic-stick'" :disabled="disabled">运行</el-button>
        <el-button type="success" @click="run(true)" :icon="disabled?'el-icon-loading':'el-icon-video-camera-solid'" :disabled="disabled">mock运行</el-button>
        <el-button type="success" @click="saveHistory" :icon="disabled?'el-icon-loading':'el-icon-video-camera-solid'" :disabled="disabled || cannotSave">保存执行结果</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { testCaseHistoryDetailApi, getTestCaseDetailApi } from '@/config/api/inserv-api'

export default {
  data () {
    return {
      cannotSave: true,
      times: 1,
      disabled: true,
      width: 1000,
      height: 618,
      id: this.$route.params.id,
      name: null
    }
  },
  computed: {},
  methods: {
    init () {
      if (this.id) {
        testCaseHistoryDetailApi(this.id).then((res) => {
          this.name = res.name
          const history = { env: res.env, historyList: JSON.parse(res.result) }
          setTimeout(() => {
            if (res.testCaseId) {
              getTestCaseDetailApi(res.testCaseId).then((tc) => {
                const data = { testCaseId: res.testCaseId, workflow: JSON.parse(tc.workflow), presetParams: JSON.parse(tc.presetParams) || [] }

                // 设置模型
                this.$refs.runner.setData(data)
                // 设置历史
                this.$refs.runner.setData(history)
              })
            } else {
              const data = { testCaseId: null, workflow: history.historyList[0].workflow }

              // 设置模型
              this.$refs.runner.setData(data)
              // 设置历史
              this.$refs.runner.setData(history)
            }
          }, 800)
        })
      }
    },
    run (mock) {
      this.disabled = true
      // 最大1000次
      this.times = this.times > 1000 ? 1000 : this.times
      this.times = this.times < 1 ? 1 : this.times
      this.$refs.runner.run(this.times, mock)
      this.cannotSave = false
    },
    saveHistory () {
      this.$refs.runner.saveHistory()
    },
    finished () {
      this.disabled = false
    }
  },
  components: {
    'v-runner': () => import('@/components/runner')
  },
  mounted () {
    this.width = this.$el.clientWidth - 100
    this.init()
  }
}
</script>

<style type="text/css" lang="scss" scoped>
  @import 'index.scss';
</style>
