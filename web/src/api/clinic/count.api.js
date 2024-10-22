import * as util from '@/api/util'
import { getStartOfDay, getEndOfDay, formatDateToYMD } from '@/views/system/clinic/home.js'

const PATH = `/api/clinic/count`


const admissionService = {
    search(params, callback){
        util.get(PATH, params, callback)
    },
    searchCountTow(callback) {
        let now = new Date();
        let startOfDay = getStartOfDay(now);
        let endOfDay = getEndOfDay(now);
        let currentDayProceedParam = {
            current: 1,
            size: 10,
            startDate: startOfDay.getTime(),
            endDate: endOfDay.getTime(),
        }
        let params = {
            admissionLogParam: { current: 1, size: 10, state: 1, createTime: formatDateToYMD(new Date()) },
            currentDayAdmissionLogParam: { current: 1, size: 10, createTime: formatDateToYMD(new Date()) },
            currentDayProceedParam
        }

        util.update(PATH + `/tow`, params, callback)
    },
    searchJoin(params, callback){
        util.get(PATH + `/join`, params, callback)
    },
    add(params, callback){
        util.put(PATH, params, callback)
    },
    
}

export default admissionService;