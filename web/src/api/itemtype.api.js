import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
const PATH = `/financial`

// 查询核算项目类型列表
export function listType(query) {
  return request({
    url: PATH + '/salary/item/type/list',
    method: 'get',
    params: query
  })
}


// 新增核算项目类型
export function addType(data) {
  return request({
    url: PATH + '/salary/item/type',
    method: 'post',
    data: data
  })
}

// 修改核算项目类型
export function updateType(data) {
  return request({
    url: PATH + '/salary/item/type',
    method: 'put',
    data: data
  })
}

// 删除核算项目类型
export function delType(id) {
  return request({
    url: PATH + '/salary/item/type/' + id,
    method: 'delete'
  })
}
