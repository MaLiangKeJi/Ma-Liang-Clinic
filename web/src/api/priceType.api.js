import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
const PATH = `/financial`

// 新增币别
export function addMoneyType(data) {
    return request({
        url: PATH + '/priceType',
        method: 'post',
        data: data
    })
}

// 查询币别列表
export function listMoneyType(query) {
    return request({
        url: PATH + '/priceType/list',
        method: 'get',
        params: query
    })
}

// 根据id查币别列表
export function listByIds(query) {
    return request({
        url: PATH + '/priceType/listByIds',
        method: 'get',
        params: query
    })
}