import axios from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
import * as util from '@/api/util'

const PATH = `/api/clinic/prescription`

const prescriptionService = {
    add(params){
        return axios.put(PATH, { data: params })
    },
    selectPrescription(params){
        return axios.get(`/api/clinic/prescription`, params);
    },
    getFile(params, callback) {
        util.get("/api/clinic/prescription/file", params, callback)
    },
    update(params, callback){
        util.update(PATH, params, callback, true)
    },
    searchDrug(params, callback){
        util.get(PATH + `/drug`, params, callback)
    },
    searchUsage(params, callback){
        util.get(PATH + `/usage/list`, params, callback)
    },
    searchUnit(params, callback){
        util.get(PATH + `/unit/list`, params, callback)
    },
    searchTemplateName(params, callback){
        util.get(PATH + `/template/name/list`, params, callback)
    },
}

export default prescriptionService;