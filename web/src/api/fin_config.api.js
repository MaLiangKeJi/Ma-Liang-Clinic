import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
const PATH = `/financial`

// 查询资产配置列表
export function listConfig(query) {
    return request({
        url: PATH + '/config/list',
        method: 'get',
        params: query
    })
}

// 查询资产配置详细
export function getConfig(id) {
    return request({
        url: PATH + '/config/' + id,
        method: 'get'
    })
}

// 新增资产配置
export function addConfig(data) {
    return request({
        url: PATH + '/config',
        method: 'post',
        data: data
    })
}

// 修改资产配置
export function updateConfig(data) {
    return request({
        url: PATH + '/config',
        method: 'put',
        data: data
    })
}

// 删除资产配置
export function delConfig(id) {
    return request({
        url: PATH + '/config/' + id,
        method: 'delete'
    })
}