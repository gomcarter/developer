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
import G6 from '@antv/g6'
import { functionListApi, interfacesSimpleListApi, interfacesVersionedSimpleListApi } from '@/config/api/inserv-api'

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
      interfacesSimpleListApi,
      functionListApi,
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
              shape: 'diamond',
              size: [120, 50],
              style: {
                fill: '#fff',
                stroke: '#4063ff'
              },
              label: '判断条件',
              x: this.contextMenu.event.x,
              y: this.contextMenu.event.y,
              id: G6.Util.uniqueId()
            })

            this.graph.setItemState(node, 'pending', true)
          }
        }, {
          text: '编辑',
          isShow: () => this.contextMenu.event.item != null && this.contextMenu.event.item.getType() === 'node',
          handler: () => this.beginEditNode(this.contextMenu.event)
        }, {
          text: '删除节点',
          isShow: () => this.contextMenu.event.item != null && this.contextMenu.event.item.getType() === 'node',
          handler: () => {
            const item = this.contextMenu.event.item
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
            const id = G6.Util.uniqueId()
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
          handler: () => this.graph.removeItem(this.contextMenu.event.item)
        }]
      }
    }
  },
  computed: {},
  methods: {
    render () {
      this.graph = new G6.Graph({
        container: this.$el.querySelectorAll('.flow-container')[0],
        width: this.width,
        height: this.height,
        // 是否开启画布自适应。开启后图自动适配画布大小。
        fitView: true,
        fitViewPadding: 100,
        modes: {
          default: ['drag-node', 'drag-canvas', 'zoom-canvas', 'click-select']
        },
        nodeStateStyles: {
          pending: {
            radius: 4,
            fill: 'orange',
            stroke: '#4063ff'
          }
        },
        defaultNode: {
          shape: 'rect',
          size: [120, 50],
          label: '新增节点',
          style: {
            radius: 4,
            fill: '#fff',
            stroke: '#4063ff'
          }
        },
        defaultEdge: {
          shape: 'running-line',
          style: {
            stroke: '#4063ff',
            shadowColor: 'black',
            endArrow: {
              path: 'M 6,0 L -6,-6 L -6,6 Z',
              d: 6
            },
            lineAppendWidth: 20
          }
        }
      })

      const data = Object.assign({nodes: [{ id: 'main', shape: 'rect', label: '主节点', x: this.width / 2, y: 50 }]}, this.dataList)
      this.graph.data(data)
      this.graph.render()

      if (!this.dataList) {
        this.graph.getNodes().forEach(n => this.graph.setItemState(n, 'pending', true))
      }
    },
    onMenuClicked (menu) {
      // 处理
      menu.handler(this)
      // 清空事件，关闭菜单
      this.contextMenu.event = null
    },
    addNode (event) {
      // 在图上新增一个节点
      const node = this.graph.addItem('node', {
        shape: 'rect',
        x: event.x,
        y: event.y,
        id: G6.Util.uniqueId()
      })

      this.graph.setItemState(node, 'pending', true)
    },
    bindEvent () {
      // 右键菜单
      this.graph.on('contextmenu', (event) => {
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
        // console.log(node.getEdges().length, this.graph.getEdges().length, this.graph.getEdges())
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
      this.editingNode = event.item
      const model = this.editingNode.getModel()
      const edges = this.editingNode.getEdges()
      if (model.shape === 'rect') {
        this.$refs.nodeEditor.open(model, edges)
      } else {
        this.$refs.conditionEditor.open(model, edges)
      }
    },
    saveNode (node) {
      const model = this.editingNode.getModel()
      let name
      if (model.shape === 'rect') {
        // 字数太多换行
        name = node.interfaceName || ''
      } else {
        name = node.javascript
      }

      if (name.length > 16) {
        name = name.substr(0, 8) + '\r\n' + name.substr(8, 7) + '...'
      } else if (name.length > 8) {
        name = name.substr(0, 8) + '\r\n' + name.substr(8, name.length)
      }

      model.data = node
      model.label = name
      // 更新节点，以及清除节点pending状态
      this.graph.updateItem(this.editingNode, model)
      this.graph.setItemState(this.editingNode, 'pending', false)
      this.editingNode = null
    },
    registerEdge () {
      // lineDash 的差值，可以在后面提供 util 方法自动计算
      const dashArray = [
        [ 0, 1 ],
        [ 0, 2 ],
        [ 1, 2 ],
        [ 0, 1, 1, 2 ],
        [ 0, 2, 1, 2 ],
        [ 1, 2, 1, 2 ],
        [ 2, 2, 1, 2 ],
        [ 3, 2, 1, 2 ],
        [ 4, 2, 1, 2 ]
      ]
      const lineDash = [ 4, 2, 1, 2 ]
      const interval = 9
      G6.registerEdge('running-line', {
        setState (name, value, item) {
          const shape = item.get('keyShape')
          if (name === 'running') {
            if (value) {
              // 后续 G 增加 totalLength 的接口
              const length = shape.getTotalLength()
              let totalArray = []
              for (let i = 0; i < length; i += interval) {
                totalArray = totalArray.concat(lineDash)
              }
              let index = 0
              shape.animate({
                onFrame () {
                  const cfg = {
                    lineDash: dashArray[index].concat(totalArray)
                  }
                  index = (index + 1) % interval
                  return cfg
                },
                repeat: true
              }, 1)
            } else {
              shape.stopAnimate()
              shape.attr('lineDash', null)
            }
          } else if (name === 'selected') {
            if (value) {
              shape.attr('shadowBlur', 5)
            } else {
              shape.attr('shadowBlur', 0)
            }
          }
        }
      }, 'line')
    },
    isAllConnected () {
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
        result = true
      }

      return { result, message }
    },
    // 外部api， 获取数据
    workflow () {
      const nodes = this.graph.getNodes().map(s => s.getModel())
      const edges = this.graph.getEdges().map(s => s.getModel())
      return { nodes, edges }
    }
  },
  mounted () {
    window.that = this
    // 先注册，再初始化，再绑定事件
    // 初始化边动画
    this.registerEdge()

    // 初始化数据
    this.render()

    // 初始化menu显示
    this.bindEvent()
  },
  components: {
    'v-node-editor': () => import('@/components/node-editor'),
    'v-condition-editor': () => import('@/components/condition-editor'),
    'v-dialog': () => import('@/components/dialog'),
    'v-selector': () => import('@/components/selector')
  }
}
</script>

<style type="text/css" lang="scss">
  @import 'index.scss';
</style>
