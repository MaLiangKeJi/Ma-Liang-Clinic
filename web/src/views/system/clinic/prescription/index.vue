<template>
    <el-table 
      :data="tableData" 
      @cell-mouse-enter="handle.cellEnter"
      style="height: 100%; width: 100%; font-size: 1.2em;"
      :row-style="{height: '80px'}" 
    >
      <el-table-column label="药品名称" prop="name" width="400" fixed >
        <template #default="{ row, $index }">
          <div v-show="row.add" style="height: 100%; display: flex; align-items: center;">
            <el-autocomplete
              :fetch-suggestions="handle.queryDrugNameSearch"
              value-key="name"
              v-model="row.name"
              @select="(item) => handle.drugNameSelected($index, item, row)"
              clearable
              @clear="handle.clearDrugName(row.id)"
              class="inline-input w-50"
              placeholder="查询药品（别名、拼音）"
              :trigger-on-focus="true"
              :popper-append-to-body="false"
              style="height: 60%;"
            >
              <template #default="{ item }">
                <el-row :gutter="26" justify="space-between" style="width: 80vw; font-size: 1.2em; margin: 5px;" >
                  <el-col :span="4">
                    <el-text class="mx-1" :type="item.isStock ? 'success' : ''" style="font-weight: 700; font-size: 1.2em;">{{ item.name }}</el-text>
                  </el-col>
                  <el-col :span="2">
                    <el-tag :type="item.isStock ? 'success' : 'info'">{{ item.isStock ? '系统库存' : '不在系统库存' }}</el-tag>
                    <el-tag v-if="item.expiryState == 2" type="danger" style="margin-left: 5px;">即将过期</el-tag>
                  </el-col>
                  <el-col :span="2" style="text-align: right;">
                    <span style="font-weight: 700;">{{ item.dosageForm }}</span>
                  </el-col>
                  <el-col :span="4">
                    <span style="font-weight: 700;">{{ item.spec }}</span>
                  </el-col>
                  <el-col :span="2">
                    <el-text class="mx-1" type="info">{{ item.manufacturer }}</el-text>
                  </el-col>
                  <el-col :span="4" style="text-align: right;">
                    <span :class="{ read: item.expiryState == 2 }">{{ item.expiryDate ? item.expiryDate + ' 过期' : '' }}</span>
                  </el-col>
                  <el-col :span="4" style="text-align: right;"> 
                    <span v-if="item.isStock" style="font-weight: 700;" :class="{ read: item.stateCountRule != 0 && item.stockState == 1 }">{{ item.stateCountRule == 0 ? '不统计库存' : (item.stockNumber ? item.stockNumber + ' ' + item.stockNumberUnit : '库存不足') }}</span>
                  </el-col>
                </el-row>
              </template>
            </el-autocomplete>
          </div>
          <div v-show="!row.add" style="height: 100%; width: 100%;">
            <el-button type="success" @click="handle.addDrugClick(row)" style="width: 100%; height: 100%; font-size: 1.2em;">增加药品</el-button>
          </div>
        </template>
      </el-table-column>

      <!-- <el-table-column label="规格" prop="frequency" width="180">
        <template #default="{ row }">
          <div v-show="row.add" style="height: 100%; display: flex; align-items: center;">
            <el-input v-model="row.spec" style="vertical-align: top; height: 60%;" />
          </div>
        </template>
      </el-table-column> -->

      <el-table-column label="单次剂量" prop="number" width="250">
        <template #default="{ row }">
          <div v-show="row.add" style="height: 100%; display: flex; align-items: center;">
            <el-input v-model="row.singleDose" @change="handle.resetRow(row)" style="width: 40%; height: 60%; vertical-align: top;" />
            <el-autocomplete
              v-model="row.singleDoseUnit"
              :fetch-suggestions="handle.querySingleDoseUnitSearch"
              value-key="name"
              clearable
              style="width: 60%; height: 60%; margin-left: 10px;"
              @select="handle.selectSingleDoseUnit($event, row)"
            />
          </div>
        </template>
      </el-table-column>


      <el-table-column label="用法" prop="sig" width="150" style="min-width: 10px;">
        <template #default="{ row }">
          <div v-show="row.add" style="height: 100%; display: flex; align-items: center;">
            <el-autocomplete
              v-model="row.drugUsage"
              :fetch-suggestions="handle.queryUsageSearch"
              value-key="name"
              clearable
              class="inline-input w-50"
              style="width: 100%; height: 60%;"
            />
          </div>
        </template>
      </el-table-column>


      <el-table-column label="用药频次" prop="frequency" width="200" style="min-width: 250px;">
        <template #default="{ row }">
          <div v-show="row.add" style="height: 100%; display: flex; align-items: center;">
            <el-select v-model="row.frequency" @change="handle.resetRow(row)" placeholder="" style="height: 60%;">
              <el-option label="一天 3 次" value="一天 3 次" />
              <el-option label="一天 1 次" value="一天 1 次" />
              <el-option label="一天 2 次" value="一天 2 次" />
            </el-select>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="天数" prop="number" width="250">
        <template #default="{ row }">
          <div v-show="row.add" style="height: 100%; display: flex; align-items: center;">
            <el-input v-model="row.period" @change="handle.resetRow(row)" style="height: 60%; width: 60%; vertical-align: top;" placeholder="" />
            <el-autocomplete
              v-model="row.periodUnit"
              :fetch-suggestions="handle.queryPeriodUnitSearch"
              value-key="name"
              clearable
              class="inline-input w-50"
              style="height: 60%; width: 60%; margin-left: 10px"
            />
          </div>
        </template>
      </el-table-column>

      <!-- <el-table-column label="总量" prop="number" width="250">
        <template #default="{ row }">
          <div v-show="row.add" style="height: 100%; display: flex; align-items: center;">
            <el-input v-model="row.quantity" @change="handle.countNumberChange(row)"  style="height: 60%; width: 30%; vertical-align: top;" />
            <el-autocomplete
              v-model="row.quantityUnit"
              :fetch-suggestions="handle.queryUnitSearch"
              @select="handle.countNumberUnitNameClick(row, unit, index)"
              value-key="name"
              clearable
              class="inline-input w-50"
              style="height: 60%; width: 60%; margin-left: 10px"
            />
          </div>
        </template>
      </el-table-column> -->

      <el-table-column label="成本" prop="date" width="100" >
        <template #default="{ row }">
          <div v-if="row.add && row.id" v-show="isShowCost" style="height: 100%; display: flex; align-items: center;">
            {{ 
              (row.takeNumberUnitName ? '每' + row.takeNumberUnitName + ' ' : '') + 
              (row.cost) 
            }} 元
          </div>
        </template>
      </el-table-column>

      <el-table-column label="总成本" prop="date" width="200">
        <template #default="{ row }">
          <div v-if="row.add && isShowCost && row.id" style="height: 100%; display: flex; align-items: center;">
            <span>共 {{ row.totalCost }} 元</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="库存" prop="date" width="100">
        <template #default="{ row }">
          <div v-if="row.add && row.id" style="height: 100%; display: flex; align-items: center;">
            {{ (row.stockNumber || 0) + '' + (row.stockNumberUnit != undefined ? row.stockNumberUnit : '个') }}
          </div>
        </template>
      </el-table-column>

      <el-table-column fixed="right" width="100">
        <template #default="{$index, row}">
          <div style="height: 100%; display: flex; align-items: center;">
            <el-button v-if="row.add" @click="handle.delRow($index, row.id)" :disabled="tableData.length <= 2" type="danger">
              删除
            </el-button>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="date">
        <template #default="{ row }">
          <div v-show="row.add && row.edit" style="height: 100%; display: flex; align-items: center;">
            <el-input v-model="row.remark" style="vertical-align: top; height: 60%;" />
          </div>
          <div v-show="row.add && (!row.edit)">{{ row.remark }}</div>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="unitSelectAbnormal">
      <template #header>
        <el-text class="mx-1" type="warning" style="font-size: 1.5em;">警告</el-text>
      </template>
      <span style="font-size: 1.3em;">
        选择的药品【单次剂量 - 单位】，与库存登记的【单位结构】不符，是否依旧使用？
      </span>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="unitSelectAbnormal = false">取消</el-button>
          <el-button type="danger">继续</el-button>
        </span>
      </template>
    </el-dialog>

    
</template>
  
<script setup>
import { ref, reactive, defineProps, onMounted, defineExpose, watch, defineEmits, } from 'vue'
import { prescriptionService as service } from '@/api/clinic/index.js'
import { 
  countRowTotalNumber, countTotalNumber, 
  getFrequencys, computeRowTotalCost,
  searchDrugByName, prepareParam, parseResponse,
  singleDoseUnitArr, useOptions, periodUnitOptions
} from '@/views/system/clinic/prescription/prescription.js'
import { 
  checkTotalPriceNotEmpty, 
  checkDossierIsCommit, 
  checkFilterResultNotEmpty, 
  filterNullItem,
} from './method'

// import { ElNotification } from 'element-plus'

const props = defineProps(['admission', 'isShowCost'])
const { admission } = props
const isShowCost = ref(props.isShowCost)
watch(() => props.isShowCost, val => isShowCost.value = val)

const event = defineEmits(['submit','update', 'changeTotalCase', 'updPriceByTotal'])
const tableItemInitVal = {
  name: "",
  frequency: '',
  number: null,
  add: false,
  edit: true,
  takeNumber: 1,
}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
const tableData = ref([{ ...tableItemInitVal, add: true }, { ...tableItemInitVal }])
const unitSelectAbnormal = ref(false)
const liteDrugByIdOfPrice = ref(new Map());/**用于计算费用的[药品id和简化药品信息]映射*/



const form = reactive({})

const isCommit = () => {
  return admission.prescriptionId != null || form.id != null
}

const dossierId = ref()
const payState = ref(false)


const method = {
  initRow(row) {
    row.period = 1
  },
  checkSingleDoseUnitIsAbnormal(row) {
    if(row.id) {
      if(row.singleDoseUnit != undefined && row.singleDoseUnit != '') {
        row.units.forEach(unit => {
          if(row.singleDoseUnit == unit.name) {
            unitSelectAbnormal.value = false
            return
          }
        })
        unitSelectAbnormal.value = true
      }
      unitSelectAbnormal.value = false
    }
  },
}

const totalCost = ref(0) 

const handle = {
  periodUnitNameChange(row, tableData, form) {
    this.resetRow(row)
  },
  addDrugClick(row) {
    row.add = true
    addPrescription()
  },
  drugNameSelected(index, item, row) {
    Object.assign(row, item)
    row.period = 1
    row.periodUnit = '天'
    
    if(row.isStock) {
      row.quantityUnit = row.minUnit.name
      row.singleDoseUnit = row.minUnit.name

      let cost = form.totalCost + row.cost
      form.totalCost = Math.floor(cost * 100) / 100
      this.resetRow(row)
      method.checkSingleDoseUnitIsAbnormal(row)
    } else {
      row.id = undefined
    }
    if(row.singleDose == undefined) row.singleDose = 1
    if(row.singleDoseUnit == undefined || row.singleDoseUnit == '') row.singleDoseUnit = '片'
    if(row.drugUsage == undefined || row.drugUsage == '') row.drugUsage = '口服'
    if(row.frequency == undefined || row.frequency == '') row.frequency = '一天 3 次'
    if(row.quantity == undefined) row.quantity = 1
    if(row.quantityUnit == undefined || row.quantityUnit == '') row.quantityUnit = '片'
    method.initRow(row)
  },
  onSubmit() {
    if(checkDossierIsCommit(admission, dossierId)) return
    if(checkTotalPriceNotEmpty(form)) return
    let filterResult = filterNullItem(tableData)
    if(checkFilterResultNotEmpty(filterResult)) return
    service.add(prepareParam(form, admission, filterResult, (admission.dossierId ? admission.dossierId : dossierId.value)), (response) => {
      let { prescriptionId, payId } = response
      form.id = prescriptionId 
      form.prescriptionId = prescriptionId
      form.payId = payId
      event('submit', response)
    })
  },
  onUpdate() {
    if(checkDossierIsCommit(admission, dossierId)) return
    if(checkTotalPriceNotEmpty(form)) return
    let filterResult = filterNullItem(tableData)
    if(checkFilterResultNotEmpty(filterResult)) return
    service.update(prepareParam(form, admission, filterResult, (admission.dossierId ? admission.dossierId : dossierId.value)), (response) => {
      event('update', response)
    })
  },
  cellEnter(row, column, cell, event) { row.isEdit = true },
  queryDrugNameSearch(queryString, cb) { searchDrugByName(queryString != null && queryString != 'null' ? queryString : undefined, res => cb(res)) },
  queryFrequencySearch(queryString, cb) { cb(getFrequencys()) },
  queryUsageSearch(queryString, cb) { 
    if(queryString == undefined || queryString == '') {
        cb(useOptions)
        return
    }
    let result = useOptions.filter(unit => {
      return unit.name.indexOf(queryString) != -1
    })
    cb(result.length > 0 ? result : useOptions)
  },
  queryPeriodUnitSearch(queryString, cb) { 
    if(queryString == undefined || queryString == '') {
        cb(periodUnitOptions)
        return
    }
    let result = periodUnitOptions.filter(unit => {
      return unit.name.indexOf(queryString) != -1
    })
    cb(result.length > 0 ? result : periodUnitOptions)
  },
  queryUnitSearch(queryString, cb) { service.searchUnit({ name: queryString != null && queryString != 'null' ? queryString : undefined, }, response => cb(response)) },
  querySingleDoseUnitSearch(queryString, cb) {
    if(queryString == undefined || queryString == '') {
        cb(singleDoseUnitArr)
        return
    }
    let result = singleDoseUnitArr.filter(unit => {
      return unit.name.indexOf(queryString) != -1
    })
    cb(result.length > 0 ? result : singleDoseUnitArr)
  },
  /**
   * 修改【总量】时，重新统计
   */
  countNumberChange(row) { computeRowTotalCost(row) && countTotalNumber(tableData.value, form) },
  countNumberUnitNameClick(row, unit, index) {
    row.countNumberUnitIdx = index
    row.countNumberUnit = unit
    this.countNumberChange(row)
  },
  selectSingleDoseUnit(item, row) {
    method.checkSingleDoseUnitIsAbnormal(row)
  },
  computeTotalCost() {
    let cost = 0
    tableData.value.forEach(row => {
      if(row.add) {
        cost += parseFloat(row.totalCost)
      }
    })
    totalCost.value = cost;
  },
  resetRow(row) {
    countRowTotalNumber(row, tableData.value, form)
    this.computeTotalCost()
    event('changeTotalCase', totalCost.value)
    putDrugByCalPrice(row);
  },
  delRow(index, id) {
    tableData.value.splice(index, 1);
    delDrugBySystemStock(id);
  },
  clearDrugName(id) {
    delDrugBySystemStock(id);
  }
}

/**往计算费用的集合里新增药品 */
function putDrugByCalPrice(row) {
  let liteDrugByPrice = {
    price: row.price,
    priceUnit: row.priceUnit,
    quantity: row.quantity,
    stockUnitList: row.stockUnitList,
    minUnit: row.minUnit
  };

  liteDrugByIdOfPrice.value.set(row.id, liteDrugByPrice);
  //更新费用
  event('updPriceByTotal', [...liteDrugByIdOfPrice.value.values()]);
}

function delDrugBySystemStock(id) {
  //系统库存有的药品更新费用
  if (id != undefined) {
    liteDrugByIdOfPrice.value.delete(id);
    //更新费用
    event('updPriceByTotal', [...liteDrugByIdOfPrice.value.values()]);
  }
}



const addPrescription = () => { tableData.value.push({ ...tableItemInitVal })}

onMounted(() => {
  let flag = false
  if(admission.prescriptionId) {
    Object.assign(form, parseResponse(admission.prescription))
    // if(isCommit()) {
      tableData.value = admission.prescriptionDrugs
      tableData.value.forEach(row => {
        row.stockNumber = row.number
        if(row.unit && row.unit.length > 0) {
          row.stockNumberUnit = row.unit && row.unit[row.unit.length -1]
        }
        if(row.name && row.name.length > 0){
          row.add = true
          flag = true
        }else{
          row.add = false
          flag = false
        }
        countRowTotalNumber(row, tableData.value, form)
      });
    // }
  }
  
  if(flag) {
    addPrescription()
  }
})
function execVerify() {
  let filterResult = filterNullItem(tableData)
  return prepareParam(form, admission, filterResult, (admission.dossierId ? admission.dossierId : dossierId.value))
}
defineExpose({
  setDossierId(id) {
    dossierId.value = id
  },
  setPayState(state) {
    payState.value = state
  },
  execVerify,
})
</script>

<style scoped>
.vue-office-pdf-wrapper {
  padding-top: 0px !important;
  padding-bottom: 0px !important;
  background:#dadada !important;
}
.docx-wrapper{
  background:#dadada !important;
}
docx-wrapper>section.docx {
  box-shadow: none !important;
}

.el-dialog__body {
  padding: 0px !important;
}

.el-table__row {
  height: 80px !important;
  max-height: 80px;
}

.table_font {
  /* height: 80px; */
  font-size: 1.4em;
  font-weight:bolder;
}

.el-input {
  font-size: 1.2em;
}

.el-table__row {
  height: 55px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 90%;
  margin-bottom: 1%;
  margin-left: 5%;
}
.el-card__body {
  height: 100%;
}

/* 患者信息布局样式 */
.el-row {
  margin-bottom: 20px;
}
.el-row:last-child {
  margin-bottom: 0;
}
.el-col {
  border-radius: 4px;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}

.el-space__item {
  line-height: 1.6;
  font-family: Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Fira Sans', 'Droid Sans', 'Helvetica Neue', sans-serif;
  font-size: 15px;
  text-rendering: optimizeLegibility;
  font-weight: normal;
}
.read {
  color: red
}
:deep(td){
  height: 100%;
}
:deep(.cell) {
  height: 100%;
}
:deep(.el-autocomplete>.el-input) {
  height: 100% !important;
}
:deep(.el-select__wrapper) {
  height: 100%;
  font-size: 1.2em;
}
</style>
