import * as util from '@/api/util'

const PATH = `/api/clinic/proof`

const proofService = {
    add(params, callback){
        util.put(PATH, params, callback, true)
    },
    search(params, callback){
        util.get(PATH, params, callback)
    },
    getOne(params, callback){
        util.get(PATH+`/one`, params, callback)
    },
    delete(params, callback){
        util.del(PATH, params, callback, true)
    },
}

export default proofService;