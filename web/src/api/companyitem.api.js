import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
const PATH = `/financial`

// 查询当前公司核算凭证列表
export function listCompanyItem(query) {
  return request({
    url: PATH + '/item/list',
    method: 'get',
    params: query
  })
}

// 新增当前公司核算凭证
export function addCompanyItem(data) {
  return request({
    url: PATH + '/item',
    method: 'post',
    data: data
  })
}

// 修改当前公司核算凭证
export function updateCompanyItem(data) {
  return request({
    url: PATH + '/item',
    method: 'put',

    data: data
  })
}

// 修改当前公司核算凭证开启状态
export function updateCompanyItemStatus(data) {
  return request({
    url: PATH + '/salary/item/status',
    method: 'put',
    params: data
  })
}

// 删除当前公司核算凭证
export function delCompanyItem(id) {
  return request({
    url: PATH + '/item/' + id,
    method: 'delete'
  })
}
