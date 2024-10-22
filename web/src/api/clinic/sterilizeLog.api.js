import * as util from '@/api/util'

const PATH = `/api/clinic/log/sterilize`


const sterilizeService = {
    search(params, callback){
        util.get(PATH, params, callback)
    },
    add(params, callback){
        util.put(PATH, params, callback, true)
    },
}

export default sterilizeService;