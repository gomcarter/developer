<template>
  <div class="detail">
    <h4 class="title">{{title}}（待开发）</h4>
    <hr/>
    <el-form :model="form" label-width="9em" ref="edit">
      <el-form-item label="接口名称:" :rules="[{ required: true, message: '请输入接口名称', trigger: ['blur', 'change'] }]" prop="name">
        <el-input v-model="form.name" placeholder="请输入接口名称" ></el-input>
      </el-form-item>
      <el-form-item label="控制器:">
        <el-input v-model="form.controller" placeholder="请输入控制器" ></el-input>
      </el-form-item>
      <el-form-item label="访问类型:">
        <v-selector :data="{ GET:'GET', POST: 'POST', PUT: 'PUT', PATCH: 'PATCH', DELETE: 'DELETE' }"
                    :onSelectionChanged="(d) => form.method = (d || []).map(s => s.id).join(',')"
                    :multiple="true" placeholder="请选择访问类型"></v-selector>
      </el-form-item>
      <el-form-item label="所属项目:">
        <v-selector
          :id="'id'" :text="'name'"
          :onSelectionChanged="(d) => form.endId = (d[0] || {}).id"
          :filterable="true" :remote="true"
          placeholder="请选择前端项目（可输入名称进行搜索）"
          :url="endListApi"
        ></v-selector>
      </el-form-item>
      <el-form-item label="所属模块:">
        <v-selector
          :id="'id'" :text="'name'"
          :onSelectionChanged="(d) => form.javaId = (d[0] || {}).id"
          :filterable="true" :remote="true"
          placeholder="请选择后端服务（可输入名称进行搜索）"
          :url="javaListApi"
        ></v-selector>
      </el-form-item>
      <el-form-item label="接口参数:">
        <v-parameter :json="parameters || []"></v-parameter>
      </el-form-item>
      <el-form-item label="接口说明:">
        <el-input v-model="form.mark" placeholder="接口说明" type="textarea" :rows="10"></el-input>
      </el-form-item>
      <el-form-item label="返回值:">
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getInterfacesApi, endListApi, javaListApi } from '@/config/api/inserv-api'
import { formatDate, generateReturns } from '@/config/utils'

export default {
  name: 'interfacesDetail',
  data () {
    return {
      endListApi,
      javaListApi,
      title: '添加接口',
      form: {
        name: null,
        controller: null,
        endId: null,
        javaId: null
      },
      data: null,
      formatDate,
      returns: null,
      parameters: null,
      generatedReturns: null,
      versioned: []
    }
  },
  computed: {
  },
  methods: {
    init () {
      const id = this.$route.params.id
      if (id) {
        this.title = '修改接口'
        getInterfacesApi(id).then((res) => {
          this.data = res
          this.returns = JSON.parse(this.data.returns)
          this.generatedReturns = generateReturns(this.returns)
          this.parameters = JSON.parse(this.data.parameters)
        })
      }
    }
  },
  components: {
    'v-datagrid': () => import('@/components/datagrid'),
    'v-jsonformatter': () => import('@/components/jsonformatter'),
    'v-parameter': () => import('@/components/parameter'),
    'v-selector': () => import('@/components/selector')
  },
  mounted () {
    this.init()
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style type="text/css" lang="scss" scoped>
  @import 'index.scss';
</style>
