<template>
  <div>
    <h4 class="title">{{title}}</h4>
    <hr/>
    <el-form :model="form" ref="edit" label-width="8em" class="min_width">
      <el-form-item label="服务名称:" :rules="[{ required: true, message: '请输入模块名称', trigger: ['blur', 'change'] }]" prop="name">
        <el-input v-model="form.name" placeholder="请输入模块名称"></el-input>
      </el-form-item>
      <el-form-item label="应用别名:" :rules="[{ required: true, message: '请输入应用别名', trigger: ['blur', 'change'] }]" prop="alias">
        <el-input v-model="form.alias" placeholder="请输入应用别名"></el-input>
      </el-form-item>
      <el-form-item v-for="(value, key) in ENV_DOMAIN_MAP"
                    :key="key"
                    :label="value + '域名:'"
                    :rules="[{ required: true, message: '请输入' + value + '域名', trigger: ['blur', 'change'] }]"
                    :prop="key">
        <el-input v-model="form[key]" :placeholder="'请输入' + value + '域名'" ></el-input>
      </el-form-item>
      <el-form-item label="返回值包装类:">
        <el-button type="primary" icon="el-icon-plus" @click="addWrapperColumn()" circle size="small"></el-button>
        <div class="mark">返回数据字段只能设置一个</div>
      </el-form-item>
      <el-form-item v-if="wrapper" v-for="(w, index) of wrapper" v-bind:key="index">
        <el-input placeholder="请输入字段名" class="param-key" v-model="w.key"></el-input>
        <el-input placeholder="备注，没有可以不写" class="param-value" v-model="w.value"></el-input>
        <el-radio v-model="w.data" :label="true" @change="onDataColumnSwitch(index)">设置为数据字段</el-radio>
        <el-button type="danger" icon="el-icon-delete" @click="deleteWrapperColumn(index)" circle size="small"></el-button>
      </el-form-item>
      <el-form-item label="包装类预览:" style="min-height: 400px;">
        <v-jsonformatter v-if="preview" :json="preview" :height="360"></v-jsonformatter>
      </el-form-item>
      <el-form-item >
        <el-button type="primary" @click="add" icon="el-icon-success">提交</el-button>
        <el-button type="info" @click="$router.go(-1)" icon="el-icon-back">返回</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { addJavaApi, getJavaApi, updateJavaApi } from '@/config/api/inserv-api'
import { ENV_DOMAIN_MAP } from '@/config/mapping'

export default {
  data () {
    return {
      title: '新增模块',
      form: {
        id: null,
        name: null,
        alias: null
      },
      wrapper: [],
      ENV_DOMAIN_MAP,
      preview: {}
    }
  },
  computed: {},
  methods: {
    init () {
      if (this.$route.params.id) {
        getJavaApi(this.$route.params.id).then((res) => {
          this.form = res
          if (res.wrapper) {
            this.wrapper = JSON.parse(res.wrapper)
          }
        }).catch((err) => {
          console.log(err)
        })
      }
      this.title = this.$route.params.id ? '修改后端服务' : '新增后端服务'

      // 插入环境
      for (const key in ENV_DOMAIN_MAP) {
        this.$set(this.form, key, '')
      }

      this.previewUpdater()
    },
    previewUpdater () {
      const timer = setInterval(() => {
        // 离开当前页面，停止刷新
        if (!this.$route.path.startsWith('/provider/list/edit')) {
          clearInterval(timer)
        }
        if (this.wrapper) {
          const w = {}
          this.wrapper.forEach(s => {
            w[s.key] = s.value
          })
          if (JSON.stringify(w) === JSON.stringify(this.preview)) {
            // 如果没有变化则不更新
            return
          }

          this.preview = null
          setTimeout(() => {
            this.preview = w
          })
        }
      }, 500)
    },
    add () {
      this.$refs.edit.validate((valid) => {
        if (valid) {
          this.$confirm('确定保存？', '提示', {type: 'info'}).then(() => {
            if (this.wrapper != null && this.wrapper.length > 0) {
              this.form.wrapper = JSON.stringify(this.wrapper)
            }
            if (this.$route.params.id) {
              updateJavaApi(this.form).then((res) => {
                this.$transfer({
                  back: '返回编辑',
                  buttons: [{
                    text: '去列表',
                    link: '/provider/list'
                  }]
                })
              }).catch((err) => {
                console.log(err)
              })
            } else {
              addJavaApi(this.form).then((res) => {
                this.$transfer({
                  back: '继续添加',
                  buttons: [{
                    text: '去列表',
                    link: '/provider/list'
                  }]
                })
              }).catch((err) => {
                console.log(err)
              })
            }
          })
        }
      })
    },
    addWrapperColumn () {
      this.wrapper.push({key: '', value: '', data: this.wrapper.filter(s => s.data).length === 0})
    },
    deleteWrapperColumn (i) {
      this.wrapper.splice(i, 1)
      if (this.wrapper.filter(s => s.data).length === 0 && this.wrapper.length > 0) {
        this.wrapper[0].data = true
      }
    },
    onDataColumnSwitch (index) {
      this.wrapper.forEach(s => {
        s.data = false
      })
      this.wrapper[index].data = true
    }
  },
  mounted () {
    this.init()
  },
  components: {
    'v-jsonformatter': () => import('@/components/jsonformatter')
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style type="text/css" lang="scss" scoped>
  @import 'index.scss';
</style>
