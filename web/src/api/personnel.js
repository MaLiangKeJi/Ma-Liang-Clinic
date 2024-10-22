import * as util from '@/api/util'

const PATH = `/auth`


export const companyStructureService = {
    search(params, callback){
        util.get(PATH + '/company/structure/tree', params, callback)
    },
    all(params, callback){
        util.get(PATH + '/company/structure/all', params, callback)
    },
    selectOrCreateCompanyStructure(params, callback){
        util.update(PATH + '/company/structure', params, callback)
    },
}
export const companyStaffService = {
    search(params, callback){
        util.get(PATH + '/company/staff', params, callback)
    },
    join(params, callback){
        util.put(PATH + '/company/staff/invite', params, callback)
    },
}

export function searchPositionName(params, callback){
    util.get(PATH + '/staff/position', params, callback)
}