export const rules = {
    name: [
        { required: true, message: '必须输入【药品名称】！', trigger: 'blur' },
    ],
    type: [
        { required: true, message: '必须输入【剂型】！', trigger: 'blur' },
    ],
    spec: [
        { required: true, message: '必须输入【规格】！', trigger: 'blur' },
    ],
    drugNo: [
        { required: true, message: '必须输入【药品编码】！', trigger: 'blur' },
    ],
    approvalNumber: [
        { required: true, message: '必须输入【批准文号】！', trigger: 'blur' },
    ],
    manufacturer: [
        { required: true, message: '必须输入【生产单位】！', trigger: 'blur' },
    ],
    batchNumber: [
        { required: true, message: '必须输入【产品批号】！', trigger: 'blur' },
    ],
    produceDate: [
        { required: true, message: '必须输入【生产日期】！', trigger: 'blur' },
    ],
    expiryDate: [
        { required: true, message: '必须输入【有效期】！', trigger: 'blur' },
    ],
    sort: [
        { required: true, message: '必须输入【类型】！', trigger: 'blur' },
    ],
    skinTest: [
        { required: true, message: '必须选择【皮试】！', trigger: 'blur' },
    ],
    essential: [
        { required: true, message: '必须选择【国家基本药物】！', trigger: 'blur' },
    ],
    selectUnitIds: [
        { required: true, message: '必须选择【单位结构】！', trigger: 'blur' },
    ],
    totalCost: [
        { required: true, message: '必须输入【总进货价】！', trigger: 'blur' },
    ],
    price: [
        { required: true, message: '必须输入【零售价】！', trigger: 'blur' },
    ],
}

export function getParams(form, drugRef, unitStructureRef, usageRef, primeCostRef, outStockReminderRef) {
    let params = {
        ...drugRef.value.form,
        ...unitStructureRef.value.form,
        ...usageRef.value.form,
        ...primeCostRef.value.form,
        ...outStockReminderRef.value.form,
    }
    params.essential = params.essential
    params.countUnitId = params.countUnit
    params.remark = form.remark
    let selectUnits = []
    let priceUnitIdx
    for (let index = 0; index < params.selectUnits.length; index++) {
        let unit = params.selectUnits[index]
        selectUnits.push({ id: unit.id, stepSize: unit.number, })

        if(params.priceUnit == unit.id) {
            priceUnitIdx = index
        }
        if(index > priceUnitIdx) {
            params.price = params.price * unit.stepSize
        }
    }

    params.stockUnit = selectUnits
    params.skinTest = params.skinTest ? 1 : 0
    params.unitName = params.fastUnit.name
    params.unitId = params.fastUnit.id
    params.countUnitId = params.countUnit
    params.price = primeCostRef.value.form.price
    
    return params
}