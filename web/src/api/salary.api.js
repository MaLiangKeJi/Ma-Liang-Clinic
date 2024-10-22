import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
const PATH = `/financial`

// 查询工资列表
export function listSalary(query) {
  return request({
    url: PATH + '/salary/list',
    method: 'get',
    params: query
  })
}

// 查询工资详细
export function getSalary(id) {
  return request({
    url: PATH + '/salary/' + id,
    method: 'get'
  })
}

// 新增工资
export function importSalary(data) {
  return request({
    url: PATH + '/salary/import',
    headers: {
      'Content-Type': 'multipart/form-data; boundary=----WebKitFormBoundarynl6gT1BKdPWIejNq'
    },
    method: 'post',
    data: data
  })
}

// 生成凭证
export function saveSalaryCertificate(data) {
  return request({
    url: PATH + '/salary/certificate',
    method: 'put',
    data: data
  })
}

// 修改工资
export function updateSalary(data) {
  return request({
    url: PATH + '/salary',
    method: 'put',
    data: data
  })
}

// 删除工资
export function delSalary(id) {
  return request({
    url: PATH + '/salary/' + id,
    method: 'delete'
  })
}
