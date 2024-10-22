import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
const PATH = `/api/clinic/reception`

export function receptionOver(data){
    return request({
        url: PATH,
        method: 'put',
        data
    })
}