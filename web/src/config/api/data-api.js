
const tableMap = {
  1: {
    name: '订单主表',
    schema: 'order'
  },
  2: {
    name: '订单商品主表',
    schema: 'order'
  },
  3: {
    name: '商品主表',
    schema: 'order'
  },
  4: {
    name: '优惠券主表',
    schema: 'order'
  }
}

export const tableListApi = (params) => {
  return Promise.resolve(Object.entries(tableMap)
    .map(s => {
      return {
        id: +s[0],
        name: s[1].name
      }
    })
  )
}

export const tableInfoApi = (tableId) => {
  return Promise.resolve({
    columns: [{
      name: '字段' + 1,
      column: 'column' + 1,
      type: '类型',
      comment: '备注' + Date.now()
    }, {
      name: '字段' + 2,
      column: 'column' + 2,
      type: '类型',
      comment: '备注' + Date.now()
    }, {
      name: '字段' + 3,
      column: 'column' + 3,
      type: '类型',
      comment: '备注' + Date.now()
    }, {
      name: '字段' + 4,
      column: 'column' + 4,
      type: '类型',
      comment: '备注' + Date.now()
    }],
    id: tableId,
    name: tableMap[tableId].name,
    description: '主表，相关的数据'
  })
}
