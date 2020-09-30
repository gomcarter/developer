<template>
  <div>
    <div class="flow-container"></div>
    <ul class="work-flow-context-menu" v-if="contextMenu.event" :style="{left: contextMenu.left, top: contextMenu.top}">
      <li v-for="(menu, index) of contextMenu.menu" :key="index" v-if="contextMenu.event && menu.isShow()" @click="onMenuClicked(menu)">{{menu.text}}</li>
    </ul>
    <v-node-editor ref="nodeEditor" :on-ok="saveNode"></v-node-editor>
    <v-condition-editor ref="conditionEditor" :on-ok="saveNode"></v-condition-editor>
  </div>
</template>

<script>
import { functionListApi, interfacesVersionedSimpleListApi } from '@/config/api/inserv-api'
import { generateParameters } from '@/config/utils'
import { G6 } from '@/config/G6'
import insertCss from 'insert-css'

export default {
  props: {
    dataList: {
      type: Object,
      default: null
    },
    width: {
      type: Number,
      default: 618
    },
    height: {
      type: Number,
      default: 1000
    }
  },
  data () {
    return {
      interfacesVersionedSimpleListApi,
      functionListApi,
      alt: false,
      selectedItem: null,
      graph: null,
      data: null,
      contextMenu: {
        left: -10000,
        top: -10000,
        event: null,
        show (global, event) {
          this.event = event
          this.left = event.canvasX + 2 + 'px'
          this.top = event.canvasY + 2 + 'px'
          // this.left = event.x + 2 + 'px'
          // this.top = event.y + 2 + 'px'
        },
        menu: [{
          text: '新增节点',
          // 空白处右键
          isShow: () => this.contextMenu.event.item == null,
          handler: () => this.addNode(this.contextMenu.event)
        }, {
          text: '新增条件',
          // 空白处右键
          isShow: () => this.contextMenu.event.item == null,
          handler: () => {
            // // 在图上新增一个节点
            const node = this.graph.addItem('node', {
              type: 'diamond',
              size: [120, 50],
              style: {
                fill: '#fff',
                stroke: '#4063ff'
              },
              label: '判断条件',
              x: this.contextMenu.event.x,
              y: this.contextMenu.event.y,
              id: G6.uuid(this.graph)
            })

            this.graph.setItemState(node, 'pending', true)
            this.selectedItem = node
          }
        }, {
          text: '编辑',
          isShow: () => this.contextMenu.event.item != null && this.contextMenu.event.item.getType() === 'node',
          handler: () => this.beginEditNode(this.contextMenu.event)
        }, {
          text: '删除节点',
          isShow: () => this.contextMenu.event.item != null && this.contextMenu.event.item.getType() === 'node',
          handler: (that, item) => {
            if (item.getStates().indexOf('pending') >= 0) {
              // 未编辑状态可直接删除
              this.graph.removeItem(item)
            } else {
              this.$confirm('你即将删除节点，是否继续？', '提示', {type: 'warning'}).then(() => {
                this.graph.removeItem(item)
              })
            }
          }
        }, {
          text: '增加连线',
          isShow: () => this.contextMenu.event.item != null && this.contextMenu.event.item.getType() === 'node',
          handler: () => {
            const ev = this.contextMenu.event
            const node = ev.item
            const id = G6.uuid(this.graph)
            this.edge = this.graph.addItem('edge', {
              id,
              label: id,
              source: node.getModel().id,
              target: { x: ev.x, y: ev.y }
            })
            this.addingEdge = true
          }
        }, {
          text: '删除连线',
          isShow: () => this.contextMenu.event.item != null && this.contextMenu.event.item.getType() === 'edge',
          handler: (that, item) => this.graph.removeItem(item)
        }]
      }
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
      const minimap = new G6.Minimap({
        size: [150, 100]
      })
      this.graph = new G6.Graph({
        container: this.$el.querySelectorAll('.flow-container')[0],
        width: this.width,
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
            fill: 'orange',
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

      const data = Object.assign({nodes: []}, this.dataList)
      this.graph.data(data)
      this.graph.render()

      if (this.dataList == null || Object.entries(this.dataList).length === 0) {
        this.graph.getNodes().forEach(n => this.graph.setItemState(n, 'pending', true))
      }
    },
    onMenuClicked (menu) {
      // 处理
      menu.handler(this, this.contextMenu.event.item)
      // 清空事件，关闭菜单
      this.contextMenu.event = null
    },
    batchAddNodes (interfaces) {
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

        // load
        const data = {
          interfaceId: face.id,
          interfaceName: name,
          history: false,
          hash: face.hash,
          headers: face.end.header ? JSON.parse(face.end.header) : [],
          javascript: null,
          java: face.java,
          url: face.url,
          sleep: null,
          returns: JSON.parse(face.returns),
          method: face.method,
          parameters: generateParameters(face.parameters)
        }

        const id = G6.uuid(this.graph)
        this.graph.addItem('node', {
          id: id,
          type: 'rect',
          label: '（' + id + '）' + name,
          x: base + (i % 3) * 190,
          y: (parseInt(i / 3) + 1) * 100,
          data
        })
      }
    },
    addNode (event) {
      // 在图上新增一个节点
      const node = this.graph.addItem('node', {
        type: 'rect',
        x: event.x,
        y: event.y,
        id: G6.uuid(this.graph)
      })

      this.graph.setItemState(node, 'pending', true)
      this.selectedItem = node
    },
    bindEvent () {
      // 右键菜单
      this.graph.on('contextmenu', (event) => {
        event.preventDefault()
        event.stopPropagation()

        // 如果有进行中的连线，删除
        if (this.addingEdge && this.edge) {
          this.graph.removeItem(this.edge)
          this.edge = null
          this.addingEdge = false
        } else {
          // 显示菜单
          this.contextMenu.show(this, event)
          // 单击事件
          this.graphClicked(event)
        }
      })

      this.graph.on('click', (event) => {
        // 关闭菜单
        this.contextMenu.event = null
        // 单击事件
        this.graphClicked(event)
      })

      this.graph.on('dblclick', (event) => {
        event.preventDefault()
        event.stopPropagation()
        if (event.item == null) {
          this.addNode(event)
        } else if (event.item.getType() === 'node') {
          // 双击node，进行编辑node
          this.beginEditNode(event)
        }
      })

      this.graph.on('mousemove', (ev) => {
        // 处理连线
        if (this.addingEdge && this.edge) {
          const target = { x: ev.x, y: ev.y }
          this.graph.updateItem(this.edge, { target })
        }
      })

      this.graph.on('keydown', (ev) => {
        if (ev.key === 'Control') {
          this.graph.setMode('zoom')
        } else if (ev.key === 'Alt') {
          this.alt = true
          console.log('alt', this.alt)
        }
      })
      this.graph.on('keyup', (ev) => {
        this.graph.setMode('default')
        if (ev.key === 'Delete') {
          if (this.selectedItem) {
            // 删除选中物体
            let index = -1
            if (this.selectedItem.getType() === 'node') {
              index = 3
            } else if (this.selectedItem.getType() === 'edge') {
              index = 5
            }
            if (index >= 0) {
              this.contextMenu.menu[index].handler(this, this.selectedItem)
            }
          }
        } else if (ev.key === 'Alt') {
          this.alt = false
          console.log('alt', this.alt)
        }
      })
    },
    graphClicked (event) {
      if (event.item == null) {
        // 点击空白处，移除边动画
        this.graph.getEdges().forEach(edge => {
          this.graph.setItemState(edge, 'running', false)
          this.graph.setItemState(edge, 'selected', false)
        })
      } else if (event.item.getType() === 'node') {
        // 点击节点
        this.nodeClicked(event)
      } else if (event.item.getType() === 'edge') {
        // 点击边
        this.edgeClicked(event)
      }
      this.selectedItem = event.item
    },
    edgeClicked (ev) {
      const currentEdge = ev.item
      // 处理连线，拖拽过程中，点击会点击到新增的边上
      if (this.addingEdge && this.edge === currentEdge) {
        this.graph.removeItem(this.edge)
        this.edge = null
        this.addingEdge = false
      } else {
        // 移除其他边动画和选中状态
        this.graph.getEdges().forEach(edge => {
          this.graph.setItemState(edge, 'running', false)
          this.graph.setItemState(edge, 'selected', false)
        })
        // 添加当前边选择状态
        this.graph.setItemState(ev.item, 'selected', true)
      }
    },
    nodeClicked (ev) {
      // 处理节点间的连线
      const node = ev.item
      if (this.addingEdge && this.edge) {
        const model = node.getModel()
        // 看两个节点之间是否已经存在连线: 此线的起点和已经存在的线的起点相同，此线起点和已经存在的线的终点相同
        const matched = (node.getEdges().filter(s => s.getSource() === this.edge.getSource() || s.getTarget() === this.edge.getSource()) || []).length > 0
        if (matched) {
          // 已经存在连线则此连线作废
          this.graph.removeItem(this.edge)
          this.edge = null
          this.addingEdge = false
        } else {
          // 不存在则加上
          this.graph.updateItem(this.edge, { target: model.id })
          this.edge = null
          this.addingEdge = false
        }
      } else if (this.alt) {
        // 按下了alt键，那么点击item则增加连线
        const id = G6.uuid(this.graph)
        this.edge = this.graph.addItem('edge', {
          id,
          label: id,
          source: node.getModel().id,
          target: { x: ev.x, y: ev.y }
        })
        this.addingEdge = true
      }

      // 遍历所有edges关闭动画
      this.graph.getEdges().forEach(edge => {
        this.graph.setItemState(edge, 'running', false)
        this.graph.setItemState(edge, 'selected', false)
      })

      // 遍历所有nodes关闭动画
      this.graph.getNodes().forEach(n => this.graph.setItemState(n, 'selected', false))

      // 设置当前节点选中。以及相关边动画
      setTimeout(() => {
        this.graph.setItemState(node, 'selected', true)
        node.getEdges().forEach(edge => this.graph.setItemState(edge, 'running', true))
      }, 25)
    },
    beginEditNode (event) {
      this.selectedItem = null
      this.editingNode = event.item
      const model = this.editingNode.getModel()
      const edges = this.editingNode.getEdges()
      if (model.type === 'rect') {
        this.$refs.nodeEditor.open(model, edges)
      } else {
        this.$refs.conditionEditor.open(model, edges)
      }
      document.getElementsByTagName('input')[0].focus()
    },
    saveNode (node) {
      const model = this.editingNode.getModel()
      let name
      if (model.type === 'rect') {
        name = node.interfaceName || ''
      } else {
        name = node.javascript
      }

      // 字数太多换行
      if (name.length > 16) {
        name = name.substr(0, 8) + '\r\n' + name.substr(8, 7) + '...'
      } else if (name.length > 8) {
        name = name.substr(0, 8) + '\r\n' + name.substr(8, name.length)
      }

      model.data = node
      model.label = '（' + model.id + '）' + name
      // 更新节点，以及清除节点pending状态
      this.graph.updateItem(this.editingNode, model)
      this.graph.setItemState(this.editingNode, 'pending', false)
      this.editingNode = null
    },
    isAllConnected (nodes, edges) {
      return true
    },
    // 外部api， 验证填写是否正确
    validate () {
      let result = false
      let message
      const nodes = this.graph.getNodes()
      const edges = this.graph.getEdges()
      if (!nodes || nodes.length <= 0) {
        message = '未配置任何节点'
      } else if (nodes.filter(s => s.getStates().indexOf('pending') >= 0).length > 0) {
        message = '存在未配置完成的节点'
      } else if (!this.isAllConnected(nodes, edges)) {
        message = '流程配置有误，所有节点都应该被串联起来'
      } else {
        // 条件节点必须至少有一个口节点
        // nodes.filter(n => n.getModel().type === 'diamond' && n.getEdges().filter(e => e.getTarget()))
        result = true
      }

      return { result, message }
    },
    // 外部api， 获取数据
    workflow () {
      return G6.getWorkflow(this.graph)
    },
    beautify () {
      G6.beautify(this.graph)
      // 如果有未编辑完的节点，标记为pending
      this.graph.getNodes().forEach(n => {
        const model = n.getModel()
        if (!model || !model.data || !model.data.interfaceId) {
          this.graph.setItemState(n, 'pending', true)
        }
      })
    }
  },
  mounted () {
    // 先注册，再初始化，再绑定事件
    // 初始化边动画
    G6.registerRunningLineEdge()

    // 初始化数据
    this.render()

    // 初始化menu显示
    this.bindEvent()

    window.that = this
  },
  components: {
    'v-node-editor': () => import('@/components/node-editor'),
    'v-condition-editor': () => import('@/components/condition-editor'),
    'v-dialog': () => import('@/components/dialog'),
    'v-selector': () => import('@/components/selector')
  }
}
</script>

<style type="text/css" lang="scss" scoped>
  @import 'index.scss';
</style>
