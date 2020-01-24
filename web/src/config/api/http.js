import axios from 'axios'
import qs from 'query-string'
import {removeBlank} from '@/config/utils'
import {that} from '../../main.js'

// 表示跨域请求时是否需要使用凭证
axios.defaults.withCredentials = true
// 超时时间20s
axios.defaults.timeout = 15000
//
axios.defaults.crossDomain = true

// 添加请求拦截器
axios.interceptors.request.use((config) => {
  const customHeaders = config.customHeaders || (config.params && config.params.customHeaders)
  // console.log(customHeaders)
  if (config.params) {
    delete config.params.customHeaders
  }

  if (customHeaders && customHeaders.length > 0) {
    customHeaders.filter(n => n.key).forEach(h => { config.headers[h.key] = h.value })
  }

  config.paramsSerializer = params => qs.stringify(params)
  // 在发送请求之前做些什么
  if (['post', 'put', 'patch'].indexOf(config.method) >= 0) {
    if (config.type === 'updoad') {
      // do nothing
    } else if (config.type === 'json') {
      if (typeof config.data !== 'string') {
        config.data = JSON.stringify(config.data)
      }
      config.headers['Content-type'] = 'application/json'
    } else if (config.type === 'raw') {
      config.headers['Content-type'] = 'html/text'
    } else {
      config.data = qs.stringify(removeBlank(config.data))
      config.headers['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8'
    }
  } else if (config.method === 'get') {
    config.params = removeBlank(config.params)
  }
  return config
}, error => Promise.reject(error))

let alerted = false

axios.interceptors.response.use((response) => {
  if (response.config.notice !== false) {
    // 判断http状态码
    if ([200, 304, 400].indexOf(response.status) === -1) {
      that.$alert('网络异常！', '提示', {type: 'error'})
    } else if (response.data.code === -401) {
      if (!alerted) {
        alerted = true
        that.$alert('登录超时', '提示', {type: 'error'}).then(() => that.$router.push('/login'))

        setTimeout(() => { alerted = false }, 5000)
      }
    } else if (!response.data.success) {
      that.$alert(response.data.message || '请求失败！', '提示', {type: 'error'})
    }
  }
  return response
}, error => Promise.reject(error))

export const xhr = axios
