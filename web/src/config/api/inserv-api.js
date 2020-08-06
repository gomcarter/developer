import { xhr } from '@/config/api/http'
import { INSERV_URL } from '@/config/api/env'
import { store } from '@/config/cache'
import { toQueryString } from '@/config/utils'

export const loginApi = async (params) => {
  const res = await xhr.post(`${INSERV_URL}publics/user/login`, params)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

export const deleteInterfaces = async (id) => {
  const res = await xhr.delete(`${INSERV_URL}developer/interfaces/${id}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-13
 * @returns 接口管理
 */
export const interfacesCountApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/interfaces/count`, { params })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}
/**
 * @author gomcarter 2019-02-13
 * @returns 接口管理
 */
export const interfacesListApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/interfaces/list`, { params })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @returns 接口转移
 */
export const transferTocustomerApi = async (id) => {
  const res = await xhr.post(`${INSERV_URL}developer/interfaces/transfer`, { id })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @returns 查询接口id是否已经被收藏
 */
export const cusInterfacesFavorites = async (interfacesIdList) => {
  const params = { interfacesIdList }
  const res = await xhr.get(`${INSERV_URL}developer/cusinterfaces/favorites`, { params })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-13
 * @returns 接口管理
 */
export const interfacesSimpleListApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/interfaces/simple/list`, { params })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-14
 * @returns 接口管理---详情
 */
export const getInterfacesApi = async (id) => {
  const res = await xhr.get(`${INSERV_URL}developer/interfaces/${id}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-14
 * @returns 接口管理---详情
 */
export const getCusInterfacesApi = async (id) => {
  const res = await xhr.get(`${INSERV_URL}developer/cusinterfaces/${id}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-14
 * @returns 接口管理---公共详情
 */
export const getPublicsInterfacesApi = async (key) => {
  const res = await xhr.get(`${INSERV_URL}publics/interfaces/${key}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-14
 * @returns 接口历史
 */
export const interfacesVersionedListApi = async (interfacesId) => {
  const res = await xhr.get(`${INSERV_URL}developer/versioned/of/${interfacesId}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}
/**
 * @author gomcarter 2019-02-14
 * @returns 接口历史
 */
export const interfacesVersionedSimpleListApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/versioned/simple/list`, { params })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author 李银 on 2019-06-22 13:24:19
 * @returns 获取接口的headers
 */
export const getInterfacesHeadersApi = async (env, endId) => {
  const res = await getPrivatesEndAuthApi(endId)
  if (res && res.config) {
    const cacheKey = env + '_' + endId
    let result = store.get(cacheKey)
    if (result) {
      return result
    }

    const config = JSON.parse(res.config)
    const c = await sendXhr(generateUrl(config.java, env, config.url, config.parameters[env]),
      config.method, config.parameters[env], config.requestHeaders)
    const argName = 'end_' + new Date().getTime()
    const headers = config.headers || []
    window[argName] = c.data.data
    if (c.data.data) {
      const reg = new RegExp('\\$data', 'g')

      result = headers.map(h => {
        let value = h.value
        try {
          if ((value || '').indexOf('$data') >= 0) {
            /* eslint-disable */
            value = new Function('return ' + value.replace(reg, argName))()
          }
        } catch (e) {
          value = h.value
        }
        return {key: h.key, value: value}
      })
      store.set(cacheKey, result, 20 * 60)
      return result
    }
    return null
  } else {
    return null
  }
}

/**
 * @author gomcarter 2019-02-14
 * @returns 接口管理---新增项目
 */
export const endListApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/end/list`, { params })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-15
 * @returns 项目管理---项目数量
 */
export const endCountApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/end/count`, params)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}
/**
 * @author gomcarter 2019-02-15
 * @returns 项目管理---添加项目
 */
export const addEndApi = async (params) => {
  const res = await xhr.post(`${INSERV_URL}developer/end`, params)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-15
 * @returns 接口管理---项目编辑
 */
export const updateEndApi = async (id, params) => {
  const res = await xhr.put(`${INSERV_URL}developer/end/${id}`, params)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-15
 * @returns 接口管理---项目详情
 */
export const getEndApi = async (id) => {
  const res = await xhr.get(`${INSERV_URL}developer/end/${id}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2020-06-22
 * @returns 设置个性鉴权接口
 */
export const setPrivatesEndAuthApi = async (endId, config) => {
  const res = await xhr.put(`${INSERV_URL}developer/end/privates/${endId}`, { config })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2020-06-22
 * @returns 获取设置个性鉴权接口
 */
export const getPrivatesEndAuthApi = async (endId) => {
  const res = await xhr.get(`${INSERV_URL}developer/end/privates/${endId}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-15
 * @returns 模块管理---模块列表-模块数量
 */
export const javaCountApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/java/count`, { params })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}
/**
 * @author gomcarter 2019-02-15
 * @returns 模块管理---模块列表-添加模块
 */
export const addJavaApi = async (params) => {
  const res = await xhr.post(`${INSERV_URL}developer/java`, params)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}
/**
 * @author gomcarter 2019-02-15
 * @returns 模块管理---模块列表-编辑模块
 */
export const updateJavaApi = async (params) => {
  const res = await xhr.put(`${INSERV_URL}developer/java/${params.id}`, params)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}
/**
 * @author gomcarter 2019-02-15
 * @returns 模块管理---模块列表-模块详情
 */
export const getJavaApi = async (id) => {
  const res = await xhr.get(`${INSERV_URL}developer/java/${id}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-25
 * @returns 模块管理---列表
 */
export const javaListApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/java/list`, { params })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error())
}

/**
 * @author gomcarter 2019-02-28
 * @returns 自定义参数列表
 */
export const functionListApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/function/list`, {
    params: params
  })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 自定义参数列表-数量
 */
export const functionCountApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/function/count`, {
    params: params
  })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---参数规则列表-添加
 */
export const addFunctionApi = async (params) => {
  const res = await xhr.post(`${INSERV_URL}developer/function`, params)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---参数规则列表-编辑
 */
export const modifyFunctionApi = async (params) => {
  const res = await xhr.put(`${INSERV_URL}developer/function/${params.id}`, params)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---参数规则列表-详情
 */
export const getFunctionApi = async (id) => {
  const res = await xhr.get(`${INSERV_URL}developer/function/${id}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---用例列表
 */
export const testCaseListApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/testCase/list`, {
    params: params
  })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---删除
 */
export const deleteTestCaseApi = async (id) => {
  const res = await xhr.delete(`${INSERV_URL}developer/testCase/${id}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---复制
 */
export const copyTestCaseApi = async (id) => {
  const res = await xhr.post(`${INSERV_URL}developer/testCase/copy/${id}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---用例列表
 */
export const testCaseCountApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/testCase/count`, {
    params: params
  })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---新增
 */
export const createTestCaseApi = async (params) => {
  const res = await xhr.post(`${INSERV_URL}developer/testCase`, params, { type: 'json' })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---编辑
 */
export const updateTestCaseApi = async (id, params) => {
  const res = await xhr.post(`${INSERV_URL}developer/testCase/${id}`, params, { type: 'json' })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---用例列表
 */
export const getTestCaseDetailApi = async (id) => {
  const res = await xhr.get(`${INSERV_URL}developer/testCase/${id}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

export const testCaseHistoryCountApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/testCase/history/count`, { params })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

export const testCaseHistoryListApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/testCase/history/list`, { params })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

export const deleteTestCaseHistoryApi = async (id) => {
  const res = await xhr.delete(`${INSERV_URL}developer/testCase/history/${id}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

export const testCaseHistoryDetailApi = async (id) => {
  const res = await xhr.get(`${INSERV_URL}developer/testCase/history/${id}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * 保存测试用户执行结果
 */
export const saveTestCaseHistorytApi = async (history) => {
  const res = await xhr.post(`${INSERV_URL}developer/testCase/history`, history, { type: 'json' })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---用例列表
 */
export const getInterfaceMarkApi = async (interfaceId) => {
  const res = await xhr.get(`${INSERV_URL}developer/mark/${interfaceId}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---新增
 */
export const addInterfaceMarkApi = async (interfaceId, mark) => {
  const res = await xhr.post(`${INSERV_URL}developer/mark/${interfaceId}`, { mark: mark })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2020年03月02日15:56:28
 * @returns 用户列表
 */
export const userListApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/user/list`, { params })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2020年03月02日15:56:31
 * @returns 用户列表计算总数
 */
export const userCountApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/user/count`, { params })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2020年03月02日15:56:31
 * @returns 删除用户
 */
export const deleteUserApi = async (id) => {
  const res = await xhr.delete(`${INSERV_URL}developer/user/${id}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2020年03月02日15:56:31
 * @returns 新增用户
 */
export const addUserApi = async (params) => {
  const res = await xhr.post(`${INSERV_URL}developer/user`, params)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2020年03月02日15:56:31
 * @returns 获取用户
 */
export const getUserApi = async (id) => {
  const res = await xhr.get(`${INSERV_URL}developer/user/${id}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2020年03月02日15:56:31
 * @returns 修改用户
 */
export const updateUserApi = async (id, params) => {
  const res = await xhr.put(`${INSERV_URL}developer/user/${id}`, params)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2020年03月02日15:56:31
 * @returns 修改用户
 */
export const updatePasswordApi = async (oldPassword, password) => {
  const res = await xhr.put(`${INSERV_URL}developer/user/password`, {oldPassword, password})
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2020年03月02日15:56:31
 * @returns 获取用户配置
 */
export const getUserSettingApi = async () => {
  const res = await xhr.get(`${INSERV_URL}developer/user/setting`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2020年03月02日15:56:31
 * @returns 修改用户配置
 */
export const updateUserSettingApi = async (params) => {
  const res = await xhr.put(`${INSERV_URL}developer/user/setting`, params)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2020年03月02日15:56:31
 * @returns 获取用户配置
 */
export const testRemoteLoginApi = async (params) => {
  const res = await xhr.post(`${INSERV_URL}publics/user/test/login`, params)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

export const mockUrl = (hash) => {
  if (INSERV_URL === '/') {
    return window.location.origin + '/publics/mock/' + hash
  } else {
    if (INSERV_URL.startsWith('http:')) {
      return INSERV_URL + 'publics/mock/' + hash
    }
    return 'http:' + INSERV_URL + 'publics/mock/' + hash
  }
}

export const generateUrl = (java, env, url, parameters) => {
  if (!url) {
    return ''
  }
  let domain = java[env]
  if (!domain) {
    return ''
  }

  parameters = parameters || []
  domain = domain.endsWith('/') ? domain.substr(0, domain.length - 1) : domain
  url = domain + '/' + (url.startsWith('/') ? url.substr(1, url.length) : url)
  parameters.filter(s => url.indexOf('{' + s.key + '}') >= 0)
    .forEach(s => {
      if (s.defaults) {
        url = url.replace('{' + s.key + '}', s.defaults)
      }
    })
  return url
}

export const publicUrl = (key) => {
  return window.location.origin + '#/public/' + key
}

export const originMockUrl = (domain, url) => {
  return `${domain}/_mock?url=${encodeURIComponent(url)}`
}

export const packageListApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/packaged/list`, {params})
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

export const packageCountApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/packaged/count`, {params})
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

export const getPackageApi = async (id) => {
  const res = await xhr.get(`${INSERV_URL}developer/packaged/${id}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

export const addPackageApi = async (params) => {
  const res = await xhr.post(`${INSERV_URL}developer/packaged`, params)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

export const deletePackageApi = async (id) => {
  const res = await xhr.delete(`${INSERV_URL}developer/packaged/${id}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}


export const saveCusTestApi = async (id,parameters,javascript,preParams) => {
  const res = await xhr.post(`${INSERV_URL}developer/interfaces/transfer`, {id ,parameters,javascript,preParams})
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

export const deleteCusInterfaces = async (id) => {
  const res = await xhr.delete(`${INSERV_URL}developer/cusinterfaces/${id}`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

export const cusInterfacesCountApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/cusinterfaces/count`, { params })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

export const bindFavoriteApi = async (id, favoriteCode) => {
  const res = await xhr.put(`${INSERV_URL}developer/cusinterfaces/bind/${id}`, {favoriteCode})
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

export const cusInterfacesListApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/cusinterfaces/list`, { params })
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

export const favoriteCreateApi = async (parentId, name) => {
  const res = await xhr.post(`${INSERV_URL}developer/favorite`, {parentId, name})
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

export const favoriteUpdateApi = async (id, name) => {
  const res = await xhr.put(`${INSERV_URL}developer/favorite/${id}`, {name})
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

export const favoriteTreeApi = async () => {
  const res = await xhr.get(`${INSERV_URL}developer/favorite/tree`)
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

export const sortFavoriteApi = async (id, sort) => {
  const res = await xhr.put(`${INSERV_URL}developer/favorite/sort/${id}`, {sort})
  return res.data.code === 0 ? res.data.data : Promise.reject(new Error(res.data.message))
}

const prepare = async(url, method, parameters, inputHeaders, preParams) => {
  parameters = parameters || []
  preParams = preParams || []
  const result = {
    method: (method || 'POST').toLowerCase(),
    params: {},
    url: url
  }

  // 处理预置参数
  if (typeof preParams === 'string') {
    preParams = JSON.parse(preParams)
  }
  if (preParams && preParams.length > 0) {
    for (let index = 0; index < preParams.length; ++index) {
      const p = preParams[index]
      let key = p.key
      if (!key) {
        continue
      }
      window['$' + key] = await processParams(p)
      console.log(`初始化预置参数：$${key}=${window['$' + key]}`)
    }
  }

  // 处理接口参数
  const params = result.params
  if (parameters && parameters.length > 0) {
    for (let index = 0; index < parameters.length; ++index) {
      const p = parameters[index]
      let key = p.key
      if (!key) {
        continue
      }
      let value = await processParams(p)
      console.log(`初始化参数：${key}=${value}`)
      if (value == null) {
        continue
      }

      if (p.body) {
        result.body = typeof value === 'string' ? value : JSON.stringify(value)
      } else if (url.indexOf(`{${ key }}`) >= 0) {
        result.url = url.replace(`{${ key }}`, value)
      } else {
        // 同一key的时候，需要合并
        if (params[key] === undefined) {
          params[key] = value
        } else {
          params[key] = [value].concat(params[key])
        }
      }
    }
  }

  result.headers = inputHeaders.map(h => {
    let value = h.value
    try {
      if (h.value.indexOf('$') === 0) {
        value = new Function('return ' + h.value)()
      }
    } catch (e) {
      value = h.value
    }
    return { key: h.key, value: value }
  })

  return result
}

/**
 * 处理预置参数
 */
export const processParams = async(p) => {
  let value = p.defaults
  // 如果value里面存在变量引用，则执行脚本
  if (p.fix && (value + '').indexOf('$') >= 0) {
    let script = 'return ' + (value + '').replace(/\n/g, ' ')
    try {
      /* eslint-disable */
      value = new Function(script)()
    } catch (e) {
      console.log('执行参数转换失败', script, e)
    }
  }

  if (!p.fix && p.functionId) {
    // 如果有functionId还要去拉取function脚本自动生成
    const fr = await getFunctionApi(p.functionId)
    /* eslint-disable */
    const args = (p.arguments || '').split(',').map(s => {
        try {
          return new Function(' return ' + s)()
        } catch(e) {
          return s
        }
      })
    value = new Function(fr.scriptText)(...args)
  }

  return value
}

const send = (url, method, params, headers, body) => {
  let p = params
  if (['put', 'post', 'patch'].indexOf(method) < 0) {
    p = { params }
  }

  if (body) {
    let type
    try {
      JSON.parse(body)
      type = 'json'
    } catch (e) {
      type = 'raw'
    }
    const u = url + (url.indexOf('?') >= 0 ? '&' : '?') + toQueryString(params)
    return xhr[method](u, body, { type: type, customHeaders: headers, notice: false, cookie: false })
  } else if (method === 'post') {
    return xhr[method](url, p, { customHeaders: headers, notice: false, cookie: false })
  } else {
    p.customHeaders = headers
    p.notice = false
    p.cookie = false
    return xhr[method](url, p)
  }
}

export const sendXhr = async(inputUrl, inputMethod, parameters, inputHeaders) => {
  let { url, method, params, headers, body } = await prepare(inputUrl, inputMethod, parameters, inputHeaders)
  return send(url, method, params, headers, body)
}

window.assert = (express, message) => {
  if (!express) {
    throw new Error(message)
  }
  return express
}

/**
 *
 * @param inputUrl      接口地址，https://xxxxx 全路径形式
 * @param inputMethod   get，post，put等
 * @param parameters    参数
 * @param inputHeaders  头
 * @param preParams     预置参数
 * @param resultName    接口调用返回值存入window[resultName]中, 可不填写，不填写系统自动生成
 * @param checkScript   检查点脚本，可以不填写，不填写不执行检查点
 */
export const mockXhr = async(inputUrl, inputMethod, parameters, inputHeaders, preParams, checkScript, resultName) => {
  let { url, method, params, headers, body } = await prepare(inputUrl, inputMethod, parameters, inputHeaders, preParams)

  let check, message, result, success
  try {
    result = await send(url, method, params, headers, body)

    success = result.status === 200
    // 执行检查点
    if (success) {
      if (!resultName) {
        resultName = '$' + Date.now()
      }
      // 赋值
      window[resultName] = result.data

      checkScript = (checkScript || '').trim()
      if (checkScript) {
        // 有检查点，执行检查点
        check = true
        const m = []
        // 多行检查点，依次执行
        checkScript.split('\n')
          .forEach(s => {
            try {
              new Function(s.replace(/\$this/g, resultName)).call(result.data);
              m.push(`<p style="color:green">${s}</p>`)
            } catch (e) {
              check = false
              m.push(`<p style="color:red">${s}：${e.toString()}</p>`)
            }
          })

        message = m.join('')
      }
    } else {
      message = `${ result.status } - ${ result.statusText }`
    }
  } catch (e) {
    message = e.message
    result = {
      data: message
    }
    success = false
  }

  return {
    url,
    method,
    params,
    headers,
    body,
    result,
    check,
    message,
    success
  }
}
