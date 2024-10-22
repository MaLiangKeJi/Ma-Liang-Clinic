const local = window.localStorage

function getStorageKey(patient) {
    return ('other_charges_' + patient.id)
}
export function storageIsUsable(patient) {
    let storage = local.getItem(getStorageKey(patient))
    return (
        storage != null && 
        storage != undefined && 
        storage != 'null'
    )
}

export function getStorage(patient) {
    return JSON.parse(local.getItem(getStorageKey(patient)))
}

export function setStorage(patient, tableData, payRecordId) {
    local.setItem(getStorageKey(patient), JSON.stringify({ tableData: tableData.value, payRecordId }))
    return true
}

export function delStorage(patient) {
    local.removeItem(getStorageKey(patient))
    return true
}