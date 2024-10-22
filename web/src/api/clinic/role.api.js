import * as util from '@/api/util'

const PATH = `/auth/role`


const roleService = {
    search(params, callback){
        util.get(PATH, params, callback)
    },
    add(params, callback){
        util.put(PATH, params, callback, true)
    },
    batchAddResourcePermission(params, callback){
        util.put(PATH + "/resource/batch", params, callback, true)
    },
    searchJoin(params, callback){
        util.get(PATH + '/resource', params, callback)
    },
    batchAddGroup(params, callback){
        util.update(PATH + '/group/batch', params, callback, true)
    },
}

export default roleService;