<template>
  <div>
    <h4 class="title">单接口批量执行用例</h4>
    <hr/>
    <el-form ref="edit" label-width="8em">
      <el-form-item label="接口名称:">
        {{name}}
      </el-form-item>
      <el-form-item label="并行执行:">
        <el-switch v-model="fork"></el-switch>
      </el-form-item>
      <el-form-item label="执行用例:">
        <v-selector :id="'id'" :text="'name'" v-if="selections"
                    style="width: 35%; text-indent: 0; margin-left: 20px;"
                    :onSelectionChanged="onSelectCusInterfacesItem" placeholder="请选择一个历史保存的用例"
                    :remote="true" :multiple="true" :url="getCusInterfacesItemListApi"
                    :load="selections" ref="selector"
                    :extraParams="id ? {customInterfacesId: id, rows: 10000} : {customInterfacesId: -1}"
        ></v-selector>
      </el-form-item>
      <el-form-item style="margin-left: -3em;">
        <v-runner ref='runner' :width="width" :height="height" style="line-height: 20px; "
                  :finished="finished" :prepared="finished" :envChanged="envChanged"></v-runner>
      </el-form-item>
      <el-form-item>
        <el-button type="info" @click="$router.go(-1)" icon="el-icon-back">返回</el-button>
        <el-button type="success" @click="run(false)" :icon="disabled?'el-icon-loading':'el-icon-magic-stick'" :disabled="disabled">运行</el-button>
        <el-button type="success" @click="saveHistory" :icon="disabled?'el-icon-loading':'el-icon-video-camera-solid'" :disabled="disabled">保存执行结果</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getCusInterfacesItemListApi, getInterfacesApi, getCusInterfacesApi, getInterfacesHeadersApi } from '@/config/api/inserv-api'
import { unionParams } from '@/config/utils'

export default {
  data () {
    return {
      getCusInterfacesItemListApi,
      width: 1000,
      height: 618,
      disabled: true,
      id: this.$route.params.cusInterfacesId,
      name: null,
      fork: false,
      face: null,
      cus: null,
      selections: null,
      items: null,
      env: 'testDomain'
    }
  },
  computed: {},
  methods: {
    envChanged (env) {
      this.env = env
      this.loadItems()
    },
    onSelectCusInterfacesItem (selections) {
      this.selections = (selections || []).map(s => s.id)
      this.$refs.runner.deleteAll()
      this.$refs.runner.batchAddNodes(this.face, selections)
    },
    async init () {
      if (this.id) {
        this.cus = await getCusInterfacesApi(this.id)
        this.face = await getInterfacesApi(this.cus.interfacesId)
        this.name = this.face.complexName
        this.items = await getCusInterfacesItemListApi({customInterfacesId: this.cus.id, rows: 10000})
        if (this.items && this.items.length > 0) {
          this.selections = this.items.map(s => s.id)
          this.loadItems()
        }
      }
    },
    run (mock) {
      this.disabled = true
      // 最大1000次
      this.$refs.runner.run(1, mock, this.fork)
    },
    saveHistory () {
      this.$refs.runner.saveHistory()
    },
    finished () {
      this.disabled = false
    },
    async loadItems () {
      try {
        this.disabled = true
        const headers = await getInterfacesHeadersApi(this.env, this.face.end.id, true)
        const items = this.items.filter(s => this.selections.indexOf(s.id) >= 0)
          .map(s => {
            s.headers = s.cusHeaders ? JSON.parse(s.cusHeaders) : []
            s.headers = unionParams(headers.map(s => s), s.headers, true)
            return s
          })

        setTimeout(() => {
          this.$refs.runner.deleteAll()
          this.$refs.runner.batchAddNodes(this.face, items)

          this.disabled = false
        }, 800)
      } catch (e) {
        this.$alert('默认登录授权失败', '提示', {type: 'error'})
      }
    }
  },
  components: {
    'v-selector': () => import('@/components/selector'),
    'v-runner': () => import('@/components/runner')
  },
  mounted () {
    this.width = this.$el.clientWidth - 100
    this.init()

    window.tt = this
  }
}
</script>

<style type="text/css" lang="scss" scoped>
  @import 'index.scss';
</style>
