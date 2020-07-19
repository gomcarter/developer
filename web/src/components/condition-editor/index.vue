<template>
  <div>
    <v-dialog ref="editConditionDialog" :title="'配置条件：' + nodeId" :ok="confirm" :width="1000">
      <el-form slot="body" ref="form" :model="node" label-width="8em" class="edit-node-dialog">
        <el-form-item label="休眠" >
          <el-input v-model="node.sleep" style="display: inline-block; width: 100px" placeholder="0"></el-input>&#12288;秒之后运行
        </el-form-item>
        <el-form-item label="条件处理脚本:">
          <el-input :placeholder="`${hasSource?'您可以使用（'+ source.map(s => '$' + s).join('，') + '）来获取上游返回值，':''}
${hasTarget? '并选择[' + target.map(t => ' \'' + t + '\' ').join(',') + ']中任意一个或者多个作为返回值，表示下游执行哪条线路' : ''}
如不做任何处理，下游每条分支都将被运行，没有下游则说明都不执行，举例：

if (xx === yy) {
    return [${hasTarget ? ' \'' + target[0] + '\' ' : ''}]
} else {
    return [ ${target.map(t => ' \'' + t + '\' ').join(',')} ]
}`" :rows="15" type="textarea" v-model="node.javascript">
          </el-input>
        </el-form-item>
      </el-form>
    </v-dialog>
  </div>
</template>

<script>
export default {
  props: {
    onOk: {
      type: Function,
      default: () => {}
    }
  },
  data () {
    return {
      nodeId: null,
      source: [],
      target: [],
      node: {
        sleep: 0,
        javascript: null
      },
      hasTarget: false,
      hasSource: false
    }
  },
  methods: {
    open (model, edges) {
      this.target = edges.filter(e => e.getModel().source === model.id)
        .map(s => s.getModel().id) || []
      this.source = edges.filter(e => e.getModel().target === model.id)
        .map(s => s.getModel().id) || []
      this.hasTarget = this.target.length > 0
      this.hasSource = this.source.length > 0

      const data = model.data || {}
      this.nodeId = `（上游节点：${this.source}；下游节点：${this.target}）`
      // load
      this.node.javascript = data.javascript
      this.node.sleep = data.sleep

      this.$refs.editConditionDialog.open()
    },
    confirm () {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.onOk && this.onOk(Object.assign({}, this.node))
          this.$refs.editConditionDialog.close()
        }
      })
    }
  },
  components: {
    'v-dialog': () => import('@/components/dialog')
  }
}
</script>

<style type="text/css" lang="scss" scoped>
  @import 'index';
</style>
