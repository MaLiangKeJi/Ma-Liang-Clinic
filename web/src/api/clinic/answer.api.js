import * as util from '@/api/util'
const PATH = `/answer/api/v1`

export default {
    search(params, callback){
        util.get(PATH + `/search`, params, callback)
    },
};