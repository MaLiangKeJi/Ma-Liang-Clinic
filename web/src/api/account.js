// import request from '@/utils/request'
import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例

// 查询科目列表
export function listAccount(query) {
  return request({
    url: '/api/financial/account/list',
    method: 'get',
    params: query
  })
}
export function joinListAccount(query) {
  return request({
    url: '/api/financial/account/list/join',
    method: 'get',
    params: query
  })
}
export function joinAccount(query) {
  return request({
    url: '/api/financial/account/list/join',
    method: 'get',
    params: query
  })
}

// 查询科目详细
export function getAccount(id) {
  return request({
    url: '/api/financial/account/' + id,
    method: 'get'
  })
}

// 新增科目
export function addAccount(data) {
  return request({
    url: '/api/financial/account',
    method: 'put',
    data: data
  })
}

// 修改科目
export function updateAccount(data) {
  return request({
    url: '/api/financial/account',
    method: 'put',
    data: data
  })
}

// 删除科目
export function delAccount(id) {
  return request({
    url: '/api/financial/account/' + id,
    method: 'delete'
  })
}

export function sortList(data) {
  return request({
    url: '/api/financial/account/sort',
    method: 'get',
    params: data
  })
}

export function treeAccount(data) {
  return request({
    url: '/api/financial/account/tree',
    method: 'get',
    params: data
  })
}

export function addCurrency(data) {
  return request({
    url: '/api/financial/account/currency',
    method: 'put',
    data: data
  })
}

export function searchAccounList(data) {
  return request({
    url: '/api/financial/account/currency/list',
    method: 'get',
    params: data
  })
}

export function queryAccountName(data) {
  return request({
    url: '/api/financial/account/name',
    method: 'get',
    params: data
  })
}

export function addMoneyType(data) {
  return request({
    url: '/api/financial/money/type',
    method: 'put',
    data: data
  })
}

export function searchMoneyTypeList(data) {
  return request({
    url: '/api/financial/money/type/list',
    method: 'get',
    params: data
  })
}

export function addAuxiliaryCalculation(data) {
  return request({
    url: '/api/financial/auxiliary/calculation',
    method: 'put',
    data: data
  })
}

export function searchAuxiliaryCalculationList(data) {
  return request({
    url: '/api/financial/auxiliary/calculation/list',
    method: 'get',
    params: data
  })
}

export function searchAccountTree(data) {
  return request({
    url: '/api/financial/certificate/accountTree',
    method: 'get',
    params: data
  })
}


export function searchQuantityAccountTree(data) {
  return request({
    url: '/api/financial/certificate/quantity/accountTree',
    method: 'get',
    params: data
  })
}
