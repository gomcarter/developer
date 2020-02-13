<template>
  <div>
    <el-form label-width="3em">
      <el-form-item label="环境：" style="width: 250px">
        <el-select v-model="env">
          <el-option label="开发" value="devDomain"></el-option>
          <el-option label="测试" value="testDomain"></el-option>
          <el-option label="预发" value="prevDomain"></el-option>
          <el-option label="线上" value="onlineDomain"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div>
      <span class="simple waiting"></span><span>-等待执行</span>&#12288;
      <span class="simple running"></span><span>-执行中</span>&#12288;
      <span class="simple success"></span><span>-执行成功</span>&#12288;
      <span class="simple failed"></span><span>-执行失败</span>
      <span class="simple ignore"></span><span>-不执行</span>
    </div>
    <el-container>
      <el-main class="container" :style="{width: width * 0.6 + 'px', height: height + 'px'}">
        <div class="flow-container"></div>
      </el-main>
      <el-aside :style="{width: width * 0.4 + 'px', height: height + 'px'}" class="runner-log">
        <div v-for="(log, index) of logs" :key="index" style="min-height: 20px;">
          <span v-if="log.type === 'json'">
            <v-jsonformatter :json="log.data" :min-height="25"></v-jsonformatter>
          </span>
          <span v-else-if="log.type === 'error'" style="color: red;">{{ log.data }}</span>
          <span v-else>{{ log.data }}</span>
        </div>
      </el-aside>
    </el-container>
  </div>
</template>

<script>
import G6 from '@antv/g6'
import { xhr } from '@/config/api/http'
import { functionListApi } from '@/config/api/inserv-api'
import { toQueryString } from '@/config/utils'

export default {
  props: {
    width: {
      type: Number,
      default: 1000
    },
    height: {
      type: Number,
      default: 618
    }
  },
  data () {
    return {
      logs: [],
      env: 'testDomain',
      workflow: {
        nodes: null,
        edges: null
      },
      presetParams: null,
      model: []
    }
  },
  methods: {
    setData (data) {
      this.log('========开始========')
      this.workflow = data.workflow
      this.presetParams = data.presetParams || []

      // render
      this.graph.data(this.workflow)
      this.graph.render()

      this.constructExecutableDataModel()

      // 初始化预制参数
      this.initPresetParams()
    },
    /**
     * 对节点进行编排，编排成这样 [[node1,node2 ...], [node3,node4...]] 将node分层，方便依次执行这些节点。
     */
    constructExecutableDataModel () {
      this.log('初始化运行流程：')
      this.model = []
      // 遍历节点找到最顶端的节点设置为level 1， 往下一层level 2， 以此类推，把所有节点分层
      // 然后最后执行时就从第一层开始执行，到最后一层。
      const nodes = this.graph.getNodes()
      nodes.forEach(n => { n.getModel().mark = 0 })
      const totalCounts = nodes.length

      // 第一层怎么找: 存在任何一个节点只有出没有入的节点
      let currentNodes = nodes.filter(n => n.getEdges().filter(e => e.getSource() !== n).length === 0)
      let count = currentNodes.length
      if (count === 0) {
        // 如果没有找到首节点，证明产生了回环，那么随便选一个节点作为 currentNodes
        currentNodes = [nodes[0]]
        count++
      }
      this.model.push(currentNodes)
      // 标记节点已经被修改
      currentNodes.forEach(n => { n.getModel().mark = n.getModel().mark + 1 })
      // currentNodes.forEach(n => console.log(this.model.length + '：', n.getModel().id, n.getModel().label))

      // 第二层开始：入口是上一层的下层节点，而且（如果一个节点存在多个入口，如又是第二层又是第三层，那么要取第三层）
      while (count < totalCounts) {
        currentNodes = this.graph.getEdges()
          .filter(e => {
            const isPreLevelChildren = currentNodes.indexOf(e.getSource()) >= 0
            if (isPreLevelChildren) {
              const target = e.getTarget()
              const targetIsMe = target.getEdges().filter(n => n.getTarget() === target)
              target.getModel().mark = target.getModel().mark + 1
              if (targetIsMe.length === target.getModel().mark) {
                return true
              }
            }
            return false
          })
          .map(e => e.getTarget())

        if (currentNodes.length === 0) {
          // 没有找到则从剩下的node中随便取一个
          currentNodes = [nodes.filter(n => n.getModel().mark === false)[0]]
          count++
        } else {
          count += currentNodes.length
        }

        // 表示已处理
        this.model.push(currentNodes)
      }

      // logs
      let index = 1
      this.model.forEach(m => {
        this.log(index + ' => ' + m.map(mm => `${mm.getModel().label}（$${mm.getModel().id}）`).join('，'))
        index++
      })
      this.log('')
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
    initGraph () {
      // 创建graph
      this.graph = new G6.Graph({
        container: this.$el.querySelectorAll('.flow-container')[0],
        width: this.width * 0.6,
        height: this.height,
        // 是否开启画布自适应。开启后图自动适配画布大小。
        // fitView: true,
        // fitViewPadding: 100,
        modes: {
          default: ['drag-node', 'drag-canvas', 'zoom-canvas']
        },
        nodeStateStyles: {
          waiting: {
            radius: 4,
            fill: 'yellow',
            stroke: '#4063ff'
          },
          running: {
            radius: 4,
            fill: 'yellowgreen',
            stroke: '#4063ff'
          },
          success: {
            radius: 4,
            fill: 'green',
            stroke: '#4063ff'
          },
          failed: {
            radius: 4,
            fill: 'red',
            stroke: '#4063ff'
          },
          ignore: {
            radius: 4,
            fill: 'darkgray',
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
    },
    initPresetParams () {
      // 1,拿出固定值
      this.log('初始化预置参数：')
      this.presetParams.filter(s => s.fix)
        .forEach(s => {
          window['$' + s.key] = s.value
          this.log('$' + s.key + ' => ' + s.value)
        })

      // 2，拿出非固定值，请求获取到function，再通过function获取到最新的值
      const functionPresetParams = this.presetParams.filter(s => !s.fix)
      const functionIdList = functionPresetParams.map(s => s.functionId)
      functionListApi({idList: functionIdList})
        .then(r => {
          const map = {}
          r.forEach(c => {
            /* eslint-disable */
            map[c.id] = Function(c.script)()
          })

          functionPresetParams.forEach(s => {
            window['$' + s.key] = map[s.functionId]
            this.log('$' + s.key + ' => ' + window['$' + s.key])
          })
          this.log('')
        })
    },
    clearState (node) {
      node = node == null ? this.graph.getNodes() : (node instanceof Array ? node : [node])

      node.forEach(n => {
        this.graph.setItemState(n, 'waiting', false)
        this.graph.setItemState(n, 'running', false)
        this.graph.setItemState(n, 'success', false)
        this.graph.setItemState(n, 'failed', false)
        this.graph.setItemState(n, 'selected', false)
      })
    },
    setState (node, state, bool = true) {
      node = node == null ? this.graph.getNodes() : (node instanceof Array ? node : [node])
      state = state instanceof Array ? state : [state]
      node.forEach(n => state.forEach(s => this.graph.setItemState(n, s, bool)))
    },
    run () {
      this.log('开始执行：')
      this.clearState()
      this.graph.getNodes().forEach(n => {
        n.getModel().data.shouldRun = true
      })

      this.setState(null, 'waiting')

      this.runLevel(0)
    },
    runLevel (level) {
      if (level >= this.model.length) {
        this.log('执行完毕！')
        this.log('')
        return
      }

      const toBeRunning = this.model[level]
      const totalCount = toBeRunning.length
      let count = 0
      let error = false

      toBeRunning.forEach(node => {
        const model = node.getModel()
        const data = model.data

        // 如果被标记不执行则跳过下游线路
        if (!data.shouldRun) {
          this.log(`节点【${model.id}】不执行`)
          this.setState(node, ['ignore'])
          count++
          return
        }

        // 上游线
        const preEdges = node.getEdges().filter(e => e.getTarget() === node)
        if (level !== 0 && preEdges.filter(e => e.getSource().getModel().data.shouldRun).length === 0) {
          this.log(`上游节点未存在执行节点，本节点【${model.id}】不执行`)
          this.setState(node, ['ignore'])
          // 上游没有一个可执行的，那么本节点也不执行
          count++
          return
        }

        // 下游节点和线
        const edges = node.getEdges().filter(e => e.getSource() === node)
        this.setState(edges.concat(node), ['selected', 'running'])

        // 条件
        if (model.shape === 'diamond') {
          // 执行脚本
          // 有脚本，则按照脚本重新赋值
          try {
            this.log('条件判断结果：')
            const condition = new Function(data.javascript)() || []
            this.log('执行路线：' + condition.join(','))
            if (condition.length > 0) {
              // 找出下游不在condition中的线路， 然后把这些线路的终点节点标记不执行
              edges.filter(e => !(condition.indexOf(e.getModel().id) >= 0)).forEach(e => {
                  const m = e.getTarget().getModel()
                  if (!m.data) {
                    m.data = { shouldRun : false }
                  } else {
                    m.data.shouldRun = false
                  }
                  this.setState([e], ['selected', 'running'], false)
                })

              // // 入线
              // const entry = node.getEdges().filter(e => e.getTarget() === node)
              // edges.filter(e => condition.indexOf(e.getTarget().getModel().id) >= 0)
              //   .forEach(e => {
              //     window['$' + e.getModel().id] = window['$' + entry[0].getModel().id]
              //   })
            }
            this.setState(edges.concat(node), ['selected', 'running'], false)
            this.setState(node, ['success'], true)
            this.log('')
            count++

          } catch (e) {
            this.log(`条件脚本处理出错：${e.message}`, 'error')
            this.log('')

            this.setState(edges.concat(node), ['selected', 'running'], false)
            this.setState(node, ['failed'], true)
            error = true
          }
        } else {
          this.log(`开始执行节点：${model.label}（$${model.id}）`)
          this.buildXhr(data)
            .then((d) => {
              if (d.data.success) {
                // 赋值
                window['$' + model.id] = d.data.extra
                this.log(`返回结果：`)
                this.log(window['$' + model.id], 'json')
                if (data.javascript) {
                  // 有脚本，则按照脚本重新赋值
                  try {
                    const mapped = new Function(data.javascript);
                    if (mapped) {
                      window['$' + model.id] = mapped
                      this.log('脚本处理后本节点结果：')
                      this.log(window['$' + model.id], 'json')
                    } else {
                      this.log('脚本未对结果做任何处理，本节点结果为')
                      this.log(window['$' + model.id], 'json')
                    }
                  } catch (e) {
                    this.log(`数据转换脚本处理出错：${e.message}`, 'error')
                    this.log('')

                    this.setState(edges.concat(node), ['selected', 'running'], false)
                    this.setState(node, ['failed'], true)
                    error = true
                  }
                }

                if (!error) {
                  // 将结果放到输送线上去
                  edges.forEach(e => {
                    window['$' + e.getModel().id] = window['$' + model.id]
                  })

                  this.setState(edges.concat(node), ['selected', 'running'], false)
                  this.setState(node, ['success'], true)
                  this.log('')
                  count++
                }
              } else {
                this.log('执行结果出错：')
                this.log(d.data, 'json')
                this.log('')

                this.setState(edges.concat(node), ['selected', 'running'], false)
                this.setState(node, ['failed'], true)
                error = true
              }
            })
            .catch((e) => {
              this.log('执行出错了: ' + e.toString())
              this.log('')
              this.setState(edges.concat(node), ['selected', 'running'], false)
              this.setState(node, ['failed'], true)
              error = true
            })
        }
      })

      // 等待所有上述执行完毕，进行下次操作
      const timer = setInterval(() => {
        if (error) {
          clearInterval(timer)
          this.log("支持完毕，执行过程中发生了错误！")
          this.log('')
        }

        if (totalCount <= count) {
          count = 0
          clearInterval(timer)
          // 执行下一层
          this.runLevel(level + 1)
        }
      }, 100)
    },
    buildXhr (model) {
      let params
      let method = model.method.toLowerCase()
      let res
      let obj = {}
      let url = model.java[this.env] + model.url
      let bodyParams
      if (model.parameters.length > 0) {
        model.parameters.forEach(p => {
          let value = p['defaults']
          if ((value + '').indexOf('$') >= 0) {
            try {
              value = new Function('return ' + p['defaults'])()
            } catch (e) {
              this.log(`参数处理失败: ${e.message}`, 'error')
              this.log('')
            }
          }

          if (p.inputType === 'textarea') {
            bodyParams = value
          } else if (url.indexOf(`{${p['key']}}`) >= 0) {
            url = url.replace(`{${p['key']}}`, value)
          } else {
            // 同一key的时候，需要合并
            if (obj[p['key']] === undefined) {
              obj[p['key']] = value
            } else {
              obj[p['key']] = [value].concat(obj[p['key']])
            }
          }
        })
      }

      if (['put', 'post', 'patch'].indexOf(method) >= 0) {
        params = obj
      } else { // get and delete
        params = {
          params: obj
        }
      }

      const headers = model.headers.map(h => {
        let value = h.value
        try {
          if (h.value.indexOf('$') === 0) {
            value = new Function('return ' + h.value)()
          }
        } catch (e) {
          this.log(`header处理失败: ${e.message}`, 'error')
          this.log('')
          value = h.value
        }
        return {key: h.key, value: value}
      })

      if (bodyParams) {
        let body
        let type
        try {
          body = JSON.stringify(JSON.parse(bodyParams))
          type = 'json'
        } catch (e) {
          body = bodyParams
          type = 'raw'
        }
        delete obj[bodyParams]

        const u = url + (url.indexOf('?') >= 0 ? '&' : '?') + toQueryString(obj)
        this.log(`${method.toUpperCase()} ${url}`)
        headers.length && this.log(`header：${headers.map(h => h.key + '=' + h.value).join('，')}`)
        this.log(`body：${body}`)
        res = xhr[method](u, body, {notice: false, type: type, customHeaders: headers})
      } else if (method === 'post') {
        this.log(`${method.toUpperCase()} ${url}`)
        headers.length && this.log(`header：${headers.map(h => h.key + '=' + h.value).join('，')}`)
        this.log(`参数：${JSON.stringify(obj)}`)
        res = xhr[method](url, params, {notice: false, customHeaders: headers})
      } else {
        this.log(`${method.toUpperCase()} ${url}`)
        headers.length && this.log(`header：${headers.map(h => h.key + '=' + h.value).join('，')}`)
        this.log(`参数：${JSON.stringify(obj)}`)
        params.customHeaders = headers
        res = xhr[method](url, params, {notice: false})
      }
      return res
    },
    log (log, type = 'text') {
      this.logs.push({data: log, type: type})
      // 滚动条滚到最下面
      setTimeout(() => {
        const container = this.$el.querySelector(".runner-log")
        container.scrollTop = container.scrollHeight
      }, 1)
    }
  },
  mounted () {
    // registerEdge
    this.registerEdge()

    // initGraph
    this.initGraph()

    window.that = this
  },
  components: {
    'v-jsonformatter': () => import('@/components/jsonformatter')
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style type="text/css" lang="scss" scoped>
  @import 'index';
</style>
