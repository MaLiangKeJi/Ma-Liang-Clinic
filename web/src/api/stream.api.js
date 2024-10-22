import * as util from '@/api/util'

const PATH = `/stream-manage/stream`

const streamService = {
    search(params, callback) {
        util.get(PATH + '/page', params, callback)
    },
    create(params, callback) {
        util.put(PATH + '/create', params, callback)
    },
    shenpi(params, callback) {
        util.get(PATH + '/update', params, callback)
    },
}

export default streamService;