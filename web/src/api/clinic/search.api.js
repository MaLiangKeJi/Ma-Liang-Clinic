import * as util from '@/api/util'

const PATH = `/api/clinic/search`

export function search(val, callback){
    util.get(PATH, { val }, callback)
}

export function searchAi(quiz, callback){
    util.get('/api/clinic/ai/cf', { quiz }, callback, false, 0)
}

export function searchUnderStock(callback){
    util.get(PATH + '/stock/under', {}, callback)
}

export function searchExpiredDrug(callback){
    util.get(PATH + '/stock/expired', {}, callback)
}