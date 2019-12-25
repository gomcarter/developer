<template>
  <div>
    <h4 class="title">{{title}}</h4>
    <hr/>
    <el-form :model="form" ref="edit">
      <el-form-item label="接口名称:" label-width="8em" :rules="[{ required: true, message: '用例接口名称', trigger: ['blur', 'change'] }]" prop="name">
        <el-input v-model="form.name" placeholder="请输入用例接口名称" />
      </el-form-item>
        <el-form-item label="接口id:" label-width="8em" :rules="[{ required: true, message: '用例接口名称', trigger: ['blur', 'change'] }]" prop="fkInterfacesId">
          <el-input v-model="form.fkInterfacesId" placeholder="请输入用例接口id" />
      </el-form-item>
      <el-form-item label="入参配置:" label-width="8em">
        <el-input v-model="form.parmConfig"  placeholder="请输入入参配置"/>
      </el-form-item>
      <el-form-item label="结果验证:" label-width="8em">
        <el-input v-model="form.resultHandler"  placeholder="请输入结果验证"/>
      </el-form-item>
      <el-form-item label-width="8em">
        <el-button type="primary" @click="add" icon="el-icon-success">提交</el-button>
        <el-button type="info" @click="$router.go(-1)" icon="el-icon-back">返回</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { postInterTestCaseItem, putInterTestCaseItem, getInterTestCaseItemId } from '@/config/api/inserv-api'
export default {
  data () {
    return {
      title: '新增用例接口',
      form: {
        name: '',
        fkInterfacesId: '',
        resultHandler: '',
        fkTestCaseId: this.$route.params.fkTestCaseId,
        sort: 1,
        parmConfig: ''
      },
      result: ''
    }
  },
  computed: {},
  methods: {
    init () {
      if (this.$route.params.id) {
        this.title = '修改用例接口'
        getInterTestCaseItemId(this.$route.params.id).then((res) => {
          this.form = res
        }).catch((err) => {
          console.log(err)
        })
      }
    },
    add () {
      this.$refs.edit.validate((valid) => {
        if (valid) {
          this.$confirm('确定保存？', '提示', {type: 'info'}).then(() => {
            if (this.$route.params.id) {
              putInterTestCaseItem(this.form).then((res) => {
                this.$message({
                  message: '修改成功',
                  type: 'success'
                })
                this.$router.push(`/flow/testCaseItem/` + this.form.fkTestCaseId)
              }).catch(() => {
              })
            } else {
              postInterTestCaseItem(this.form).then((res) => {
                this.$transfer({
                  back: '继续添加',
                  buttons: [{
                    text: '去列表',
                    link: '/flow/testCaseItem/' + this.form.fkTestCaseId
                  }]
                })
              }).catch(() => {
              })
            }
          }).catch(() => {
          })
        }
      })
    }
  },
  watch: {},
  components: {
  },
  beforeCreate () {
  },
  cteated () {
  },
  beforeMount () {
  },
  mounted () {
    this.init()
  },
  beforeUpdate () {
  },
  updated () {
  },
  beforeDestroy () {
  },
  destroyed () {
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style type="text/css" lang="scss" scoped>
  @import 'index.scss';
</style>
