import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
import * as util from '@/api/util'
export function searchCompany(query) {
  return request({
    url: '/api/auth/back/company',
    method: 'get',
    params: query
  })
}

export function searchSystem(callback) { util.get('/api/auth/back/system', {}, callback) }

export function searchSystemRouterTree(query) { return request({ url: '/api/auth/back/system/router/tree', method: 'get', params: query }) }

export function searchSystemRouter(id, callback) { util.get('/api/auth/back/system/router', { id }, callback) }

export function addSystemRouter(params, callback) { util.put(`/api/auth/back/system/router`, params, callback, true) }

export function delSystemRouter(params, callback) { util.del(`/api/auth/back/system/router/` + params, params, callback, true) }

export function searchSystemRouterList(systemId, callback) { util.get(`/api/auth/back/system/router/list`, { systemId }, callback) }

export function moveSystemRouter(routerId, targetRouterId, callback) { util.update(`/api/auth/back/system/router/move`, { routerId, targetRouterId, }, callback, true) }

export function batchUpdateSystemRouterType(ids, type, callback) { util.update(`/api/auth/back/system/router/batch`, { ids, type, }, callback, true) }

export function updateSystemRouter(router, callback) { util.update(`/api/auth/back/system/router`, router, callback, true) }

export function searchUserList(params, callback) { util.get(`/api/auth/back/user/list`, params, callback, false) }

export function searchRenewLogs(params, callback) { util.get(`/api/auth/back/renew/log`, params, callback, false) }

export function searchCumulativePaymentRecord(params, callback) { util.get(`/api/auth/back/renew/count/log`, params, callback, false) }


export function searchMeInviteClientCumulativePaymentRecord(params, callback) { util.get(`/api/auth/back/renew/log/me/count`, params, callback, false) }


export function searchMeInviteClientPaymentRecord(params, callback) { util.get(`/api/auth/back/renew/log/me/list`, params, callback, false) }
