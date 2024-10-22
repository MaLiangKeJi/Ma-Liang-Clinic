const local = window.localStorage

function getStorageKey(patient) {
    return ('prescription_' + patient.id)
}
export function storageIsUsable(patient) {
    if(patient) {
        let storage = local.getItem(getStorageKey(patient))
        return (
            storage != null && 
            storage != undefined && 
            storage != 'null'
        )
    }
    return false
}

export function getStorage(patient) {
    let storage = local.getItem(getStorageKey(patient))
    return JSON.parse(storage)
}

export function setStorage(patient, form, tableData, payRecordId, submitState) {
    local.setItem(getStorageKey(patient), JSON.stringify({ form, tableData: tableData.value, payRecordId, submitState}))
    return true
}

export function delStorage(patient, activeIndex) {
    local.removeItem(getStorageKey(patient))
    return true
}