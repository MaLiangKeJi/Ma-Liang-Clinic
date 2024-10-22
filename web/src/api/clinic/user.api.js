import * as util from '@/api/util'

const PATH = `/auth/user`


const userService = {
    search(params, callback){
        util.get(PATH, params, callback)
    },
    add(params, callback){
        util.put(PATH, params, callback, true)
    },
    login(params, callback) {
        return util.update(PATH, params, callback);
    },
    logout(callback) {
        return util.del(PATH, {}, callback);
    },
    updateConfig(params, callback) {
        return util.update(PATH + '/admin', params, callback, true);
    },
    getConfig(params, callback) {
        return util.get(PATH + '/admin', params, callback);
    },
    getResource(params, callback) {
        return util.get(PATH + '/resource/tree', params, callback);
    },
}

export default userService;