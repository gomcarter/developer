<template>
  <div class="customer">
    <div class="header">
      <img src="@/assets/img/logo.png" class="logo"/>
      <h3>开发者中心</h3>
    </div>
    <div class="index_more">
      <div class="index_chunk">
        <h4 class="title">接口详情</h4>
        <hr/>
        <el-form v-if="data" label-width="9em">
          <el-form-item label="接口名称:">{{data.name}}<b v-if="data.deprecated" style="color:red">（已废弃）</b></el-form-item>
          <el-form-item label="访问类型:">{{data.method}}</el-form-item>
          <el-form-item label="所属项目:">{{data.end.name}}</el-form-item>
          <el-form-item label="header参数说明:"><i style="color:red;">header名:{{data.end.header}}<br>{{data.end.mark}}</i></el-form-item>
          <el-form-item label="所属模块:">{{data.java.name}}</el-form-item>
          <!--<el-form-item label="开发环境地址:"><a @click="linkTo(data, 'devDomain')" target="_blank">{{`${data.java.devDomain}${data.url}`}}</a></el-form-item>-->
          <el-form-item label="测试环境地址:">{{`${data.java.testDomain}${data.url}`}}</el-form-item>
          <!--<el-form-item label="预发环境地址:"><a @click="linkTo(data, 'prevDomain')" target="_blank">{{`${data.java.prevDomain}${data.url}`}}</a></el-form-item>-->
          <el-form-item label="线上环境地址:">{{`${data.java.onlineDomain}${data.url}`}}</el-form-item>
          <el-form-item label="创建时间:">{{formatDate(data.createTime)}}</el-form-item>
          <el-form-item label="接口参数:"><v-parameter :json="parameters || []"></v-parameter></el-form-item>
          <el-form-item label="接口说明:">
            <div v-html="(data.mark || '无')"></div>
          </el-form-item>
          <el-form-item label="返回值:">
            <v-jsonformatter v-if="generatedReturns" :json="generatedReturns"></v-jsonformatter>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { getInterfacesApi } from '@/config/api/inserv-api'
import { formatDate, generateReturns } from '@/config/utils'

export default {
  name: 'interfacesDetail',
  data () {
    return {
      data: null,
      formatDate,
      returns: null,
      parameters: null,
      generatedReturns: null
    }
  },
  computed: {
  },
  methods: {
    init () {
      getInterfacesApi(this.$route.params.id).then((res) => {
        this.data = res
        this.returns = JSON.parse(this.data.returns)
        this.generatedReturns = generateReturns(this.returns)
        this.parameters = JSON.parse(this.data.parameters)
      })
    },
    linkTo (data, env) {
      this.$router.push(`/test/${data.id}/${env}`)
    }
  },
  components: {
    'v-datagrid': () => import('@/components/datagrid'),
    'v-jsonformatter': () => import('@/components/jsonformatter'),
    'v-parameter': () => import('@/components/parameter')
  },
  mounted () {
    this.init()
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style type="text/css" lang="scss">
  @import 'index.scss';
</style>
