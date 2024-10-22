import { storageIsUsable, getStorage } from '@/storage/cure'

export function loadStorage(patient, activeIndex, caseProp) {
    if(storageIsUsable(patient)) {
        let storage = getStorage(patient)
        activeIndex.value = storage.activeIndex
        caseProp.value = storage.caseProp
    }
}