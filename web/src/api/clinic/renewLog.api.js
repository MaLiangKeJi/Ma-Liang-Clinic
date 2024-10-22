import * as util from '@/api/util'
const PATH = `/api/auth/renew/log`


export function search(current, size, callback) {
        util.get(PATH + `/list`, { current: current.value, size: size.value }, callback)
}