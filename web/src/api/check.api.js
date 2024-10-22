import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
const PATH = `/financial`

export function isCheck(query) {
    return request({
        url: PATH + '/check/isCheck',
        method: 'get',
        params: query
    })
}

export function getOriCheck(query) {
    return request({
        url: PATH + '/check/getOri',
        method: 'get',
        params: query
    })
}

export function getCheckByYear(query) {
    return request({
        url: PATH + '/check/getCheckByYear',
        method: 'get',
        params: query
    })
}

// 新增结账
export function addCheck(data) {
    return request({
        url: PATH + '/check',
        method: 'post',
        data: data
    })
}

// 修改结账
export function updCheck(data) {
    return request({
        url: PATH + '/check/update',
        method: 'post',
        data: data
    })
}