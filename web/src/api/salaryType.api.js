import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
const PATH = `/financial`

// 查询工资类型列表
export function listType(query) {
  return request({
    url: PATH + '/type/list',
    method: 'get',
    params: query
  })
}


// 新增工资类型
export function addType(data) {
  return request({
    url: PATH + '/type',
    method: 'post',
    data: data
    
  })
}

// 修改工资类型
export function updateType(data) {
  return request({
    url: PATH + '/type',
    method: 'put',
    data: data
  })
}

// 删除工资类型
export function delType(id) {
  return request({
    url: PATH + '/type/' + id,
    method: 'delete'
  })
}

