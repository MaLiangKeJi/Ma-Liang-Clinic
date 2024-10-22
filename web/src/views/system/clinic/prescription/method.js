import { ElNotification } from 'element-plus'

export function checkTotalPriceNotEmpty(form) {
    if(form.totalPrice == null || form.totalPrice == undefined) {
      ElNotification({ title: '警告', message: '请填写处方【费用】！', type: 'error' })
      return true
    }
    return false
}

export function checkFilterResultNotEmpty(drugList) {
    if(notAddDrug(drugList)) {
      ElNotification({ title: '警告', message: '请【增加药品】！', type: 'error' })
      return true
    }
    return false
}

export function checkDossierIsCommit(admission, dossierId) {
    // if(admission.dossierId || dossierId.value) {
        // return false
    // } else {
        // ElNotification({ title: '警告', message: '请先填写【病历】！', type: 'error' })
        // return true
    // }
}


function notAddDrug(drugList) {
    return (drugList == null || drugList == undefined || drugList.length <= 0)
}

export function filterNullItem(tableData) {
    let drugList = []
    tableData.value.forEach(row => {
        if(row.add) {
            let item = {}
            item.stockBatchId = row.id || undefined
            Object.assign(item, row)
            drugList.push(item)
        }
    });
    return drugList
}

export function removeEmptyItem(tableData) { tableData.value.splice(tableData.value.length - 1, 1) }