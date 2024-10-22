import * as util from '@/api/util'

const PATH = `/api/clinic/auxiliary/type`


const auxiliaryService = {
    search(param, callback){
        util.get(PATH, param, callback)
    },
}

export default auxiliaryService;