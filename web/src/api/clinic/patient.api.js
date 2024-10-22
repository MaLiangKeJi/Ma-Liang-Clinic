import * as util from '@/api/util'

const PATH = `/api/clinic/patient`

const patientService = {
    addPatient(params, callback){
        util.put(PATH, params, callback, true)
    },
    selectPatient(params, callback){
        util.get(PATH, params, callback)
    },
}

export default patientService;
