const local = window.localStorage

function getStorageKey(patient) {
    return ('cure_' + patient.id)
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

export function setStorage(patient, activeIndex) {
    local.setItem(getStorageKey(patient), JSON.stringify({ activeIndex }))
    return true
}

export function delStorage(patient) {
    local.removeItem(getStorageKey(patient))
    return true
}