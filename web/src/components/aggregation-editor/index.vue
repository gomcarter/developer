<template>
  <div class="agg-editor">
    <el-container style="height: 722px;">
      <el-main class="graph-container" style="width: 50%">
        <div class="flow-container"></div>
      </el-main>
      <el-aside class="data-container" style="width: 49%">
        <div v-if="!disabled" >
          <h4 class="title">参数</h4>
          <hr/>
          <el-form v-if="!disabled" v-for="(list, key) in parametersMap" :key="key">
            <el-form-item label="" v-for="(param, index) in list"  :key="index" v-if="param.type" style="margin-bottom: 22px;">
              <div v-if="param.inputType === 'textarea'" class="param-key v-top el-input text-right">body：</div>
              <el-input v-else placeholder="可手动修改参数名" class="param-key v-top" v-model="param.showKey"></el-input>
              <span class="v-top" v-if="param.inputType !== 'textarea'">=</span>
              <div v-if="param.dependency && param.dependency.open" class="param-value">
                {{ param.dependency.id + '：' + param.dependency.target }}
              </div>
              <el-input v-else :placeholder="(param.comment || '请输入参数值') + (param.notNull ? '，必填项' : '')"
                         class="param-value"
                         :rows="10"
                         :type="param.inputType === 'textarea' ? 'textarea' : 'text'"
                         v-model="param.defaults"
                         :name="param.showKey">
              </el-input>
              <el-button type="primary" icon="el-icon-setting" v-if="size > 1" @click="openDependencyDialog(key, index)" cycle size="small"></el-button>
            </el-form-item>
          </el-form>
          <p>&nbsp;</p>
          <h4 class="title">返回值</h4>
          <hr/>
          <el-form>
            <el-form-item label="返回值：">
              <v-json-editor v-for="(returns, key) in returnsMap" :json="returns" :key="key"></v-json-editor>
            </el-form-item>
          </el-form>
        </div>
      </el-aside>
    </el-container>
    <div class="mask" v-if="disabled" >
      <el-button type="primary" @click="create" icon="el-icon-plus" class="create-button">创建接口聚合</el-button>
    </div>
    <v-dialog ref="dependencyDialog"
              title="设置参数依赖"
              :width="800"
              :ok="saveDependency">
      <div slot="body">
        <el-form slot="body" ref="dependencyForm" :model="dependency" label-width="8em" class="dependency-dialog">
          <el-form-item prop="open" label="是否依赖">
            <el-switch v-model="dependency.open"></el-switch>
          </el-form-item>
          <el-form-item v-if="dependency.open" prop="id" label="依赖接口" required
                        :rules="[{ required: true, message: '请选择一个依赖的接口', trigger: ['blur', 'change'] }]">
            <v-selector :onSelectionChanged="(s) => $set(dependency, 'id', (s[0] || {}).id)"
                        :remote="false" placeholder="请选择一个依赖的接口"
                        :load="dependency.id ? [dependency.id] : null"
                        :data="selectableItems">
            </v-selector>
            <el-input v-model="dependency.id" class="hidden"></el-input>
          </el-form-item>
          <el-form-item v-if="dependency.open && dependency.id" prop="target" label="依赖字段"
                        :rules="[{ required: true, message: '请指定依赖字段', trigger: ['blur', 'change'] }]">
            <v-json-editor :json="returnsMap[dependency.id]"
                           :onSelectionChanged="(s) => $set(dependency, 'target', (s || [])[0])"
                           :editable="false" :selectable="true"
                           :selectedKeys="dependency.target ? [dependency.target] : []"></v-json-editor>
            <el-input v-model="dependency.target" class="hidden"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </v-dialog>
  </div>
</template>

<script>
import { generateParameters, generateReturns } from '@/config/utils'
import { G6 } from '@/config/G6'
import insertCss from 'insert-css'

export default {
  props: {
    workflow: {
      type: Object,
      default: null
    },
    onOpen: {
      type: Function,
      default: () => {}
    }
  },
  data () {
    return {
      dependency: {
        id: null,
        open: false,
        target: null
      },
      selectableItems: [],
      width: null,
      height: 600,
      selectedItem: null,
      graph: null,
      disabled: true,
      endNode: null,
      size: 0,
      parametersMap: {},
      returnsMap: {},
      updater: 1
    }
  },
  computed: {},
  methods: {
    render () {
      insertCss(`
        .g6-minimap-container {
          border: 1px solid #e2e2e2;
        }
        .g6-minimap-viewport {
          border: 2px solid rgb(25, 128, 255);
        }
      `)
      const minimap = new G6.Minimap({ size: [150, 100] })
      this.graph = new G6.Graph({
        container: this.$el.querySelectorAll('.flow-container')[0],
        width: this.width * 0.5,
        height: this.height,
        // 是否开启画布自适应。开启后图自动适配画布大小。
        // fitView: true,
        // fitViewPadding: 100,
        modes: {
          default: ['drag-node', 'drag-canvas', 'click-select'],
          zoom: ['zoom-canvas', 'drag-node', 'drag-canvas', 'click-select']
        },
        nodeStateStyles: {
          pending: {
            radius: 4,
            fill: 'darkgray',
            stroke: '#4063ff'
          },
          selected: {
            radius: 4,
            fill: '#b3eee9',
            stroke: '#4063ff'
          }
        },
        defaultNode: {
          type: 'rect',
          size: [140, 50],
          label: '新增节点',
          anchorPoints: [],
          style: {
            radius: 4,
            fill: '#fff',
            stroke: '#4063ff'
          }
        },
        defaultEdge: {
          type: 'running-line',
          style: {
            stroke: '#4063ff',
            shadowColor: 'black',
            endArrow: {
              // path: 'M 6,0 L -6,-6 L -6,6 Z',
              path: 'M 0,0 L 12,6 L 9,0 L 12,-6 Z',
              fill: '#4063ff'
            },
            lineAppendWidth: 20
          }
        },
        plugins: [ minimap ]
      })

      const data = Object.assign({nodes: []}, this.workflow)
      this.graph.data(data)
      this.graph.render()
      if (this.workflow == null || Object.entries(this.workflow).length === 0) {
        this.graph.getNodes().forEach(n => this.graph.setItemState(n, 'pending', true))
      } else {
        this.disabled = false
        const endNode = this.getEndNode()
        const model = endNode.getModel()
        this.parametersMap = Object.assign({}, model.data.parametersMap)
        this.returns = model.data.returns
      }
    },
    batchDeleteNodes (interfacesIdList) {
      if (this.disabled) {
        return
      }
      this.graph.getNodes().forEach(n => {
        const model = n.getModel()
        if (!model.end) {
          const interfaceId = model.data.interfaceId
          if (interfacesIdList.indexOf(interfaceId) >= 0) {
            delete this.parametersMap[model.id]
            this.graph.removeItem(n)
            this.$set(this, 'parametersMap', this.parametersMap)
          }
        }
      })

      this.size = this.graph.getNodes().length - 1
    },
    batchAddNodes (interfaces) {
      if (this.disabled) {
        return
      }

      const endNode = this.getEndNode()

      const base = this.width / 2 - 200
      const index = this.graph.getNodes().length
      for (let i = index; i < interfaces.length + index; ++i) {
        const face = interfaces[i - index]
        let name = face.name
        if (name.length > 16) {
          name = name.substr(0, 8) + '\r\n' + name.substr(8, 7) + '...'
        } else if (name.length > 8) {
          name = name.substr(0, 8) + '\r\n' + name.substr(8, name.length)
        }

        const id = G6.uuid(this.graph)
        // load
        const parameters = generateParameters(face.parameters)
        const data = {
          interfaceId: face.id,
          name: '（' + id + '）' + face.name,
          hash: face.hash,
          java: face.java,
          url: face.url,
          method: face.method
        }
        const node = this.graph.addItem('node', {
          id: id,
          type: 'rect',
          label: '（' + id + '）' + name,
          x: base + (i % 3) * 190,
          y: (parseInt(i / 3) + 1) * 100,
          data
        })
        this.connect(node, endNode)
        parameters.forEach(s => {
          s.showKey = id + '.' + s.key
          return s
        })

        this.parametersMap[id] = parameters
        this.returnsMap[id] = generateReturns(JSON.parse(face.returns), face.java.wrapper)
      }

      this.size = this.graph.getNodes().length - 1
      G6.beautify(this.graph)
    },
    getEndNode () {
      if (this.endNode) {
        return this.endNode
      } else if ((this.endNode = this.graph.getNodes().filter(n => n.getModel().end)[0]) == null) {
        this.endNode = this.graph.addItem('node', {
          id: G6.uuid(this.graph),
          type: 'rect',
          label: '输出节点',
          x: this.width * 0.5 * 0.5,
          y: this.height * 0.5,
          end: true
        })
      }
      return this.endNode
    },
    connect (source, target) {
      const id = G6.uuid(this.graph)
      this.graph.addItem('edge', {
        id,
        label: id,
        source: source.getModel().id,
        target: target.getModel().id
      })
    },
    disconnect (source, target) {
      const edge = this.graph.getEdges().filter(e => e.getSource() === source && e.getTarget() === target)
      if (edge.length > 0) {
        this.graph.removeItem(edge[0])
      }
    },
    bindEvent () {
      this.graph.on('click', this.graphClicked)

      this.graph.on('keydown', (ev) => {
        if (ev.key === 'Control') {
          this.graph.setMode('zoom')
        }
      })

      this.graph.on('keyup', (ev) => {
        this.graph.setMode('default')
      })
    },
    graphClicked (event) {
      const current = event.item
      if (this.selectedItem !== current) {
        // 移除其他边动画和选中状态
        this.graph.getEdges().forEach(edge => this.graph.clearItemStates(edge, ['running', 'selected']))

        // 遍历所有nodes关闭动画
        this.graph.getNodes().forEach(n => this.graph.clearItemStates(n, ['selected']))
      }

      if (current == null) {
        return
      }

      if (current.getType() === 'node') {
        // 设置当前节点选中。以及相关边动画
        setTimeout(() => {
          this.graph.setItemState(current, 'selected', true)
          current.getEdges().forEach(edge => this.graph.setItemState(edge, 'running', true))
        }, 25)
      } else if (current.getType() === 'edge') {
        // 添加当前边选择状态
        this.graph.setItemState(current, 'selected', true)
      }
      this.selectedItem = current
    },
    // 外部api， 获取数据
    getWorkflow () {
      if (this.disabled || this.graph.getNodes().length === 1) {
        return null
      }

      // 更新输出节点
      const model = this.endNode.getModel()
      model.data = {
        parametersMap: this.parametersMap,
        returnsMap: this.returnsMap
      }
      return G6.getWorkflow(this.graph)
    },
    create () {
      this.disabled = false
      this.onOpen && this.onOpen()
    },
    openDependencyDialog (key, index) {
      this.editingParam = this.parametersMap[key][index]
      this.selectableItems = this.graph.getNodes()
        .filter(s => !s.getModel().end && s.getModel().id !== key)
        .map(s => {
          const model = s.getModel()
          return {
            id: model.id,
            text: model.data.name
          }
        })
      console.log(this.selectableItems)

      this.dependency = Object.assign({}, this.editingParam.dependency || {})
      this.$refs.dependencyDialog.open()
    },
    saveDependency () {
      this.$refs.dependencyForm.validate((valid) => {
        if (valid) {
          if (this.dependency.open) {
            this.editingParam.dependency = this.dependency
          } else {
            this.editingParam.dependency = null
          }

          this.updater = this.updater + 1
          this.$refs.dependencyDialog.close()
        }
      })
    }
  },
  mounted () {
    this.width = this.$el.clientWidth
    // 先注册，再初始化，再绑定事件
    // 初始化边动画
    G6.registerRunningLineEdge()

    // 初始化数据
    this.render()

    // 初始化menu显示
    this.bindEvent()

    window.agg = this
  },
  components: {
    'v-json-editor': () => import('@/components/json-editor'),
    'v-dialog': () => import('@/components/dialog'),
    'v-selector': () => import('@/components/selector'),
    'v-parameter': () => import('@/components/parameter')
  }
}
</script>

<style type="text/css" lang="scss" scoped>
  @import 'index.scss';
</style>
