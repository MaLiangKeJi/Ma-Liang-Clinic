import * as util from '@/api/util'

const PATH = `/api/clinic/dossier`

export default {
    add(params, callback){
        util.put(PATH, params, callback, true)
    },
    update(params, callback){
        util.update(PATH, params, callback, true)
    },
    search(params, callback){
        util.get(PATH, params, callback)
    },
}
