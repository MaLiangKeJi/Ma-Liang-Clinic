import * as util from '@/api/util'

const PATH = `/financial`

export default {
    search(params, callback){
        util.get(PATH + '/certificate', params, callback)
    },
    create(params, callback){
        util.post(PATH+'/product', params, callback)
    },
    update(params, callback){
        util.put(PATH+'/product', params, callback)
    },
    delete(params, callback){
        util.del(PATH+'/product', params, callback)
    },
};