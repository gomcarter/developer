<template>
  <el-form v-if="parameters && parameters.length > 0" class="params-form">
    <el-form-item label="" v-for="(param, index) in parameters" :key="index" v-if="param.type" style="margin-bottom: 22px;">
      <el-input placeholder="请输入参数名" class="param-key v-top" v-model="param.key">
      </el-input>
      <span class="v-top">=</span>
      <el-date-picker v-if="($set(param, 'fix', param.fix == null ? true : param.fix)) && param.inputType === 'datetime'"
                      :placeholder="(param.comment || '请输入参数') + (param.notNull ? '，必填项' : '')"
                      v-model="param.defaults"
                      class="param-value"
                      value-format="yyyy-MM-dd HH:mm:ss"
                      :type="param.inputType"
                      :name="param.key">
      </el-date-picker>
      <el-switch v-else-if="param.fix && param.inputType === 'switch'"
                 v-model="param.defaults"
                 :name="param.key">
      </el-switch>
      <el-input v-else-if="param.fix"
                :placeholder="(param.comment || '请输入参数') + (param.notNull ? '，必填项' : '')"
                class="param-value"
                :rows="10"
                :type="param.inputType"
                v-model="param.defaults"
                :name="param.key">
        <template slot="prepend"><span>参数值</span></template>
      </el-input>
      <v-selector v-else class="param-script"
                  :id="'id'" :text="'name'"
                  :onSelectionChanged="(d) => param.functionId = (d[0] || {}).id"
                  :filterable="true" :remote="true"
                  placeholder="请输选择一个自定义参数，输入名称进行搜索"
                  :load="[param.functionId]"
                  :url="functionListApi"
      ></v-selector>
      <el-input v-if="!param.fix"
                v-model="param.arguments"
                placeholder="函数入参，逗号隔开"
                class="param-arguments"
                type="text">
      </el-input>
      <el-checkbox v-model="param.fix">固定值</el-checkbox>
      <el-button type="primary" icon="el-icon-plus" @click="addParams()" circle size="small"></el-button>
      <el-button type="danger" icon="el-icon-delete" @click="delParam(index)" circle size="small"></el-button>
    </el-form-item>
  </el-form>
</template>

/**
* @author ： 李银 on 2020年07月14日16:54:34
**/
<script>
import { functionListApi } from '@/config/api/inserv-api'

export default {
  name: 'parameter-input',
  props: {
    parameters: {
      type: Array,
      default: () => []
    }
  },
  data () {
    return {
      functionListApi
    }
  },
  mounted () {
  },
  methods: {
    addParams () {
      this.parameters.push({key: '', value: '', type: 'text'})
    },
    delParam (i) {
      this.parameters.splice(i, 1)
    }
  },
  components: {
    'v-jsonformatter': () => import('@/components/jsonformatter'),
    'v-selector': () => import('@/components/selector')
  }
}
</script>

<style lang='scss' scoped>
  @import 'index';
</style>
