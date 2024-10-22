import axios from '@/api/axios.js'; // 导入 axios 中创建的 axios 实例
import * as util from '@/api/util'

const PATH = `/api/clinic/open`

const openPhoneService = {
    searchOpenPrescription(admissionId, callback){
        util.get(PATH + `/prescription`, { admissionId }, callback)
    },
    searchOpenClinic(openId, callback){
        util.get(PATH + `/patient/clinic`, { openId }, callback)
    },
    searchOpenAdmission(patientId, callback){
        util.get(PATH + `/patient/admission`, { patientId }, callback)
    },
    searchOpenAllPrescription(patientId, callback){
        util.get(PATH + `/patient/prescription`, { patientId }, callback)
    },
}

export default openPhoneService;