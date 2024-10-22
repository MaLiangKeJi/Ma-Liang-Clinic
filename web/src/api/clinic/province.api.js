import * as util from '@/api/util'
const PATH = `/api/auth/province`

export function searchProvince(params, callback){
    util.get(PATH, params, callback)
}