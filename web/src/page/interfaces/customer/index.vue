<template>
  <div class="customer">
    <div class="header">
      <img src="@/assets/img/logo.png" class="logo"/>
      <h3>开发者中心</h3>
    </div>
    <div class="index_more" v-if="data && data.id > 0" >
      <div class="index_chunk">
        <h4 class="title">接口详情</h4>
        <hr/>
        <el-form label-width="9em">
          <el-form-item label="接口名称：">{{data.name}}</el-form-item>
          <el-form-item label="访问类型：">{{data.method || 'GET'}}</el-form-item>
          <el-form-item label="所属项目：">{{data.end.name}}</el-form-item>
          <el-form-item label="系统说明：">
            <div><i class="pre red">{{data.end.mark}}</i></div>
          </el-form-item>
          <el-form-item label="所属模块：">{{data.java.name}}</el-form-item>
          <el-form-item v-for="(value, key) in ENV_DOMAIN_MAP" :label="value+ '：'" :key="key">{{`${data.java[key]}${data.url}`}}</el-form-item>
          <el-form-item label="接口说明：">
            <div v-html="(data.mark || '无')"></div>
          </el-form-item>
          <el-form-item label="接口参数：">
            <v-parameter :json="parameters || []"></v-parameter>
          </el-form-item>
          <el-form-item label="返回值：">
            <v-jsonformatter v-if="generatedReturns" :json="generatedReturns"></v-jsonformatter>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div v-else-if="data && data.id === -1">
      <div class="customer_container">
        <div class="result-container">
          <span class="el-icon-error failed"></span>
        </div>
        <br/>
        <br/>
        <h4 class="center"><b>接口不存在</b></h4>
      </div>
    </div>
    <div v-else>
      <div class="customer_container">
        <div class="result-container">
          <span class="el-icon-loading success"></span>
        </div>
        <br/>
        <br/>
        <h4 class="center"><b>加载中……</b></h4>
      </div>
    </div>
  </div>
</template>

<script>
import { getPublicsInterfacesApi } from '@/config/api/inserv-api'
import { formatDate, generateReturns } from '@/config/utils'
import { ENV_DOMAIN_MAP } from '@/config/mapping'

export default {
  name: 'publicInterfacesDetail',
  data () {
    return {
      ENV_DOMAIN_MAP,
      data: null,
      formatDate,
      returns: null,
      parameters: null,
      generatedReturns: null
    }
  },
  computed: {},
  methods: {
    init () {
      getPublicsInterfacesApi(this.$route.params.key).then((res) => {
        if (res) {
          this.data = res
          this.returns = JSON.parse(this.data.returns)
          this.generatedReturns = generateReturns(this.returns, this.data.java.wrapper)
          this.parameters = JSON.parse(this.data.parameters)
        } else {
          this.data = {id: -1}
        }
      })
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
<style type="text/css" lang="scss" scoped>
  @import 'index.scss';
</style>
