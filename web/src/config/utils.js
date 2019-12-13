/* eslint-disable */
import { IMAGE_SERVER_URL } from '@/config/api/env'
import { Notification } from 'element-ui'

export const toJsonHtml = (json) => {
  const rawed = JSON.stringify(json || {}, null, 4).replace(/</g, '&lt;').replace(/>/g, '&gt;').split(/\n/)
  const r = ['<span class="kvov arrElem rootKvov">']
  // 当前缩进， 当缩进增加则增加一个blockInner，下面的元素进入此元素自己根据每一行的缩进
  let blockIndex = 0

  rawed.forEach(row => {
    let tt = (row || '').trimLeft()
    let bIndex = row.length - tt.length
    if (tt.length === 0) {
      return
    }

    try {
      let t = tt
      let hasComma = false
      if (t.endsWith(',')) {
        t = t.substring(0, tt.length - 1)
        hasComma = true
      }
      const s = JSON.parse(`{${t}}`)
      tt = Object.keys(s)
        .map(key => {
          let value = s[key]
          return `"${key}": <span class="json-${typeof value}">${JSON.stringify(value)}</span>${hasComma ? ',' : ''}`
        })[0]
    } catch (e) {
    }

    tt = tt.replace(/\{/, '<span class="b">{</span>')
      .replace(/\}/, '<span class="b">}</span>')
      .replace(/\[/, '<span class="b">[</span>')
      .replace(/\]/, '<span class="b">]</span>')

    // 此行缩进大于当前缩进
    if (bIndex > blockIndex) {
      // 在缩进之前加展开按钮
      r.push('<span class="expander"></span>')
      r.push('<span class="ell"></span>')
      r.push('<span class="blockInner">')

      r.push('<span class="kvov objProp">' + tt)
    } else if (bIndex < blockIndex) {
      // 小于缩进
      r.push('</span>')
      r.push('</span>')
      r.push(tt)
    } else {
      r.push('</span>')
      r.push('<span class="kvov objProp">' + tt)
    }
    blockIndex = bIndex
  })

  r.push('</span>')
  return r.join('')
}

/**
 * @param data
 * @returns {array|object|null|undefined|function|error|regexp|number|string|boolean}
 */
export const type = (data) => {
  const t = typeof data

  switch (typeof data) {
    case 'object':
      if (data === null) {
        return 'null'
      } else if (data instanceof Array) {
        return 'array'
      } else if (data instanceof RegExp) {
        return 'regexp'
      } else if (data instanceof Error) {
        return 'error'
      }
      return t
    default:
      return t
  }
}

/**
 * 示例：
 * {a:1,b:[1,2,3]}  => a=1&b=1&b=2&b=3
 * {a:{b:1,c:[1,2]}} => a.b=1&a.c=1&a.c=2
 * {a:{b:1,c:[{d:1},{d:2},{e:3}]}} => a.b=1&a.c.d=1&a.c.d=2&a.c.e=3
 * {a:{b:{d:{e:[{f:{g:1}},{f:{g:2}}]}},c:[1,2]}} => a.b.d.e.f.g=1&a.b.d.e.f.g=2&a.c=1&a.c=2
 * toQueryString([1,2,3,4], 'c') => c=1&c=2&c=3&c=4
 *
 * 将忽略值是null|undefined|function|error|regexp类型的数据
 * @param data
 * @param prefix 参数名前缀
 * @returns {*}
 */
export const toQueryString = (data, prefix) => {
  prefix = prefix || ''
  const queryString = []
  switch (type(data)) {
    // []类型
    case 'array':
      if (prefix === '') {
        return ''
      }
      data.forEach((s) => {
        const d = toQueryString(s, prefix)
        if (d !== null && d !== undefined) {
          queryString.push(d)
        }
      })
      break
    // {}类型
    case 'object':
      Object.entries(data)
        .forEach((s) => {
          let key = s[0]
          const value = s[1]
          if (prefix !== '') {
            key = `${prefix}.${key}`
          }
          const d = toQueryString(value, key)
          if (d !== null && d !== undefined) {
            queryString.push(d)
          }
        })
      break
    case 'string':
    case 'boolean':
    case 'number':
      if (prefix === '') {
        return ''
      }
      return `${prefix}=${encodeURIComponent(data)}`
    // 其他类型忽略
    default:
      return ''
  }
  return queryString.join('&')
}

/**
 * 删除对象-属性值是null|undefined|function|error|regexp类型的数据
 * 只删除第一层数据，不递归删除；
 *
 * @param params {array|object|string}
 * @returns {Array|Object|String} 返回一个新的和params类型一致的数据
 */
export const removeIllegalParams = (params, removeType) => {
  return remove(params, ['null', 'undefined', 'function', 'error', 'regexp'])
}

/**
 * 删除对象-属性值是 指定 toRemoveType 的数据
 * @param params {array|object|string}
 * @param toRemoveType {null|undefined|function|error|regexp}
 * @returns {Array|Object|String} 返回一个新的和params类型一致的数据
 */
export const remove = (params, toRemoveType) => {
  const t = type(params)
  if (t === 'object') {
    const localParams = {}
    Object.keys(params || ({})).forEach((v) => {
      if (toRemoveType.indexOf(type(params[v])) === -1) {
        localParams[v] = params[v]
      }
    })
    return localParams
  } else if (t === 'array') {
    return params.filter(s => toRemoveType.indexOf(type(s)) === -1)
  }
  return params
}

/**
 * 移除params里面的 空，null，undefined的数据
 * @param params
 * @param toRemoveType
 * @returns {*}
 */
export const removeBlank = (params) => {
  const toRemoveType = ['null', 'undefined', 'function', 'error', 'regexp']
  const t = type(params)
  if (t === 'object') {
    const localParams = {}
    Object.keys(params || ({})).forEach((v) => {
      if (toRemoveType.indexOf(type(params[v])) === -1 && `${params[v]}`.trim() !== '') {
        localParams[v] = params[v]
      }
    })
    return localParams
  } else if (t === 'array') {
    return params.filter(s => toRemoveType.indexOf(type(s)) === -1 && `${s}`.trim() !== '')
  }
  return params
}

export const padLeftZero = str => `00${str}`.substr(str.length)

/**
 * 将时间格式化为字符串。
 * @param date ： 可以是Date对象也可以是timestamp
 * @param fmt ： 格式化之后的样式，默认： yyyy-MM-dd HH:mm:ss
 * @returns {string}
 */
export const formatDate = (date, fmt = 'yyyy-MM-dd hh:mm:ss') => {
  if (!date) {
    return ''
  }
  // debugger
  // if (typeof date === 'number') {
  //   date = new Date(date);
  // }
  date = new Date(date)
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (`${date.getFullYear()}`).substr(4 - RegExp.$1.length))
  }
  const o = {
    'M+': date.getMonth() + 1,
    'd+': date.getDate(),
    'h+': date.getHours(),
    'm+': date.getMinutes(),
    's+': date.getSeconds(),
  }
  Object.keys(o)
    .forEach((k) => {
      if (new RegExp(`(${k})`).test(fmt)) {
        const str = `${o[k]}`
        fmt = fmt.replace(RegExp.$1, RegExp.$1.length === 1 ? str : padLeftZero(str))
      }
    })
  return fmt
}

export const success = (message) => {
  Notification({
    type: 'success',
    position: 'bottom-right',
    message: message || '操作成功！',
    duration: 3000,
    title: '提示'
  })
}

/**
 * 下载文件
 * @param
  content: 指定下载地址
 filename: 指定下载文件名
 */
export const download = (content, filename) => {
  // 创建隐藏的可下载链接
  const eleLink = document.createElement('a')
  eleLink.download = filename
  eleLink.style.display = 'none'
  eleLink.href = content
  // 触发点击
  document.body.appendChild(eleLink)
  eleLink.click()
  // 然后移除
  document.body.removeChild(eleLink)
}

/**
 * bIsPcOrPhone
 * return
 true: pc
 false: phone
 */
export const bIsPcOrPhone = () => {
  const sUserAgent = navigator.userAgent.toLowerCase()
  const bIsIpad = sUserAgent.match(/ipad/i) == 'ipad'
  const bIsIphoneOs = sUserAgent.match(/iphone os/i) == 'iphone os'
  const bIsMidp = sUserAgent.match(/midp/i) == 'midp'
  const bIsUc7 = sUserAgent.match(/rv:1.2.3.4/i) == 'rv:1.2.3.4'
  const bIsUc = sUserAgent.match(/ucweb/i) == 'ucweb'
  const bIsAndroid = sUserAgent.match(/android/i) == 'android'
  const bIsCE = sUserAgent.match(/windows ce/i) == 'windows ce'
  const bIsWM = sUserAgent.match(/windows mobile/i) == 'windows mobile'
  return !(bIsIpad || bIsIphoneOs || bIsMidp || bIsUc7 || bIsUc || bIsAndroid || bIsCE || bIsWM)
}

/**
 * 全屏
 */
export const fullScreen = (el) => {
  const rfs = el.requestFullScreen || el.webkitRequestFullScreen || el.mozRequestFullScreen || el.msRequestFullscreen
  if (typeof rfs != 'undefined' && rfs) {
    rfs.call(el)
  }

  return
}
/**
 * 退出全屏
 */
export const exitScreen = () => {
  if (document.exitFullscreen) {
    document.exitFullscreen()
  } else if (document.mozCancelFullScreen) {
    document.mozCancelFullScreen()
  } else if (document.webkitCancelFullScreen) {
    document.webkitCancelFullScreen()
  } else if (document.msExitFullscreen) {
    document.msExitFullscreen()
  }
  if (typeof cfs != 'undefined' && cfs) {
    cfs.call(el)
  }
}

/**
 * 跳转至成功页面
 * @param opts ：{
          message: '操作成功！', // 提示语句，可以为空
          buttons: [{ //操作按钮
            text: '返回',
            link: '/role/list', // 要去的页面
          }],
        }
 */
  // 这里使用function定义函数，是为了保留函数里面的this指针
export const transfer = function (opts = {}) {
    opts = opts || {}
    // 跳转到操作成功页面
    this.$router.push(`/transfer/${encodeURIComponent(JSON.stringify(opts))}`)
  }

/**
 * 对象深刻隆
 * @param opts ：{}
 */
export const clone = opts => JSON.parse(JSON.stringify(opts || {}))

/**
 * 自动补全位数
 * @param num 数字
 * @param length 位数
 */
export const prefixInteger = (num, length = 2) => (Array(length).join('0') + num).slice(-length)

/**
 * 获取数组长度
 * @param array
 */
export const getArrLen = (o) => (o || []).length

/**
 * 移动端上下左右执行事件
 * 列子： touch({
      left2right: this.preIndex,
      right2left: this.nextIndex
    })
 */
export const touch = (fns = {
  left2right: () => {
  },
  right2left: () => {
  },
  top2bottom: () => {
  },
  bottom2top: () => {
  }
}) => {
  let startX
  let startY
  let endX
  let endY
  let X
  let Y
  let flag = true

  const runFn = (fnnm) => {
    if (flag) {
      flag = false
      fns[fnnm] && fns[fnnm]()
    }
  }

  document.body.addEventListener('touchstart', (e) => {
    startX = e.changedTouches[0].pageX
    startY = e.changedTouches[0].pageY
  }, false)

  document.body.addEventListener('touchmove', (e) => {
    cancelable(e)
    endX = e.changedTouches[0].pageX
    endY = e.changedTouches[0].pageY
    X = endX - startX
    Y = endY - startY
    if (Math.abs(X) > Math.abs(Y) && X > 0 && Math.abs(X) > 70) {
      // 左到右
      runFn('left2right')
    } else if (Math.abs(X) > Math.abs(Y) && X < 0 && Math.abs(X) > 70) {
      // 右到左
      runFn('right2left')
    } else if (Math.abs(Y) > Math.abs(X) && Y > 0 && Math.abs(Y) > 70) {
      // 上到下
      runFn('top2bottom')
    } else if (Math.abs(Y) > Math.abs(X) && Y < 0 && Math.abs(Y) > 70) {
      // 下到上
      runFn('bottom2top')
    }
  }, false)

  document.body.addEventListener('touchend', (e) => {
    flag = true
  }, false)
}

export const cancelable = (e) => {
  // 判断默认行为是否可以被禁用
  if (e.cancelable) {
    // 判断默认行为是否已经被禁用
    if (!e.defaultPrevented) {
      e.preventDefault()
    }
  }
}

/**
 * 获取图片的全路径
 * 详见官网： http://zimg.buaa.us/documents/
 * @param url ： 图片地址
 * @param params : { w ： 宽度, h ： 高度, q ： 质量的百分比(0-100整数), f : png/jpg（图片格式）, r : 旋转度数(0-360整数) }
 * @return [] | String : 传入的url是一个数组，则返回数组，传入一个string则返回单个url
 */
export const getPictureUrl = (url, params = {}) => {
  const paramArray = Object.entries(params)
  const p = paramArray.length === 0 ? '' : paramArray
    .map(s => `${s[0]}=${s[1]}`)
    .reduce((a, b) => `${a}&${b}`)

  if (typeof url === 'string') {
    return '/' + url + (url.indexOf('?') >= 0 ? '&' : '?') + p
  }
  return url.map(v => '/' + v + (v.indexOf('?') >= 0 ? '&' : '?') + p)
}

export const getUrlHashParams = () => {
  const searchsplits = window.location.hash.split('?')
  const search = searchsplits.length > 1 ? searchsplits[1] : ''
  const r = {}
  if (!search || search.length < 3) {
    return r
  }
  search.slice(0, search.length).split('&').forEach(k => {
    const p = k.split('=')
    const key = p[0]
    const value = p[1]
    if (value) {
      if (r[key] != null) {
        r[key] = [value].concat(r[key])
      } else {
        r[key] = value
      }
    }
  })
  return r
}
