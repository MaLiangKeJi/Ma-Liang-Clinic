import * as util from '@/api/util'

const PATH = `/api/clinic/pay`


const payService = {
    searchPayRecord(params, callback){
        util.get(PATH, params, callback)
    },
    search(params, callback){
        util.get(PATH + '/patient', params, callback)
    },
    searchNoPay(params, callback){
        util.get(PATH + '/all/no', params, callback)
    },
    searchIsPay(params, callback){
        util.get(PATH + '/all/is', params, callback)
    },
    searchPayBack(params, callback){
        util.get(PATH + '/all/return', params, callback)
    },
    addOtherCharge(params, callback){
        util.update(PATH + '/other', params, callback, true)
    },
    addRecord(params, callback){
        util.update(PATH, params, callback, true)
    },
    addRecordByUid(uid, params, callback) {
        util.update(PATH + "?uid=" + uid, params, callback, true)
    },
    updateRecord(params, callback){
        util.update(PATH + '/other', params, callback, true)
    },
    unPayReceipt(id, callback){
        util.update(PATH + '/receipt', { id }, callback, true)
    },
}

export default payService;
