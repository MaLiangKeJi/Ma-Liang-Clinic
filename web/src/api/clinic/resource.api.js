import * as util from '@/api/util'

const PATH = `/auth/resource`

const resourceService = {
    search(params, callback) {
        util.get(PATH + `/list`, params, callback)
    },
    searchType(params, callback) {
        util.get(PATH + `/type/list`, params, callback)
    },
    searchTypeJoin(params, callback) {
        util.get(PATH + `/type/list/join`, params, callback)
    },
    save(params, callback) {
        util.update(PATH, params, callback, true)
    },
    batchSave(params, callback) {
        util.put(PATH + "/batch", params, callback, true)
    },
    searchUserConfig(params, callback) {
        util.get(PATH + `/config`, params, callback)
    },
    searchPage(params, callback) {
        util.get(PATH + `/page`, params, callback)
    },
    searchPageList(params, callback) {
        util.get(PATH + `/page/list`, params, callback)
    },
    searchPageTree(params, callback) {
        util.get(PATH + `/page/tree`, params, callback)
    },
    delPage(params, callback) {
        util.del(PATH + `/page`, params, callback, true)
    },
}

export default resourceService;