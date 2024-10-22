import { dossierService as service } from '@/api/clinic/index.js'

import { getStorage, storageIsUsable } from '@/stores/clinic/case.js'


export async function verify(formEl, callback) {
    return await formEl.validate()
}

export function add(params, callback) {
    service.add(params, response => {
        callback(response)  // 调用回调函数
    })
}
export function update(params,  callback) {
    service.update(params, response => {
        callback(response)  // 调用回调函数
    })
}

export function loadStorage(form, patient) {
    if(storageIsUsable(patient)) {
        let storage = getStorage(patient)
        Object.assign(form, storage)
        if(isAlreadySubmit(storage)) switchShowMode(form)
    } else {
        switchEditMode(form)
    }
}

export function isAlreadySubmit(form) {
    return (form.id && form.id > 0)
}