import {xhr} from '@/config/api/http'
import {INSERV_URL} from '@/config/api/env'

/**
 * @author gomcarter on 2019-02-13
 * @returns 登录
 */
export const loginApi = async (params) => {
  const res = await xhr.post(`${INSERV_URL}publics/user/login`, params)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-26
 * @returns 接口管理---接口列表-新增
 */
export const addInterfaces = async (params) => {
  const res = await xhr.post(`${INSERV_URL}developer/interfaces`, params)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

export const deleteInterfaces = async (id) => {
  const res = await xhr.post(`${INSERV_URL}developer/interfaces/delete/${id}`)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-13
 * @returns 接口管理
 */
export const interfacesCountApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/interfaces/count`, {params})
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}
/**
 * @author gomcarter 2019-02-13
 * @returns 接口管理
 */
export const interfacesListApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/interfaces/list`, {params})
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}
/**
 * @author gomcarter 2019-02-14
 * @returns 接口管理---详情
 */
export const getInterfacesApi = async (id) => {
  const res = await xhr.get(`${INSERV_URL}developer/interfaces/${id}`)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-14
 * @returns 接口历史
 */
export const getInterfacesVersionedApi = async (interfacesId) => {
  const res = await xhr.get(`${INSERV_URL}developer/versioned/of/${interfacesId}`)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author 李银 on 2019-06-22 13:24:19
 * @returns 获取接口的headers
 */
export const getInterfacesHeadersApi = async (id) => {
  const res = await xhr.get(`${INSERV_URL}developer/interfaces/headers/${id}`)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-14
 * @returns 接口管理---新增项目
 */
export const endListApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/end/list`, {params})
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-15
 * @returns 项目管理---项目数量
 */
export const endCountApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/end/count`, params)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}
/**
 * @author gomcarter 2019-02-15
 * @returns 项目管理---添加项目
 */
export const addEndApi = async (params) => {
  const res = await xhr.post(`${INSERV_URL}developer/end`, params)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-15
 * @returns 接口管理---项目编辑
 */
export const updateEndApi = async (id, params) => {
  const res = await xhr.put(`${INSERV_URL}developer/end/${id}`, params)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-15
 * @returns 接口管理---项目详情
 */
export const getEndApi = async (id) => {
  const res = await xhr.get(`${INSERV_URL}developer/end/${id}`)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-15
 * @returns 模块管理---模块列表-模块数量
 */
export const javaCountApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/java/count`, {params})
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}
/**
 * @author gomcarter 2019-02-15
 * @returns 模块管理---模块列表-添加模块
 */
export const addJavaApi = async (params) => {
  const res = await xhr.post(`${INSERV_URL}developer/java`, params)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}
/**
 * @author gomcarter 2019-02-15
 * @returns 模块管理---模块列表-编辑模块
 */
export const updateJavaApi = async (params) => {
  const res = await xhr.put(`${INSERV_URL}developer/java/${params.id}`, params)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}
/**
 * @author gomcarter 2019-02-15
 * @returns 模块管理---模块列表-模块详情
 */
export const getJavaApi = async (id) => {
  const res = await xhr.get(`${INSERV_URL}developer/java/${id}`)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-25
 * @returns 模块管理---列表
 */
export const javaListApi = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/java/list`, {params})
  return res.data.success ? res.data.extra : Promise.reject(new Error())
}

/**
 * @author gomcarter 2019-02-28
 * @returns 类解析器---svn列表
 */
export const getAutomationaddSvn = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/automationadd/svn`, {
    params: params
  })
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 类解析器---svn列表-总数
 */
export const getAutomationaddSvnCount = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/automationadd/svn/count`, {
    params: params
  })
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 类解析器---svn列表-添加
 */
export const postAutomationadd = async (params) => {
  const res = await xhr.post(`${INSERV_URL}developer/automationadd`, params)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 类解析器---svn详情
 */
export const getAutomationaddSvnId = async (id) => {
  const res = await xhr.get(`${INSERV_URL}developer/automationadd/svn/${id}`)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 类解析器---执行
 */
export const automationaddUpdate = async (params) => {
  const res = await xhr.put(`${INSERV_URL}developer/automationadd/update`, params)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---参数规则列表
 */
export const getInterRules = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/rules`, {
    params: params
  })
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-14
 * @returns 接口管理---新增-规则
 */
export const interRules = async () => {
  const res = await xhr.get(`${INSERV_URL}developer/rules`)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---参数规则列表-数量
 */
export const getInterRulesCount = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/rules/count`, {
    params: params
  })
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---参数规则列表-添加
 */
export const postInterRules = async (params) => {
  const res = await xhr.post(`${INSERV_URL}developer/rules`, params)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---参数规则列表-编辑
 */
export const putInterRules = async (params) => {
  const res = await xhr.put(`${INSERV_URL}developer/rules/${params.id}`, params)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---参数规则列表-详情
 */
export const getInterRulesId = async (id) => {
  const res = await xhr.get(`${INSERV_URL}developer/rules/${id}`)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---用例列表
 */
export const getInterTestcase = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/testCase`, {
    params: params
  })
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
* @author gomcarter 2019-02-28
* @returns 流程控制---用例列表
*/
export const getInterTestcaseCount = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/testCase/count`, {
    params: params
  })
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---新增
 */
export const postInterTestCase = async (params) => {
  const res = await xhr.post(`${INSERV_URL}developer/testCase`, params)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---编辑
 */
export const putInterTestCase = async (params) => {
  const res = await xhr.put(`${INSERV_URL}developer/testCase/${params.id}`, params)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---用例列表
 */
export const getInterTestCaseId = async (id) => {
  const res = await xhr.get(`${INSERV_URL}developer/testCase/${id}`)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---用例接口列表
 */
export const getInterTestCaseItem = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/testCaseItem`, {
    params: params
  })
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制---用例接口列表
 */
export const getInterTestCaseItemCount = async (params) => {
  const res = await xhr.get(`${INSERV_URL}developer/testCaseItem/count`, {
    params: params
  })
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制--详情-用例列表
 */
export const getInterTestCaseItemId = async (id) => {
  const res = await xhr.get(`${INSERV_URL}developer/testCaseItem/${id}`)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}

/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制--详情-新增
 */
export const postInterTestCaseItem = async (params) => {
  const res = await xhr.post(`${INSERV_URL}developer/testCaseItem`, params)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}
/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制--详情-编辑
 */
export const putInterTestCaseItem = async (params) => {
  const res = await xhr.put(`${INSERV_URL}developer/testCaseItem/${params.id}`, params)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}
/**
 * @author gomcarter 2019-02-28
 * @returns 流程控制--用例列表-执行-获取所有测试用例接口
 */
export const getListTestCaceItem = async (id) => {
  const res = await xhr.get(`${INSERV_URL}developer/testCase/listInterfacesDetail/${id}`)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}
/**
 * @author gomcarter 2019-02-28
 * @returns 封装接口请求
 */
export const perform = async (url, method, parm) => {
  const res = await xhr[method](url, parm)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}
