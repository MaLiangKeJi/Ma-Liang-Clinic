import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
const PATH = `/financial`

// 查询资产类别列表
export function listType(query) {
  return request({
    url: PATH + '/asset/type/list',
    method: 'get',
    params: query
  })
}

// 查询资产类别详细
export function getType(id) {
  return request({
    url: PATH + '/asset/type/' + id,
    method: 'get'
  })
}

// 新增资产类别
export function addType(data) {
  return request({
    url: PATH + '/asset/type',
    method: 'put',
    data: data
  })
}

// 修改资产类别
export function updateType(data) {
  return request({
    url: PATH + '/asset/type',
    method: 'post',
    data: data
  })
}

// 删除资产类别
export function delType(id) {
  return request({
    url: PATH + '/asset/type/' + id,
    method: 'delete'
  })
}
