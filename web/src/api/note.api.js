import request from "@/api/axios.js";

const PATH = `/financial`

/**初始金额新增凭证 */
export function addCertByOriMoney(data) {
    return request({
        url: PATH + '/note/ori/cert',
        method: 'put',
        data: data,
    })
}

/**新增日记账 */
export function addNote(data) {
    return request({
        url: PATH + '/note',
        method: 'put',
        data: data
    })
}

/**新增日记账凭证 */
export function addCert(id) {
    return request({
        url: PATH + '/note/cert/' + id,
        method: 'get',
    })
}

/**
 * 导入日记账
 */
export function importNote(data) {
    return request({
        url: PATH + '/note/importNote',
        headers: {
            'Content-Type': 'multipart/form-data; boundary=----WebKitFormBoundarynl6gT1BKdPWIejNq'
        },
        method: 'post',
        data: data
    })
}

/**
 * 查询日记账列表
 */
export function listNote(query) {
    return request({
        url: PATH + '/note/list',
        method: 'get',
        params: query
    })
}

/**查询日记账 */
export function getNote(id) {
    return request({
        url: PATH + '/note/' + id,
        method: 'get',
    })
}

/**修改日记账 */
export function updNote(data) {
    return request({
        url: PATH + '/note',
        method: 'post',
        data: data
    })
}

/**查询日记账 */
export function hasNote(zhId) {
    return request({
        url: PATH + '/hasNote/' + zhId,
        method: 'get',
    })
}

/**删除日记账凭证 */
export function delNoteCert(id, certId) {
    return request({
        url: PATH + '/note/cert/' + id +'/' + certId,
        method: 'delete'
    })
}

// 删除日记账
export function delNote(idList) {
    return request({
        url: PATH + '/note/batch/' + idList,
        method: 'delete'
    })
}