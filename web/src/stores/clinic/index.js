import { delStorage as delCaseStorage } from '@/storage/case'
import { delStorage as delOtherChargesStorage } from '@/storage/otherCharges'
import { delStorage as delThisChargeStorage } from '@/storage/thisCharge'
import { delStorage as delCureStorage } from '@/storage/cure'
import { delStorage as delPrescriptionStorage } from '@/storage/prescription'

export function cleanPatientStorage(patient) {
    delCaseStorage(patient)
    delThisChargeStorage(patient)
    delCureStorage(patient)
    delPrescriptionStorage(patient)
    return true
}