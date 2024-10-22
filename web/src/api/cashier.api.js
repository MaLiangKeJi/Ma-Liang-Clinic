import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
const PATH = `/financial`

/**是否是初始余额 */
export function isOriMoney(query) {
    return request({
        url: PATH + '/cashier/isOriMoney',
        method: 'get',
        params: query,
    })
}

/**获取初始金额 */
export function getInitMoney(query) {
    return request({
        url: PATH + '/cashier/getInitMoney',
        method: 'get',
        params: query
    })
}

// 查询科目列表
export function listSubjects(query) {
    return request({
        url: PATH + '/cashier/listSubjects',
        method: 'get',
        params: query
    })
}

// 获取期初余额
export function getOriMoney(query) {
    return request({
        url: PATH + '/cashier/cert/oriMoney',
        method: 'get',
        params: query
    })
}

/**
 * 查询初始余额生成凭证的部分摘要
 * @param {账户id} zhId 
 */
export function getAbstByOriMoney(zhId) {
    return request({
        url: PATH + '/note/ori/cert/' + zhId,
        method: 'get',
    })
}

// 获取凭证
export function listCertificate(query) {
    return request({
        url: PATH + '/cashier/listCertificate',
        method: 'get',
        params: query
    })
}

/**
 * 是否可以删除凭证摘要
 * @param {*} certId 凭证id
 */
export function isDelCertAbst(certId) {
    return request({
        url: PATH + '/cashier/certAbst/' + certId,
        method: 'get'
    })
}

// 删除凭证摘要
export function delCertAbst(companyId, msecStr, id) {
    return request({
        url: PATH + '/cashier/certAbst/' + companyId + '/' + msecStr + '/' + id,
        method: 'delete'
    })
}

// 获取收支汇总表
export function listIOTotal(query) {
    return request({
        url: PATH + '/cashier/cert/iototal',
        method: 'get',
        params: query
    })
}

// 获取核对总账
export function listConfirm(query) {
    return request({
        url: PATH + '/cashier/cert/confirm',
        method: 'get',
        params: query
    })
}