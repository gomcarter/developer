<template>
  <div class="detail">
    <h4 class="title">接口详情 &#12288;&#12288;
      <el-button v-if="versioned.length > 0" type="primary" circle icon="el-icon-timer" title="查看历史版本" size="small" @click="listVersioned"></el-button></h4>
    <hr/>
    <el-form v-if="data" label-width="9em">
      <el-form-item label="接口名称：">
        <div>{{data.name}}<b v-if="data.deprecated" style="color:red">（已废弃）</b></div>
        <div class="history" v-if="currentVersioned">{{currentVersioned.name}}<b v-if="currentVersioned.deprecated" style="color:red">（已废弃）</b></div>
      </el-form-item>
      <el-form-item label="控制器：">
        <div>{{data.controller}}</div>
        <div class="history" v-if="currentVersioned">{{currentVersioned.controller}}</div>
      </el-form-item>
      <el-form-item label="访问类型：">
        <div>{{data.method}}</div>
        <div class="history" v-if="currentVersioned">{{currentVersioned.method}}</div>
      </el-form-item>
      <el-form-item label="所属项目：">
        <div>{{data.end.name}}</div>
        <div class="history" v-if="currentVersioned">{{currentVersioned.end.name}}</div>
      </el-form-item>
      <el-form-item label="header参数说明：">
        <div><i style="color:red;">header名：{{data.end.header}}<br>{{data.end.mark}}</i></div>
        <div class="history" v-if="currentVersioned"><i>header名：{{currentVersioned.end.header}}<br>{{currentVersioned.end.mark}}</i></div>
      </el-form-item>
      <el-form-item label="所属模块：">
        <div>{{data.java.name}}</div>
        <div class="history" v-if="currentVersioned">{{currentVersioned.java.name}}</div>
      </el-form-item>
      <el-form-item label="开发地址：">
        <div><a @click="linkTo(data, 'devDomain')" target="_blank">{{`${data.java.devDomain}${data.url}`}}</a></div>
        <span>mock地址：<a :href="originMockUrl(data.java.devDomain, data.url)" target="_blank">{{originMockUrl(data.java.devDomain, data.url)}}</a></span>
      </el-form-item>
      <el-form-item label="测试地址：">
        <div><a @click="linkTo(data, 'testDomain')" target="_blank">{{`${data.java.testDomain}${data.url}`}}</a></div>
        <span>mock地址：<a :href="originMockUrl(data.java.testDomain, data.url)" target="_blank">{{originMockUrl(data.java.testDomain, data.url)}}</a></span>
      </el-form-item>
      <el-form-item label="预发地址：">
        <div><a @click="linkTo(data, 'prevDomain')" target="_blank">{{`${data.java.prevDomain}${data.url}`}}</a></div>
        <span>mock地址：<a :href="originMockUrl(data.java.prevDomain, data.url)" target="_blank">{{originMockUrl(data.java.prevDomain, data.url)}}</a></span>
      </el-form-item>
      <el-form-item label="生产地址：">
        <div><a @click="linkTo(data, 'onlineDomain')" target="_blank">{{`${data.java.onlineDomain}${data.url}`}}</a></div>
        <span>mock地址：<a :href="originMockUrl(data.java.onlineDomain, data.url)" target="_blank">{{originMockUrl(data.java.onlineDomain, data.url)}}</a></span>
      </el-form-item>
      <el-form-item label="备用mock地址：">
        <div><a :href="mockUrl(data.hash)" target="_blank">{{mockUrl(data.hash)}}</a></div>
        <div class="history" v-if="currentVersioned"><a :href="mockUrl(currentVersioned.hash)" target="_blank">{{mockUrl(currentVersioned.hash)}}</a></div>
      </el-form-item>
      <el-form-item label="开放地址：">
        <div><a :href="publicUrl(data.publicId)" target="_blank">{{publicUrl(data.publicId)}}</a></div>
      </el-form-item>
      <el-form-item label="创建时间：">{{formatDate(data.createTime)}}</el-form-item>
      <el-form-item label="更新时间：">{{formatDate(data.modifyTime)}}</el-form-item>
      <el-form-item label="接口说明：">
        <div v-html="(data.mark || '无')"></div>
        <div class="history" v-if="currentVersioned" v-html="(currentVersioned.mark || '无')"></div>
      </el-form-item>
      <el-form-item label="接口参数：">
        <v-parameter :json="parameters || []"></v-parameter>
        <v-parameter class="history" v-if="currentVersioned" :json="versionedParameters || []"></v-parameter>
      </el-form-item>
      <el-form-item label="返回值：">
        <div v-if="currentVersioned">
          <v-jsonformatter v-if="generatedReturns" :json="generatedReturns" style="width: 48%;display: inline-block;"></v-jsonformatter>
          <v-jsonformatter class="history" v-if="currentVersioned && versionedGeneratedReturns" style="width: 48%;display: inline-block;"
                           :json="versionedGeneratedReturns"></v-jsonformatter>
        </div>
        <div v-else>
          <v-jsonformatter v-if="generatedReturns" :json="generatedReturns"></v-jsonformatter>
        </div>
      </el-form-item>
      <!--<el-form-item label="返回值数据结构：">-->
        <!--<v-jsonformatter :json="returns"></v-jsonformatter>-->
      <!--</el-form-item>-->
      <el-form-item label="备注：">
        <el-button type="primary" @click="addMark" icon="el-icon-plus">添加备注</el-button>
        <div v-if="marks && marks.length > 0" v-for="(m, i) of marks" v-bind:key="i">
          <span class="user">[{{m.user === username ? '我' : m.user }}]</span>&#12288;
          <span class="date">[{{formatDate(m.createTime)}}]：</span>&#12288;
          <span class="mark">{{m.mark}}</span>
        </div>
      </el-form-item>
    </el-form>
    <v-dialog ref="historyDialog"
              title="选择一个历史版本进行对比"
              :width="800"
              :ok="claimVersioned">
      <div slot="body">
        <v-datagrid ref="dg" :loadData="versioned" :checkable="true" :singleCheck="true" :columns="columns" :pageable="false"/>
      </div>
    </v-dialog>
  </div>
</template>

<script>
import { getInterfacesApi, interfacesVersionedListApi, getInterfaceMarkApi, addInterfaceMarkApi, mockUrl,
  originMockUrl, publicUrl } from '@/config/api/inserv-api'
import { formatDate, generateReturns } from '@/config/utils'
import { user } from '@/config/login'

export default {
  name: 'interfacesDetail',
  data () {
    return {
      publicUrl,
      mockUrl,
      originMockUrl,
      data: null,
      formatDate,
      returns: null,
      parameters: null,
      generatedReturns: null,
      versioned: [],
      currentVersioned: null,
      marks: null,
      username: user(),
      columns: [
        {field: 'deprecated', header: '废弃', sort: 'deprecated', html: true, width: 80, formatter: (row, index, value) => value ? '是'.fontcolor('red') : '否'},
        {field: 'url', header: 'URL地址', sort: 'url', width: 320},
        {field: 'modifyTime', header: '上次更新时间', sort: 'modify_time', width: 200, formatter: (row, index, value) => formatDate(value)}
      ]
    }
  },
  computed: {
  },
  methods: {
    claimVersioned () {
      const current = this.$refs.dg.getSelected()
      if (current != null && current.length > 0) {
        this.currentVersioned = current[0]

        this.versionedReturns = JSON.parse(this.currentVersioned.returns)
        this.versionedGeneratedReturns = generateReturns(this.versionedReturns)
        this.versionedParameters = JSON.parse(this.currentVersioned.parameters)
      } else {
        this.currentVersioned = null
      }
      this.$refs.historyDialog.close()
    },
    listVersioned () {
      this.$refs.historyDialog.open()
    },
    init () {
      const id = this.$route.params.id
      getInterfacesApi(id).then((res) => {
        this.data = res
        this.returns = JSON.parse(this.data.returns)
        this.generatedReturns = generateReturns(this.returns)
        this.parameters = JSON.parse(this.data.parameters)
      })

      interfacesVersionedListApi(id).then((res) => {
        this.versioned = res || []
      })

      getInterfaceMarkApi(id).then((res) => {
        this.marks = res
      })
    },
    linkTo (data, env) {
      this.$router.push(`/test/${data.id}/${env}`)
    },
    addMark () {
      this.$prompt('添加备注', '', {
        inputType: 'textarea',
        inputPlaceholder: '请输入备注信息',
        inputValidator: (d) => (d || '').trim().length > 0 || '请输入备注信息'
      }).then((d) => {
        const id = this.$route.params.id
        const mark = d.value
        addInterfaceMarkApi(id, mark)
          .then(d => {
            this.marks.unshift({user: this.username, createTime: new Date().getTime(), mark: mark})
          })
      }).catch((d) => {
      })
    }
  },
  components: {
    'v-datagrid': () => import('@/components/datagrid'),
    'v-jsonformatter': () => import('@/components/jsonformatter'),
    'v-parameter': () => import('@/components/parameter'),
    'v-dialog': () => import('@/components/dialog')
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
