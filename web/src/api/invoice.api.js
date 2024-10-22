import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例

const PATH = `/financial`

/**新增发票 */
export function addInvoice(data) {
    return request({
        url: PATH + '/invoice',
        method: 'put',
        data: data
    })
}

/**新增名为存货的辅助核算 */
export function addInvoiceAux(data) {
    return request({
        url: PATH + '/invoice/auxiliary',
        method: 'put',
        data: data
    })
}

/**设置默认凭证模板 */
export function setDefaultTemplate(invoiceCategory, tempId) {
    return request({
        url: PATH + '/invoice/template/default/' + invoiceCategory + '/' + tempId,
        method: 'get',
    })
}

/**发票生成凭证 */
export function generateCert(data) {
    return request({
        url: PATH + '/invoice/cert',
        method: 'put',
        data: data
    })
}

/**获取发票列表 */
export function listInvoice(query) {
    return request({
        url: PATH + '/invoice/list',
        method: 'get',
        params: query,
    })
}

/**获取名为存货的辅助核算列表 */
export function listInvoiceAux(query) {
    return request({
        url: PATH + '/invoice/auxiliary/list',
        method: 'get',
        params: query,
    })
}

/**获取发票生成凭证预览 */
export function listInvoiceCert(query) {
    return request({
        url: PATH + '/invoice/cert/preview',
        method: 'get',
        params: query,
    })
}

/**查询辅助核算分组 */
export function groupAuxByInvoice(query) {
    return request({
        url: PATH + '/invoice/aux',
        method: 'get',
        params: query,
    })
}

/**查询辅助核算列表 */
export function listAuxByInvoice(typeId) {
    return request({
        url: PATH + '/invoice/aux/' + typeId,
        method: 'get',
    })
}

/**
 * 查询凭证模板分页
 * @param {页码} current 
 * @param {条数} size 
 * @param {发票分类：0.销项发票;1.进项发票;2.费用小票;} invoiceCategory 
 * @param {是否启用该项目：1.启用;0.关闭;} isActive 
 * @param {模板名称} name 
 */
export function pageCertTemp(current, size, invoiceCategory, isActive, name) {
    return request({
        url: PATH + '/invoice/template/list/' + current + '/' + size + '/' + invoiceCategory + '/' + isActive + '/' + name,
        method: 'get',
    })
}

/**
 * 查询本期税负测算
 * @param {当前期数毫秒数} msec 
 */
export function searchTaxBurden(msec) {
    return request({
        url: PATH + '/invoice/taxBurden/' + msec,
        method: 'get',
    })
}

/**
 * 获取发票
 * @param {发票id} id 
 * @param {是否显示发票明细} isShowDetail 
 */
export function getInvoice(id, isShowDetail) {
    return request({
        url: PATH + '/invoice/' + id + '/' + isShowDetail,
        method: 'get',
    })
}

/**获取凭证模板 */
export function getTemplate(id) {
    return request({
        url: PATH + '/invoice/template/' + id,
        method: 'get',
    })
}

/**更新发票 */
export function updInvoice(data) {
    return request({
        url: PATH + '/invoice',
        method: 'post',
        data: data
    })
}

/**更新发票明细 */
export function updInvoiceDetail(data) {
    return request({
        url: PATH + '/invoice/detail',
        method: 'post',
        data: data
    })
}

/**
 * 更新发票明细的辅助核算
 */
export function updAuxByDetail(data) {
    return request({
        url: PATH + '/invoice/detail/aux',
        method: 'post',
        data: data,
    })
}

/**更新凭证模板 */
export function updateCertificateTemplate(data) {
    return request({
        url: PATH + '/invoice/temp',
        method: 'post',
        data: data,
    })
}

/**更新本期税负测算 */
export function updateTaxBurden(data) {
    return request({
        url: PATH + '/invoice/taxBurden',
        method: 'post',
        data: data,
    })
}

/**
 * 开启模板
 * @param {模板id} tempId 
 */
export function enableCertTemp(tempId) {
    return request({
        url: PATH + '/invoice/cert/temp/enable/' + tempId,
        method: 'get',
    })
}

/**
 * 发票认证
 * @param {发票id} invId 
 */
export function authInvoice(invId) {
    return request({
        url: PATH + '/invoice/auth/' + invId,
        method: 'get',
    })
}

/**删除发票 */
export function delInvoice(idList) {
    return request({
        url: PATH + '/invoice/' + idList,
        method: 'delete'
    })
}

/**删除发票凭证 */
export function delInvoiceCert(id, certId) {
    return request({
        url: PATH + '/invoice/cert/' + id + '/' + certId,
        method: 'delete'
    })
}