import * as util from '@/api/util'
const PATH = `/api/clinic`


const informService = {
    search(params, callback){
        util.get(PATH + `/inform`, params, callback)
    },
    readAll(params, callback){
        util.update(PATH + `/inform`, params, callback)
    },
    searchAll(params, callback){
        util.get(PATH + `/back/inform`, params, callback)
    },
    add(params, callback){
        util.put(PATH + `/back/inform`, params, callback)
    },
    delete(params, callback){
        util.del(PATH + `/back/inform`, params, callback)
    },
}

export default informService;
