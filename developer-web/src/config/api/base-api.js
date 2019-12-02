import { xhr } from '@/config/api/http'
import { BASE_URL } from '@/config/api/env'

/**
 * @author 曾世平
 * @returns 获取手机验证码
 */
export const getValidatecode = async (type, cellphone) => {
  const res = await xhr.get(`${BASE_URL}publics/sms/${type}/${cellphone}`)
  return res.data.success ? res.data : Promise.reject(new Error(res.data.message))
}
/**
 * @author 曾世平 on 2019-02-13
 * @returns 登录
 */
export const userLogin = async (params) => {
  const res = await xhr.post(`${BASE_URL}publics/user/login`, params)
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}
