import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
const PATH = `/financial`

// 查询资产列表
export function listAsset(query) {
  return request({
    url: PATH + '/asset/list',
    method: 'get',
    params: query
  })
}


// 查询资产详细
export function getAsset(id) {
  return request({
    url: PATH + '/asset/' + id,
    method: 'get'
  })
}


// 查询资产详细
export function getScheduleAsset(query) {
  return request({
    url: PATH + '/asset/schedule',
    method: 'get',
    params: query
  })
}


// 查询资产详细
export function getSummaryAsset(query) {
  return request({
    url: PATH + '/asset/summary',
    method: 'get',
    params: query
  })
}


// 新增资产
export function addAsset(data) {
  return request({
    url: PATH + '/asset',
    method: 'put',
    data: data
  })
}

// 修改资产
export function updateAsset(data) {
  return request({
    url: PATH + '/asset',
    method: 'post',
    data: data
  })
}

// 删除资产
export function delAsset(id) {
  return request({
    url: PATH + '/asset/' + id,
    method: 'delete'
  })
}


export function searchAssetNumUnit(query) {
  return request({
    url: PATH + '/asset/num/unit/list',
    method: 'get',
    params: query,
  })
}

export function searchStoragePlace(query) {
  return request({
    url: PATH + '/asset/storage/place',
    method: 'get',
    params: query,
  })
}
// 查询资产列表
export function assetDebtCertificateOrMonthDepreciation(query) {
  return request({
    url: PATH + '/asset/depreciation/debt',
    method: 'get',
    params: query
  })
}

export function searchImportTemplateNames(query) {
  return request({
    url: PATH + '/asset/import/template/names',
    method: 'get',
    params: query,
  })
}
export function searchImportRecord(query) {
  return request({
    url: PATH + '/asset/import/recsord',
    method: 'get',
    params: query,
  })
}

export function createNumUnit(data) {
  return request({
    url: PATH + '/asset/num/unit',
    method: 'put',
    data: data
  })
}