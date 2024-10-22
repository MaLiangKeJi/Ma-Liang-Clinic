import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
const PATH = `/financial`

// 查询资产变动列表
export function listLog(query) {
  return request({
    url: PATH + '/change/list',
    method: 'get',
    params: query
  })
}

// 查询资产变动详细
export function getLog(id) {
  return request({
    url: PATH + '/change/' + id,
    method: 'get'
  })
}

// 新增资产变动
export function addLog(data) {
  return request({
    url: PATH + '/change',
    method: 'post',
    data: data
  })
}

// 修改资产变动
export function updateLog(data) {
  return request({
    url: PATH + '/change',
    method: 'put',
    data: data
  })
}

// 删除资产变动
export function delLog(id) {
  return request({
    url: PATH + '/change/' + id,
    method: 'delete'
  })
}
