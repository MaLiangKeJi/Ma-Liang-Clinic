import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
import * as util from '@/api/util'
export function search(query) {
  return request({
    url: '/api/financial/close/type/list',
    method: 'get',
    params: query
  })
}

export function closeBeforeCheck(callback) { util.get(`/financial/close/before/check`, {}, callback) }