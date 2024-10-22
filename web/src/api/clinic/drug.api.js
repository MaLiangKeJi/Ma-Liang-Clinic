import axios from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
import * as util from '@/api/util'

const PATH = `/api/clinic/drug`

export default {
    addPatient(params){
        return axios.put(`/api/clinic/drug`, params);
    },
    search(params, callback){
        util.get(PATH, params, callback)
    },
    getDrugArr(params, callback) {
        util.get(PATH + "/wx/" + params, {}, callback)
    },
    /**查询药房开药用的处方列表 */
    searchOpenDrug(params, callback) {
        util.get(PATH + "/open", params, callback)
    },
    getUnit(callback) {
        axios.get(`/api/clinic/drug/unit/list`).then(({status, data}) => {
            try {
                if(status === 200 && data.code === 200) {
                    callback(data.data)
                }
            } catch (error) {
                console.error(error)
            }
        })
    },
    getNoRepeatAllDefUnit(callback) {
        axios.get(`/api/clinic/drug/unit/list/all`).then(({status, data}) => {
            try {
                if(status === 200 && data.code === 200) {
                    callback(data.data)
                }
            } catch (error) {
                console.error(error)
            }
        })
    },
    getUnitTree(callback) {
        axios.get(`/api/clinic/drug/unit/trace`).then(({status, data}) => {
            try {
                if(status === 200 && data.code === 200) {
                    callback(data.data)
                }
            } catch (error) {
                console.error(error)
            }
        })
    }
};