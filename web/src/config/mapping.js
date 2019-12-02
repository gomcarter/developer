export const ID_TYPE = {
  1: '身份证',
  2: '驾驶证',
  3: '军官证'
}

export const ENTERPRISE_TYPE = {
  wait: '待支付',
  pending: '待初审',
  collectting: '待认证官上门采集',
  confirmFailed: '初审未通过',
  reject2: '审核未通过',
  pending2: '待复审',
  confirm2Failed: '复审未通过',
  passed: '通过审核'
}

export const TRAFFIC_LIGHT = {
  red: 'red',
  yellow: '#ec971f',
  green: '#5cb85c'
}

export const VALID_PERIOD_TYPE = {
  year: '年',
  month: '个月',
  day: '天'
}

export const getShelflife = (value, type) => {
  if (value === -1) {
    return '永久'
  }
  return value + VALID_PERIOD_TYPE[type]
}

export const CODE_TYPE = {
  1: '数据一物一码',
  2: '图像一物一码',
  3: '普通图像'
}

export const CODE_DETAIL_STATE = {
  normal: '正常',
  highRisk: '异常'
}

export const ENTERPRISE_STATE = {
  waitPending: '申报材料待初审',
  waitUnPending: '申报材料初审未通过',
  waitPended: '申报材料初审通过',
  waitAudit: '申报材料待复审',
  unPass: '申报材料复审未通过',
  pass: '申报材料复审已通过',
  wait: '系统服务费待支付',
  pending: '入库材料待初审',
  collectting: '待认证官上门采集',
  confirmFailed: '入库材料初审未通过',
  reject2: '认证官采集未完成',
  pending2: '认证材料待复审',
  confirm2Failed: '认证材料复审未通过',
  passed: '认证材料复审已通过'
}

export const RECOMMEND_STATE = {
  pending: '审核中',
  passed: '通过审核',
  unpass: '审核不通过'
}

export const ENTERPRISE_EVENT = {
  create: '注册',
  pay: '支付',
  confirm: '通过',
  reject: '不通过',
  authSubmit: '认证官采集完成',
  reject2: '认证官采集未通过',
  pendingModify: '初审修改',
  pending2Modify: '复审修改',
  submit: '提交'
}

export const GOODS_STATE = {
  onshelf: '正常',
  deleted: '已删除',
  offshelf: '已下架'
}

export const CODE_STATE = {
  pending: '待审核',
  wait: '待支付',
  prepared: '可附码',
  generating: '商品码生成中',
  canceled: '已作废',
  abnormal: '异常'
}

export const UNIQUE_CODE_STATE = {
  inactive: '待激活',
  active: '已激活'
}

export const BOX_CODE_STATE = {
  normal: '正常',
  canceled: '已作废'
}

export const CODE_SCAN_EVENT = {
  create: '创建',

  // 工厂事件
  download: '下载',
  packet: '包装',

  // 经销商事件
  receive: '经销商验货',
  sold: '发货',
  allocate: '调货',
  exchange: '退换货',

  // 消费者事件
  scanValidate: '扫码验真伪'
}

export const CODE_SCAN_APP_USER = {
  zhsapp: '用户',
  distributorapp: '经销商',
  factoryapp: '工厂员工',
  printer: '喷码机',
  other: '其他'
}

// 粉丝数量区间
export const FANS_FANSNUMBER_FOR_AMOUNT = {
  hundredThousand: 100000,
  million: 1000000,
  tenMillion: 10000000
}
// 推送价格
export const FANS_AMOUNT = {
  free: 0.00,
  amountForMillion: 1000,
  amountForTenMillion: 9800
}

export const ORDER_STATE = {
  pending: '待审核',
  unPass: '审核未通过',
  shipping: '待发货',
  receiving: '待收货',
  finished: '完成',
  canceled: '已取消'
}

export const ORDER_EVENT = {
  create: '创建',
  confirm: '审核',
  reject: '审核不通过',
  sold: '发货',
  receiving: '收货',
  delete: '取消'
}
