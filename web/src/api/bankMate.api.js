import request from "@/api/axios.js";

const PATH = `/financial`

/**
 * 导入日记账
 */
export function importBankMate(data) {
    return request({
        url: PATH + '/bankMate/importBankMate',
        headers: {
            'Content-Type': 'multipart/form-data; boundary=----WebKitFormBoundarynl6gT1BKdPWIejNq'
        },
        method: 'post',
        data: data
    })
}

/**
 * 查询银行对账单列表
 */
export function listBankMate(query) {
    return request({
        url: PATH + '/bankMate/list',
        method: 'get',
        params: query
    })
}

// 删除银行对账单
export function delBankMate(id) {
    return request({
        url: PATH + '/bankMate/' + id,
        method: 'delete'
    })
}