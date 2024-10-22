import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
const PATH = `/financial`

// 新增账户
export function addZhanghu(data) {
    return request({
        url: PATH + '/zhanghu',
        method: 'post',
        data: data
    })
}

// 查询账户列表
export function listZhanghu(query) {
    return request({
        url: PATH + '/zhanghu/list',
        method: 'get',
        params: query
    })
}

// 获取账户详细信息
export function getZhInfo(id) {
    return request({
        url: PATH + '/zhanghu/' + id,
        method: 'get',
    })
}

// 修改账户
export function updZhanghu(data) {
    return request({
        url: PATH + '/zhanghu/update',
        method: 'post',
        data: data
    })
}

// 删除账户
export function delZhanghu(idList) {
    return request({
        url: PATH + '/cashier/zhanghu/batch/' + idList,
        method: 'delete'
    })
}