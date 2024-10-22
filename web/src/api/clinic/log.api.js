import * as util from '@/api/util'

const PATH = `/api/clinic/log/operation`

export default {
    search(params, callback){
        util.get(PATH, params, callback)
    },

    searchOperationList(params, callback){
        util.get(PATH + '/list', params, callback)
    },
}
