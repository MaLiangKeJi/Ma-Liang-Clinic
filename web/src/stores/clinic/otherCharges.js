const local = window.localStorage

function getStorageKey(patient) {
    return ('other_charges_' + patient.id)
}
function getSubmitStateStorageKey(patient) {
    return ('other_charges_submit_state_' + patient.id)
}
export function storageIsUsable(patient) {
    if(patient && patient.id) {
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
    return JSON.parse(local.getItem(getStorageKey(patient)))
}

export function setStorage(patient, tableData, payRecordId) {
    local.setItem(getStorageKey(patient), JSON.stringify({ tableData: tableData.value, payRecordId}))
}
export function submitStateStorageIsUsable(patient) {
    if(patient && patient.id) {
        let storage = local.getItem(getSubmitStateStorageKey(patient))
        return (
            storage != null && 
            storage != undefined && 
            storage != 'null'
        )
    }
    return false
}
export function setSubmitStateStorage(patient, submitState) {
    local.setItem(getSubmitStateStorageKey(patient), JSON.stringify(submitState.value))
    return true
}

export function getSubmitStateStorage(patient) {
    return JSON.parse(local.getItem(getSubmitStateStorageKey(patient)))
}

export function delStorage(patient) {
    local.removeItem(getStorageKey(patient))
    local.removeItem(getSubmitStateStorageKey(patient))
    return true
}