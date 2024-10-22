import * as util from '@/api/util'
import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
const PATH = `/api/clinic/log/admission`


export default {
    search(params, callback){
        util.get(PATH + `/list`, params, callback)
    },
    searchJoin(params, callback){
        util.get(PATH + `/join`, params, callback)
    },
    add(params, callback){
        util.put(PATH, params, callback)
    },
    delete(params, callback){
        util.del(PATH, params, callback)
    },
};


export function asyncSearchJoin(params){
    return request({
        url: PATH + `/join`,
        method: 'get',
        params: params
    })
}