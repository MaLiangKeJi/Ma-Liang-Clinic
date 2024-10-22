const local = window.localStorage

function getStorageKey(patient) {
    return (patient && patient.id) ? ('case_' + patient.id) : null
}
export function storageIsUsable(patient) {
    let storage = local.getItem(getStorageKey(patient))
    return (
        storage && 
        storage != 'null'
    )
}
export function getStorage(patient) {
    let storage = local.getItem(getStorageKey(patient))
    return JSON.parse(storage)
}

export function setStorage(patient, obj) {
    local.setItem(getStorageKey(patient), JSON.stringify(obj))
    return true
}

export function delStorage(patient) {
    local.removeItem(getStorageKey(patient))
    return true
}