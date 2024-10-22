import * as util from '@/api/util'

export function create(params, callback) {
    util.put('/api/auth/company', params, callback, true)
}
export function search(params, callback) {
    util.get('/api/auth/company', params, callback, false)
}