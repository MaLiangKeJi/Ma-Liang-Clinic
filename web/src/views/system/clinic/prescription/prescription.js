import { prescriptionService as service } from '@/api/clinic/index.js'
import { toRaw } from '@vue/reactivity';

export function submit({ tableData, form, caseProp, patient }, callback) {
  service.add(prepareParam(form, patient, caseProp, drugList), (resp) => callback(resp))
}

export function prepareParam(
  { drugAllergy, weight, bodyTemperature, bloodPressure, bloodGlucose, totalPrice, remark }, 
  { id, patientId, payId, prescriptionId, stockBatchId }, 
  drugList,
  dossierId
) {
  return {
    id: prescriptionId,
    payId,
    stockBatchId,
    patientId, //病人编号
    dossierId,  //病例
    drugAllergyHistory: drugAllergy, //药物过敏史
    weight, //体重
    temperature: bodyTemperature, //体温
    bloodPressure, //血压
    bloodGlucose, //血糖
    price: totalPrice,  //售价
    drugList, //药品列表
    remark,  //备注
    admissionId: id //接诊日志 ID
  }
}

export function parseResponse(response) {
  let obj = {
    ...response,
    drugAllergy: response ? response.drugAllergyHistory : null, //药物过敏史
    bodyTemperature: response ? response.temperature : null, //体温
    totalPrice: response ? response.price : null,  //售价
  }
  return obj
}

export function countRowTotalNumber(row, tableData, form) {
  if(row.add) {
    let frequencyNumber = getFrequencyNumber(row) //获取【用药频次】的 code
    let period = countDays(row) // 统计【用药天数】（将周/月/年，换算为天）


    // 总量(最小单位) = 单次剂量 * 每天次数 * 天数
    row.minUnitCountNumber = row.quantity = row.singleDose * frequencyNumber * period  // 总量
    row.minUnitCountNumberUnitName = row.countNumberUnitName = row.takeNumberUnitName // 总量单位名称
    row.minUnitCountNumberUnitIdx = row.countNumberUnitIdx = 0  // 总量单位下标
    row.totalCost = (row.cost * row.minUnitCountNumber).toFixed(2) // 药品成本
    countTotalNumber(tableData, form)
  }
}

export function countTotalNumber(tableData, form) {
  // 统计单个药品总成本
  let total = parseFloat(0)
  if(tableData.length > 0) {
    tableData.forEach(item => {
      if(item.add) total += parseFloat(item.totalCost)
    });
    form.totalCost = total
  }
  return true
}

function getFrequencyNumber({ frequency }) {
  let result = 1
  switch(frequency) {
    case '一天 1 次':
      result = 1
      break;
    case '一天 2 次':
      result = 2
      break;
    case '一天 3 次':
      result = 3
      break;
  }
  return result
}

function countDays({ period, periodUnitName }) {
  let result = period
  switch(periodUnitName) {
    case '天':
      result = period * 1
      break;
    case '周':
      result = period * 7
      break;
    case '月':
      result = period * 30
      break;
    case '年':
      result = period * 365
      break;
  }
  return result
}

export function getFrequencys() {
  return [
    { value: '一天 3 次' },
    { value: '一天 1 次' },
    { value: '一天 2 次' },
  ]
}

/**
 * 计算单行药品的总价
 * 依赖 
 *  quantity:总量
 *  countNumberUnitIdx:总量单位的索引位置
 *  stockUnitList:
 *  cost:
 */
export function computeRowTotalCost(row) {
  let minUnitCountNumber = row.quantity
  for (let index = row.countNumberUnitIdx - 1; index >= 0; index--) {
    let unit = row.stockUnitList[index];
    minUnitCountNumber = minUnitCountNumber * unit.stepSize
  }
  row.totalCost = minUnitCountNumber * row.cost
  return true
}

export function searchDrugByName(queryString, callback) {
  service.searchDrug({ name: queryString}, callback)
}

export const singleDoseUnitArr = [
  { "name": "箱" },
  { "name": "盒" },
  { "name": "瓶" },
  { "name": "袋" },
  { "name": "支" },
  { "name": "片" },
  { "name": "丸" },
  { "name": "板" },
  { "name": "粒" },
  { "name": "枚" },
  { "name": "mg" },
  { "name": "g" },
  { "name": "ml" },
  { "name": "l" },
  { "name": "包" },
  { "name": "喷" },
  { "name": "颗" },
  { "name": "滴" },
  { "name": "适量" },
  { "name": "对" },
  { "name": "个" },
  { "name": "条" },
  { "name": "件" },
  { "name": "套" },
  { "name": "卷" }
]

export const useOptions = [
  { name: '口服' },
  { name: '外用' },
  { name: '静脉点滴' },
  { name: '直肠用药' },
  { name: '舌下用药' },
  { name: '静脉注射' },
  { name: '皮下注射' },
  { name: '皮内注射' },
  { name: '肌肉注射' },
  { name: '静脉滴注' },
  { name: '吸入用药' },
  { name: '喷鼻' },
  { name: '局部浸润注射（麻醉）' },
  { name: '椎管内用药' },
  { name: '关节腔内用药' },
  { name: '胸膜腔用药' },
  { name: '腹腔用药' },
  { name: '阴道用药' },
  { name: '气管镜下用药' },
  { name: '滴眼' },
  { name: '滴鼻' },
  { name: '喷喉' },
  { name: '含化' },
  { name: '敷伤口' },
  { name: '擦皮肤' },
  { name: '其他局部用药途径' },
  { name: '其他用药途径' },
  { name: '压缩雾化吸入' },
  { name: '滴耳' },
  { name: '饭前口服' },
  { name: '饭后口服' },
  { name: '湿敷' },
  { name: '坐浴' },
  { name: '喷口腔' },
  { name: '冲服' },
  { name: '塞肛用' },
  { name: '冲泡' },
  { name: '晨起口服' },
  { name: '泡腾后口服' },
  { name: '涂口腔' },
  { name: '外洗' },
  { name: '只睡前服用' },
  { name: '温水助服' },
  { name: '含服' },
  { name: '入小壶' },
  { name: '含漱' },
  { name: '穴位注射' },
  { name: '局部注射' },
  { name: '皮试' },
  { name: '脱敏注射' },
  { name: '喷患处' },
  { name: '治疗' },
  { name: '鼻腔冲洗' },
  { name: '鼻腔给药' },
  { name: '尿道灌注' }
];

export const periodUnitOptions = [
  { name: '天' },
  { name: '周' },
  { name: '月' },
  { name: '年' }
]