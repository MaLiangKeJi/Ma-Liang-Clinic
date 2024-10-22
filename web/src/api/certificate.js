const PATH = `/financial`
import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
import qs from 'qs'

// 查询记账凭证列表
export function listCertificate(query) {
  return request({
    url: PATH + '/certificate/list',
    method: 'get',
    params: query,
    paramsSerializer: params => qs.stringify(params)
  })
}

// 查询记账凭证详细
export function getCertificate(query) {
  return request({
    url: PATH + '/certificate',
    method: 'get',
    params: query
  })
}

export function countCertificate(query) {
  return request({
    url: PATH + '/certificate/count',
    method: 'get',
    params: query
  })
}

// 新增记账凭证
export function addCertificate(data) {
  return request({
    url: PATH + '/certificate',
    method: 'put',
    data: data
  })
}

// 修改记账凭证
export function updateCertificate(data) {
  return request({
    url: PATH + '/certificate',
    method: 'post',
    data: data
  })
}

// 删除记账凭证
export function delCertificate(delList) {
  return request({
    url: PATH + '/certificate/batch',
    method: 'delete',
    data: delList,
  })
}

// 查询记账凭证详细
export function getCertificateNO(query) {
  return request({
    url: PATH + '/certificate/no',
    method: 'get',
    params: query
  })
}

export function searchCertificateAbstract(query) {
  return request({
    url: PATH + '/certificate/abstract',
    method: 'get',
    params: query
  })
}

export function addTemplate(data) {
  return request({
    url: PATH + '/certificate/template',
    method: 'put',
    data: data
  })
}
export function editTemplate(data) {
  return request({
    url: PATH + '/certificate/template',
    method: 'post',
    data: data
  })
}

export function searchTemplateType(query) {
  return request({
    url: PATH + '/certificate/template/type/list',
    method: 'get',
    params: query
  })
}

export function searchTemplateList(query) {
  return request({
    url: PATH + '/certificate/template/list',
    method: 'get',
    params: query
  })
}

export function searchTemplateJoinList(query) {
  return request({
    url: PATH + '/certificate/template/join/list',
    method: 'get',
    params: query
  })
}
export function searchTemplateJoin(query) {
  return request({
    url: PATH + '/certificate/template/join',
    method: 'get',
    params: query
  })
}

export function delTemplate(data) {
  return request({
    url: PATH + '/certificate/template',
    method: 'delete',
    data: data
  })
}

export function searchFile(query) {
  return request({
    url: PATH + '/certificate/file',
    method: 'get',
    params: query
  })
}

export function delFile(data) {
  return request({
    url: PATH + '/certificate/file',
    method: 'delete',
    data: data
  })
}

export function searchCertificateWord(query) {
  return request({
    url: PATH + '/certificate/word/list',
    method: 'get',
    params: query
  })
}
export function searchCreateName(query) {
  return request({
    url: PATH + '/certificate/create/list',
    method: 'get',
    params: query
  })
}
export function searchAuthName(query) {
  return request({
    url: PATH + '/certificate/auth/list',
    method: 'get',
    params: query
  })
}

// 修改当前公司资金凭证模板开启状态
export function updateTemplateStatus(data) {
  return request({
    url: PATH + '/certificate/template/status',
    method: 'put',
    params: data
  })
}


// 生成凭证
export function saveAssetCertificate(data) {
  return request({
    url: PATH + '/asset/certificate',
    method: 'put',
    data: data
  })
}

//根据科目id查询凭证明细
export function searchByAccountId(query) {
  return request({
    url: PATH + '/certificate/account/abstract',
    method: 'get',
    params: query
  })
}

//科目余额表
export function searchCertificateBalance(query) {
  return request({
    url: PATH + '/certificate/account/balance',
    method: 'get',
    params: query
  })
}


//总账
export function searchByAccountGeneral(query) {
  return request({
    url: PATH + '/certificate/account/general',
    method: 'get',
    params: query
  })
}

//多兰帐
export function searchByAccountColumnar(query) {
  return request({
    url: PATH + '/certificate/account/columnar',
    method: 'get',
    params: query
  })
}


//数量金额明细账
export function searchByAccountQuantity(query) {
  return request({
    url: PATH + '/certificate/account/quantity',
    method: 'get',
    params: query
  })
}

//数量金额总账
export function searchByAccountLedger(query) {
  return request({
    url: PATH + '/certificate/account/ledger',
    method: 'get',
    params: query
  })
}

