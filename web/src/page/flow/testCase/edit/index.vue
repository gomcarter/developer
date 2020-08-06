<template>
  <div>
    <h4 class="title">{{title}}</h4>
    <hr/>
    <el-form :model="form" ref="edit" label-width="8em" style="max-width: 1100px">
      <el-form-item label="用例名称:" prop="name" required
                    :rules="[{ required: true, message: '请输入用例名称', trigger: ['blur', 'change'] }]">
        <el-input v-model="form.name" placeholder="请输入用例名称"></el-input>
      </el-form-item>
      <el-form-item label="预置参数:">
        <el-button type="primary" icon="el-icon-plus" @click="addPresetParams()" circle size="small"></el-button>
        在后续脚本中使用，如设置了参数名为：name=value，使用 $name 即可获得此参数对应的值value，不要重复命名
        <v-parameter-input :parameters="presetParams"></v-parameter-input>
      </el-form-item>
      <el-form-item label="配置流程:" label-width="8em">
        <div class="testcase_editor_container" :style="{width: width + 2 + 'px'}">
          <v-workflow ref="workflow" v-if="form.workflow" :data-list="form.workflow" :width="width" :height="height"></v-workflow>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="info" @click="$router.go(-1)" icon="el-icon-back">返回</el-button>
        <el-button type="primary" @click="save" :icon="disabled?'el-icon-loading':'el-icon-success'" :disabled="disabled">保存</el-button>
        <el-button type="primary" @click="batchImport" icon="el-icon-upload2">批量导入接口</el-button>
        <el-button type="success" @click="beautify" icon="el-icon-s-grid">美化</el-button>
        <el-button type="success" @click="test" icon="el-icon-magic-stick">测试</el-button>
      </el-form-item>
    </el-form>

    <v-dialog ref="runnerDialog" title="运行用例" :ok-text="'开始执行'" :cancel-text="'关闭'"
              :ok="runTest" :width="1300">
      <div slot="body">
        <v-runner ref='runner' :width="1100" :height="518"></v-runner>
      </div>
    </v-dialog>
  </div>
</template>

<script>
import { createTestCaseApi, getTestCaseDetailApi, updateTestCaseApi, functionListApi, getPackageApi, interfacesSimpleListApi } from '@/config/api/inserv-api'
import { getUrlHashParams } from '@/config/utils'
import { user } from '@/config/login'

export default {
  data () {
    return {
      width: 1000,
      height: 600,
      title: '新增用例',
      disabled: false,
      functionListApi,
      id: this.$route.params.id,
      packageId: null,
      form: {
        name: null,
        workflow: null
      },
      owner: true,
      presetParams: []
    }
  },
  computed: {},
  methods: {
    addPresetParams () {
      this.presetParams.push({key: '', value: '', type: 'text'})
      console.log(this.presetParams)
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
          this.$confirm((this.owner ? '' : '你即将复制并保存此用例到自己的用例列表中，') + '确定保存吗？', '提示', { type: 'info' }).then(() => {
            this.disabled = true
            // 有id且是自己的才能保存，否则都是新建
            if (this.id && this.owner) {
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
                  window.history.replaceState(null, document.title, `/#/flow/testCase/edit/${res}`)
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
        presetParams: JSON.stringify(this.presetParams),
        packageId: this.packageId
      }
    },
    test () {
      const r = this.$refs.workflow.validate()
      if (!r.result) {
        this.$message({ message: r.message, type: 'warning' })
      } else {
        this.$refs.runnerDialog.open()
        const data = { workflow: this.$refs.workflow.workflow(), presetParams: this.presetParams }
        setTimeout(() => this.$refs.runner.setData(data), 500)
      }
    },
    runTest () {
      this.$refs.runner.run(1)
    },
    beautify () {
      this.$refs.workflow.beautify()
    },
    loadMultipleInterfaces (interfacesIdList) {
      interfacesSimpleListApi({rows: 1000, idList: interfacesIdList})
        .then((res) => {
          // 加载接口
          if (res && res.length > 0) {
            this.$refs.workflow.batchAddNodes(res)
          }
          this.disabled = false
        })
    },
    batchImport () {
      this.$prompt('批量导入', '', {
        inputType: 'text',
        inputPlaceholder: '请输入接口编号，多个编号逗号隔开',
        inputValidator: (d) => (d || '').trim().length > 0 || '请输入至少一个接口编号'
      }).then((d) => {
        const value = d.value.replace(/，/g, ',')
        this.loadMultipleInterfaces(value.split(',').map(v => +v).filter(v => v))
      }).catch((d) => {
      })
    }
  },
  components: {
    'v-runner': () => import('@/components/runner'),
    'v-dialog': () => import('@/components/dialog'),
    'v-selector': () => import('@/components/selector'),
    'v-workflow': () => import('@/components/workflow'),
    'v-parameter-input': () => import('@/components/parameter-input')
  },
  mounted () {
    window.that1 = this

    const { packageId } = getUrlHashParams()
    if (packageId) {
      this.form.workflow = {}
      this.disabled = true
      this.packageId = packageId
      getPackageApi(packageId)
        .then((res) => {
          this.form.name = res.name
          setTimeout(() => {
            this.loadMultipleInterfaces(res.interfacesIdList)
          }, 800)
        })
    } else if (this.id) {
      this.owner = false
      this.title = '修改用例'
      getTestCaseDetailApi(this.id).then((res) => {
        this.form.name = res.name
        this.form.workflow = JSON.parse(res.workflow)
        this.presetParams = JSON.parse(res.presetParams)
        this.owner = res.userName === user()
      }).catch((err) => {
        console.log(err)
      })
    } else {
      this.$set(this.form, 'workflow', {})
    }
  }
}
</script>

<style type="text/css" lang="scss" scoped>
  @import 'index.scss';
</style>
