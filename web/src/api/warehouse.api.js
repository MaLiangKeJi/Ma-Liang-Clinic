import * as util from '@/api/util'

const PATH = `/store-manage`


const wareService = {
    search(params, callback){
        util.get(PATH + '/warehouse/page', params, callback)
    },
    searchSimpleList(params, callback){
        util.get(PATH +'/warehouse/simple', params, callback)
    },
    create(params, callback){
        util.put(PATH+'/warehouse', params, callback)
    },
    update(params, callback){
        util.update(PATH+'/warehouse', params, callback)
    },
    delete(params, callback){
        util.del(PATH+'/warehouse', params, callback)
    },
}

export default wareService;