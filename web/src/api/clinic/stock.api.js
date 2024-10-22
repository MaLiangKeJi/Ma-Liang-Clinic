import * as util from '@/api/util'
import request from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
import qs from 'qs';

const PATH = `/api/clinic/stock`


const stockService = {
    add(params, callback){
        util.put(PATH, params, callback, true)
    },
    search(params, callback){
        util.get(PATH, params, callback)
    },

    searchStock(params, callback){
        util.get(PATH+'/only', params, callback)
    },
    getRole(callback) {
        util.get(`/api/clinic/stock/role`, {}, callback)
    },
    searchStockIn(query) {
        return request({
            url: PATH + '/in',
            method: 'get',
            params: query,
            paramsSerializer: params => qs.stringify(query)
          })
    },
    addNumber(params, callback){
        util.update(PATH, params, callback, true)
    },
    removeStock(params, callback){
        util.del(PATH + '/'+ params, params, callback, true)
    },
}

export default stockService;