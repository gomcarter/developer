import { xhr } from '@/config/api/http'
import { ITEM_URL } from '@/config/api/env'

/**
 * @author 李银
 * @returns 商品搜索
 */
export const searchApi = async (params) => {
  const res = await xhr.get(`${ITEM_URL}/publics/item/search`, { params })
  return res.data.success ? res.data.extra : Promise.reject(new Error(res.data.message))
}
