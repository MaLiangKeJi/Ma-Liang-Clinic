import { storageIsUsable, getStorage, submitStateStorageIsUsable, getSubmitStateStorage } from '@/storage/otherCharges'

export function loadStorage(patient, tableData, submitState) {
    if(storageIsUsable(patient)) {
        let storage = getStorage(patient)
        tableData.value = storage.tableData
        tableData.value.splice(tableData.value.length - 1, 1)
    } else {
        tableData.value = [{ add: true }, {}]
    }

    submitState.value = submitStateStorageIsUsable(patient) ? getSubmitStateStorage(patient) : false
}