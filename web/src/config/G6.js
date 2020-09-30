/* eslint-disable */
import g6 from '@antv/g6'

g6.registerRunningLineEdge = () => {
  // lineDash 的差值，可以在后面提供 util 方法自动计算
  const dashArray = [
    [0, 1],
    [0, 2],
    [1, 2],
    [0, 1, 1, 2],
    [0, 2, 1, 2],
    [1, 2, 1, 2],
    [2, 2, 1, 2],
    [3, 2, 1, 2],
    [4, 2, 1, 2]
  ]
  const lineDash = [4, 2, 1, 2]
  const interval = 9
  G6.registerEdge('running-line', {
    setState(name, value, item) {
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
            onFrame() {
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
}

g6.getWorkflow = (graph) => {
  const nodes = graph.getNodes().map(s => {
    const model = s.getModel()
    delete model['description']
    return model
  })
  const edges = graph.getEdges().filter(s => s.getTarget().getModel).map(s => s.getModel())
  return { nodes, edges }
}

/**
 * 对节点进行编排，编排成这样 [[node1,node2 ...], [node3,node4...]] 将node分层，方便依次执行这些节点。
 */
g6.constructExecutableDataModel = (graph) => {
  const model = []
  // 遍历节点找到最顶端的节点设置为level 1， 往下一层level 2， 以此类推，把所有节点分层
  // 然后最后执行时就从第一层开始执行，到最后一层。
  const nodes = graph.getNodes()
  nodes.forEach(n => {
    n.getModel()._mark = 0
  })
  const totalCounts = nodes.length

  // 第一层怎么找: 存在任何一个节点只有出没有入的节点
  let currentNodes = nodes.filter(n => n.getEdges().filter(e => e.getSource() !== n).length === 0)
  let count = currentNodes.length
  if (count === 0) {
    // 如果没有找到首节点，证明产生了回环，那么随便选一个节点作为 currentNodes
    currentNodes = [nodes[0]]
    count++
  }
  model.push(currentNodes)
  // 标记节点已经被修改
  currentNodes.forEach(n => {
    n.getModel()._mark = n.getModel()._mark + 1
  })
  // currentNodes.forEach(n => console.log(this.model.length + '：', n.getModel().id, n.getModel().label))

  // 第二层开始：入口是上一层的下层节点，而且（如果一个节点存在多个入口，如又是第二层又是第三层，那么要取第三层）
  while (count < totalCounts) {
    currentNodes = graph.getEdges()
      .filter(e => {
        const isPreLevelChildren = currentNodes.indexOf(e.getSource()) >= 0
        if (isPreLevelChildren) {
          const target = e.getTarget()
          const targetIsMe = target.getEdges().filter(n => n.getTarget() === target)
          target.getModel()._mark = target.getModel()._mark + 1
          if (targetIsMe.length === target.getModel()._mark) {
            return true
          }
        }
        return false
      })
      .map(e => e.getTarget())

    if (currentNodes.length === 0) {
      // 没有找到则从剩下的node中随便取一个
      currentNodes = [nodes.filter(n => n.getModel()._mark === false)[0]]
      count++
    } else {
      count += currentNodes.length
    }

    // 表示已处理
    model.push(currentNodes)
  }

  return model
}

g6.beautify = (graph) => {
  const model = g6.constructExecutableDataModel(graph)
  const base = 190
  const workflow = g6.getWorkflow(graph)
  const nodes = workflow.nodes
  const map = {}
  nodes.forEach(s => map[s.id] = s)

  for (let i = 0; i < model.length; i++) {
    const level = model[i]
    const deltaX = (graph.cfg.width - base * 2) / level.length
    for (let j = 0; j < level.length; j++) {
      const item = level[j]
      const model = map[item.getModel().id]
      model.x = base + deltaX / 2 + deltaX * j - 60
      model.y = 100 + 150 * i
    }
  }

  graph.data(workflow)
  graph.render()
}

g6.uuid = (graph) => {
  const nodes = graph.getNodes() || []
  if (nodes.length === 0) {
    return 'g0'
  } else {
    const sorted = nodes.map(n => n.getModel().id)
      .filter(s => s !== 'main')
      .map(s => parseInt(s.replace('g', '')))
      .concat((graph.getEdges() || []).map(n => n.getModel().id).map(s => parseInt(s.replace('g', ''))))
      .sort((a, b) => a - b)
    const maxId = sorted[sorted.length - 1] || 0
    return 'g' + (+maxId + 1)
  }
}

export const G6 = g6
