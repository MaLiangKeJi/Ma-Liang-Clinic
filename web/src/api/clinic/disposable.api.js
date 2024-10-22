import * as util from '@/api/util'
const PATH = `/api/auth/disposable`

export function search(code, callback){
    util.get(PATH, { code }, callback)
}

export function finished(code, callback){
    util.put(PATH, { code }, callback)
}