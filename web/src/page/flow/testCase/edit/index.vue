<template>
  <div>
    <h4 class="title">{{title}}</h4>
    <hr/>
    <el-form :model="form" ref="edit" label-width="8em">
      <el-form-item label="用例名称:" prop="name" required
                    :rules="[{ required: true, message: '请输入用例名称', trigger: ['blur', 'change'] }]">
        <el-input v-model="form.name" placeholder="请输入用例名称"></el-input>
      </el-form-item>
      <el-form-item label="预置参数:">
        <el-button type="primary" icon="el-icon-plus" @click="addPresetParams()" circle size="small"></el-button>
        在后续脚本中使用，如设置了参数名为：name=value，使用 $name 即可获得此参数对应的值value，不要重复命名
        <el-form v-if="presetParams && presetParams.length > 0">
          <el-form-item v-for="(preset, index) of presetParams" v-bind:key="index">
            <el-input placeholder="请输入预置参数名" class="preset_params_key" v-model="preset.key">
              <template slot="prepend"><span class="table_title">参数名</span></template>
            </el-input>
            <div class="el-input-group__prepend preset_params_value_title"><span>参数值</span></div>
            <el-input  v-if="preset.fix" v-model="preset.value" placeholder="请输入一个固定值" class="preset_params_value"></el-input>
            <v-selector v-else class="preset_params_value"
                        :id="'id'" :text="'name'"
                        :onSelectionChanged="(d) => preset.functionId = (d[0] || {}).id"
                        :filterable="true" :remote="true"
                        placeholder="请输选择一个脚本，输入名称进行搜索"
                        :load="[preset.functionId]"
                        :url="functionListApi"
            ></v-selector>
            <el-checkbox v-model="preset.fix">固定值</el-checkbox>
            <el-button type="danger" icon="el-icon-delete" @click="delPresetParams(index)" size="small" circle></el-button>
          </el-form-item>
        </el-form>
      </el-form-item>
<!--      <el-form-item label="备注:">-->
<!--        <el-input v-model="form.mark" type="textarea" rows="5" placeholder="请输入备注信息"></el-input>-->
<!--      </el-form-item>-->
      <el-form-item label="配置流程:" label-width="8em" v-if="id">
        <div class="container" :style="{width: width + 2 + 'px', height: height + 2 + 'px'}">
          <v-workflow ref="workflow" v-if="form.workflow" :data-list="form.workflow" :width="width" :height="height"></v-workflow>
        </div>
      </el-form-item>
      <el-form-item label="配置流程:" label-width="8em" v-else>
        <div class="container" :style="{width: width + 2 + 'px', height: height + 2 + 'px'}">
          <v-workflow ref="workflow" :width="width" :height="height"></v-workflow>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save" :icon="disabled?'el-icon-loading':'el-icon-success'" :disabled="disabled">提交</el-button>
        <el-button type="info" @click="$router.go(-1)" icon="el-icon-back">返回</el-button>
        <el-button type="success" @click="test" icon="el-icon-magic-stick">测试</el-button>
      </el-form-item>
    </el-form>

    <v-dialog ref="runnerDialog" title="运行用例" :ok-text="'开始执行'" :cancel-text="'关闭'"
              :ok="runTest" :width="1200">
      <div slot="body">
        <v-runner ref='runner' :width="1100" :height="518"></v-runner>
      </div>
    </v-dialog>
  </div>
</template>

<script>
import { createTestCaseApi, getTestCaseDetailApi, updateTestCaseApi, functionListApi } from '@/config/api/inserv-api'

export default {
  data () {
    return {
      width: 1000,
      height: 1000,
      title: '新增用例',
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
        this.title = '修改用例'
        getTestCaseDetailApi(this.id).then((res) => {
          this.form.name = res.name
          this.form.mark = res.mark
          this.form.workflow = JSON.parse(res.workflow)
          this.presetParams = JSON.parse(res.presetParams)
        }).catch((err) => {
          console.log(err)
        })
      }
    },
    save () {
      this.$refs.edit.validate((valid) => {
        // 校验流程图
        if (!valid) {
          this.$message({ message: '请输入用例名称', type: 'warning' })
          return
        }
        const r = this.$refs.workflow.validate()
        if (!r.result) {
          this.$message({ message: r.message, type: 'warning' })
        } else {
          this.$confirm('确定保存？', '提示', { type: 'info' }).then(() => {
            this.disabled = true
            if (this.id) {
              updateTestCaseApi(this.id, this.generateParams()).then((res) => {
                this.$transfer({
                  back: '再次编辑',
                  buttons: [{
                    text: '去列表',
                    link: '/flow/testCase'
                  }]
                })
              }).catch(() => {
                this.disabled = false
              })
            } else {
              createTestCaseApi(this.generateParams())
                .then((res) => {
                  this.$transfer({
                    back: '继续添加',
                    buttons: [{
                      text: '去列表',
                      link: '/flow/testCase'
                    }]
                  })
                }).catch(() => {
                  this.disabled = false
                })
            }
          })
        }
      })
    },
    generateParams () {
      return {
        workflow: JSON.stringify(this.$refs.workflow.workflow()),
        name: this.form.name,
        mark: this.form.mark,
        presetParams: JSON.stringify(this.presetParams)
      }
    },
    test () {
      const r = this.$refs.workflow.validate()
      if (!r.result) {
        this.$message({ message: r.message, type: 'warning' })
      } else {
        this.$refs.runnerDialog.open()
        const data = { workflow: this.$refs.workflow.workflow(), presetParams: this.presetParams }
        setTimeout(() => this.$refs.runner.setData(data), 1)
      }
    },
    runTest () {
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
    this.init()
  }
}
</script>

<style type="text/css" lang="scss" scoped>
  @import 'index.scss';
</style>
