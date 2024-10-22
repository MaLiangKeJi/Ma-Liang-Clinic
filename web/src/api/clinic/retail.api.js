import * as util from '@/api/util'

const PATH = `/api/clinic/retail`


const retailService = {
    add(params, callback){
        util.put(PATH, params, callback, true)
    },
    search(params, callback){
        util.get(PATH, params, callback)
    },
    searchList(params, callback){
        util.get(PATH + '/list', params, callback)
    },
}

export default retailService;