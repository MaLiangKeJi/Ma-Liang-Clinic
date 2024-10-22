import * as util from '@/api/util'
import request from '@/api/axios.js';

const PATH = `/api/clinic/settings`


const settingService = {
    add(params, callback){
        util.put(PATH, params, callback, true)
    },
    addByAxios(data) {
        return request({
            url: PATH,
            method: 'put',
            data: data,
        })
    },
    search(callback){
        util.get(PATH, {}, callback)
    },
    asyncSearch(){
        return request({
            url: PATH,
            method: 'get',
            params: {}
        })
    },
    update(params, callback){
        util.update(PATH, params, callback, true)
    },
}

export default settingService;