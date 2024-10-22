import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例


// 查询科目列表
export function searchLoginSet(query) {
    return request({
      url: '/api/financial/accounting/set/list',
      method: 'get',
      params: query
    })
}


export function selectedLoginSet(query) {
  return request({
    url: '/api/financial/accounting/set/selected',
    method: 'put',
    params: query
  })
}