<template>
  <el-skeleton v-if="!(admissionLog && admissionLog.patient)" :rows="10" animated />
  <div v-if="admissionLog.patient" style="height: 100%; padding: 2% 1%; z-index: 2021;">
      <div style="height: 15%; width: 100%; padding: 1%;">
        <el-card style="height: 100%; width: 100%; border-radius: 15px;">
          <div style="height: 100%; display: flex; align-items: center; padding: 0 2%;">
            <div style="width: 56px; margin-right: 20px;">
              <el-avatar shape="circle" size="large" :src="squareUrl" />
            </div>
            <div style="display: flex; flex-direction: column; justify-content: space-between;">
              <span>
                <el-text class="mx-1" style="font-size: 1.2em; font-weight: 700;">{{ admissionLog.patient.name }}</el-text>
              </span>
              <span>
                <el-text class="mx-1" type="info">{{ admissionLog.patient.phone }}</el-text>
              </span>
            </div>
            <el-divider direction="vertical" style="margin: 0 40px; height: 40px;" />
            <div>
              {{ admissionLog.patient.sex == 0 ? '女':'男' }}
            </div>
            <el-divider direction="vertical" style="margin: 0 40px; height: 40px;" />
            <div>
              {{ admissionLog.patient.age + '岁' }}
            </div>
            <el-divider direction="vertical" style="margin: 0 40px; height: 40px;" />
            <div>
              {{ admissionLog.patient.isFirst == 0 ? '初诊':'复诊' }}
            </div>
          </div>
        </el-card>
      </div>
      <el-scrollbar style="width: 100%; height: 85%; padding: 0 1%">
        <div style="display: flex; height: 80%;">
          <el-card class="card" style="height: 100%; border-color: #e7e8e7; width: 60%; margin-right: 1%;">
            <template #header>
              <div class="card-header" style="height: 100%; padding: 1% 3%;">
                <span style="font-size: 1.2em; font-weight: 700;">病例</span>
              </div>
            </template>
            <el-scrollbar style="width: 100%; height: 100%;" always>
              <div style="height: 100%; padding: 1% 0;">
                <CaseTab
                  ref="caseTabRef"
                  :admission="admissionLog" 
                  @submit="handle.caseSubmitOrUpdate" 
                  @update="handle.caseSubmitOrUpdate" 
                />
              </div>
            </el-scrollbar>
          </el-card>
          <el-card class="card" style="border-color: #e7e8e7; width: 40%; height: 100%;"
          >
            <template #header>
              <div class="card-header" style="height: 100%; padding: 1% 3%;">
                <span style="font-size: 1.2em; font-weight: 700;">就诊记录</span>
              </div>
            </template>
            <div style="height: 100%; padding: 5% 0;">
              <div style="height: 100%;">
                <el-scrollbar height="100%">
                  <HistoryTab :patient="admissionLog.patient" />
                </el-scrollbar>
              </div>
            </div>
          </el-card>
        </div>
        <div v-show="isPrescription" style="height: 80%; margin: 1% 0 2% 0;">
          <el-card class="prescription_card" style="height: 100%; border-radius: 15px;">
            <template #header>
              <div class="card-header" style="height: 100%; padding: 1% 1.5%; display: flex; justify-content: space-between;">
                <span style="font-size: 1.2em; font-weight: 700;">处方</span>
                <el-popover
                  placement="top-start"
                  title="进货价格"
                  :width="300"
                  trigger="hover"
                  content="隐藏/显示，药品的【进货价格】"
                >
                  <template #reference>
                    <el-button @click="isShowCost = !isShowCost" link>
                      <el-icon style="font-size: 3em;">
                        <View v-show="isShowCost" />
                        <Hide v-show="!isShowCost" />
                      </el-icon>
                    </el-button>
                  </template>
                </el-popover>
              </div>
            </template>
            <div style="height: 100%; padding: 2% 2% 0 2%;">
              <PrescriptionTab 
                ref="prescriptionRef" 
                :isShowCost="isShowCost"
                :admission="admissionLog" 
                @submit="handle.prescriptionSubmitOrUpdate" 
                @update="handle.prescriptionSubmitOrUpdate" 
                @changeTotalCase="cost => totalCost = cost"
                @updPriceByTotal='handle.outCalPrice'
              />
            </div>
            <template #footer>
              <div style="height: 50%; text-align: right; margin-right: 25%;">
                  <span v-show="isShowCost" style="margin-right: 10px; font-size: 1.2em;">
                    成本：{{ totalCost || 0 }}
                    <span>元</span>
                  </span>
                  <span style="margin: 0px 10px; font-size: 1.2em;">费用</span>
                  <el-input-number v-model="price" :min="0" style="height: 50%; width: 200px;" class="price_input" />
                  <span style="margin-left: 10px; font-size: 1.2em;">元</span>
              </div>
              <div style="height: 50%; text-align: center">
                <el-button @click="() => handle.toPay()" type="primary" style="width: 10%; height: 50%; font-size: 1.5em;" >
                  <span v-if="admissionLog.isPharmacyPay==0">
                    收费
                  </span>
                  <span v-if="admissionLog.isPharmacyPay==1">
                    下一步
                  </span>
                </el-button>
              </div>
            </template>
          </el-card>
        </div>
        <div v-show="!isPrescription" style="height: 80%; margin: 1% 0 2% 0;">
          <PayTab 
              ref="payRef"
              :totalPrice="price"
              :admission="admissionLog"
              @back="() => isPrescription = true"
          />
        </div>
      </el-scrollbar>

      <AddStockDrugDialog ref="addStockDrugDialog"/>

  </div>


</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
const routeInstance = useRoute()

import { CloseBold, View, Hide } from '@element-plus/icons-vue'

import CaseTab from '@/views/system/clinic/case/index.vue'
import PrescriptionTab from '@/views/system/clinic/prescription/index.vue'
import HistoryTab from './components/history/index.vue'
import PayTab from './components/thisCharge.vue'
import admissionService from '@/api/clinic/admission.api.js'
import auxiliaryService from '@/api/clinic/auxiliary.api.js'
import prescriptionService from '@/api/clinic/prescription.api'
import { initAuxiliaryExaminationOptions } from './index.js'
import { receptionOver } from '@/api/clinic/reception.api.js'
import { ElNotification } from 'element-plus'

import AddStockDrugDialog from './dialog/AddStockDrugDialog.vue'

const addStockDrugDialog = ref(null)
function openPrescriptDialog(noStockDrugList) {
    //调用openDialog方法
    addStockDrugDialog.value.openDialog(noStockDrugList)
}


const caseTabRef = ref()

const isShowCost = ref() //是否显示成本
const totalCost = ref(0)

// 就诊记录

// 辅助检查
const auxiliaryExaminationOptions = ref(initAuxiliaryExaminationOptions) // 【辅助检查】参数
const isFocusAuxiliaryExamination = ref(false)  // 鼠标是否聚焦【辅助检查】
const isHideAuxiliaryExaminationEditBtn = ref(false)  // 是否隐藏【辅助检查】的编辑按钮

// 病人信息
const squareUrl = ref('https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png')

const admissionLog = reactive({
  patientId: undefined,
  userId: undefined,
  dossierId	: undefined,
  prescriptionId: undefined,
  payId: undefined,
  pay: {
    state: 0,
  }
})
const price = ref(0)
const remark = ref()

watch(() => routeInstance.params.admissionLogId, id => admissionLog.id = id, { immediate: true })

const activeIndex = ref("case")

const isPrescription = ref(true)
const prescriptionProp = ref({})
const casePayRecordIdProp = ref(-1)
const otherChargeProp = ref({})
const otherChargePayRecordIdProp = ref({})

const prescriptionRef = ref()
const thisChargeRef = ref()

const handle = {
  async toPay() {
    ElNotification({ title: '提示', message: '保存中...', type: 'info', })
    let caseForm = await caseTabRef.value.execVerify()
    let prescriptionForm = prescriptionRef.value.execVerify()
    if(caseForm && prescriptionForm) {
      let flag = true;
      let drugList = []
      let noStockDrugList = []
        prescriptionForm.drugList.forEach(drug => {
          if(drug.isStock==null&&drug.cost==null){//不在库存中的药品，不管
          }else if(drug.stockBatch!=null&&(drug.quantity > drug.stockBatch.number)||drug.stockBatch==null&&(drug.quantity > drug.stockNumber)){
            ElNotification({ title: '无法提交', message: '【库存不足】请【更换药品】', type: 'error', })
            noStockDrugList.push({drug})
            flag = false
            return
          }
          if(drug.name==null){
            ElNotification({ title: '无法提交', message: '【未选择】请【选择药品】', type: 'error', })
            flag = false
            return
          }
          drugList.push({
            "stockBatchId": drug.stockBatchId || undefined,
            "name": drug.name,
            "spec": drug.spec,
            "singleDose": drug.singleDose,
            "singleDoseUnit": drug.singleDoseUnit,
            "drugUsage": drug.drugUsage,
            "frequency": drug.frequency,
            "quantity": drug.quantity,
            "quantityUnit": drug.quantityUnit,
            "period": drug.period,
            "periodUnit": drug.periodUnit,
            "manufacturer": drug.manufacturer,
            "cost": drug.cost,
            "price": drug.price,
            "remark": drug.remark
          })
        })
        let param = {
          admissionIds: admissionLog.id,
          dossier: caseForm,  //病例
          prescription: {     //处方
            price: price.value ? price.value : 0,
            remark: remark.value,
            drugList,
          }
        }
        // 准备【辅助检查】参数：过滤、数组转json
        let filterAuxiliaryExaminationOptions = auxiliaryExaminationOptions.value.filter(auxiliaryExaminationOption => {
          let { name, value } = auxiliaryExaminationOption
          if(name == '体重' || name == '体温' || name == '血压' || name == '身高') {
              if(value == "0") {
                return false  //排除掉默认的辅助检查项（前提是未修改）
              }
          }
          return true
        })
        if(filterAuxiliaryExaminationOptions.length > 0) {
          param.dossier.auxiliarycheckup = JSON.stringify(filterAuxiliaryExaminationOptions)
        }
        
        //判断库存不足的药品列表是否为空，不为空则展示添加药品的弹窗，临时添加和修改库存
        if(noStockDrugList.length > 0) {
          // 显示【添加药品】弹窗
          openPrescriptDialog(noStockDrugList)
        }
        if(flag){
          let res = await receptionOver(param)
          if(res.data) {
            let { code, msg } = res.data
            if(code == 200) {
              ElNotification({ title: '提示', message: '保存成功！', type: 'success', })
              method.search()
              // 跳转【收费记录】
              isPrescription.value = false
            }
          }
        }
    }
  },
  caseSubmitOrUpdate(id) {
    prescriptionRef.value.setDossierId(id)
    method.toPrescription()
  },
  prescriptionSubmitOrUpdate(response) {
    if(response) {
      let { prescriptionId, payId } = response
      admissionLog.prescriptionId = prescriptionId
      admissionLog.payId = payId
    }
    if(admissionLog.payId) thisChargeRef.value.reload(admissionLog.payId)
    method.toOtherCharges()
  },
  async onSubmit() {
    // 校验并获取病例信息
    let caseForm = await caseTabRef.value.execVerify()
    if(caseForm) {
      // 获取处方信息
      let prescriptionForm = prescriptionRef.value.execVerify()
      if(prescriptionForm) {
        let drugList = []
        prescriptionForm.drugList.forEach(drug => {
          drugList.push({
            "stockBatchId": drug.stockBatchId || undefined,
            "name": drug.name,
            "spec": drug.spec,
            "singleDose": drug.singleDose,
            "singleDoseUnit": drug.singleDoseUnit,
            "drugUsage": drug.drugUsage,
            "frequency": drug.frequency,
            "quantity": drug.quantity,
            "quantityUnit": drug.quantityUnit,
            "period": drug.period,
            "periodUnit": drug.periodUnit,
            "manufacturer": drug.manufacturer,
            "cost": drug.cost,
            "price": drug.price,
            "remark": drug.remark
          })
        })
        let param = {
          admissionLogId: admissionLog.id,
          prescriptionId: admissionLog.prescriptionId,
          payId: admissionLog.payId,
          dossier: caseForm,
          price: price.value ? price.value : 0,
          remark: remark.value,
          drugList,
        }
        // 准备【辅助检查】参数：过滤、数组转json
        let filterAuxiliaryExaminationOptions = auxiliaryExaminationOptions.value.filter(auxiliaryExaminationOption => {
          let { name, value } = auxiliaryExaminationOption
          if(name == '体重' || name == '体温' || name == '血压' || name == '身高') {
              if(value == "0") {
                return false  //排除掉默认的辅助检查项（前提是未修改）
              }
          }
          return true
        })
        if(filterAuxiliaryExaminationOptions.length > 0) {
          param.dossier.auxiliarycheckup = JSON.stringify(filterAuxiliaryExaminationOptions)
        }

        let result = await prescriptionService.add(param)
        if(result.data.data && result.data.data == true) {
          method.search()
          // 跳转【收费记录】
          isPrescription.value = false
        }
      }
    }
  },
  outCalPrice(liteDrugArr) {
    price.value = 0;

    //无药品直接返回
    if (liteDrugArr.length < 1)
      return;

    liteDrugArr.forEach(drug => {
      if(drug.priceUnit==drug.minUnit.id){//说明售价单位是最小单位
        price.value = price.value+(drug.price * drug.quantity)
      }else {//说明售价单位需要转换为最小单位
        let num = 1
          drug.stockUnitList.forEach(unit => {
            if(unit.id >= drug.priceUnit){
              num*=unit.stepSize
            }
          })
          price.value = price.value+ (drug.price/num) * drug.quantity
      }
    })
  }
}
const method = {  
  search() {
      admissionService.searchJoin({ id: admissionLog.id }, (response) => {
        Object.assign(admissionLog, response)
        price.value = admissionLog.payRecords?admissionLog.payRecords.fee:0
        document.title = admissionLog.patient.name
      })
  },
  toPrescription() {
    activeIndex.value = 'prescription'
  },
  toOtherCharges(params, payRecordId) {
      if(params) prescriptionProp.value = params
      if(payRecordId) casePayRecordIdProp.value = payRecordId
      activeIndex.value = 'otherCharges'
  },
  toCharge(params, payRecordId) {
      if(payRecordId) { otherChargePayRecordIdProp.value = payRecordId }
      if(params) otherChargeProp.value = params
      activeIndex.value = 'charge'
      return true
  },
  toChargeRecord() {
      activeIndex.value = 'chargeRecord'
  },
}

onMounted(() => {
  method.search()
})
function queryAuxiliaryExaminationUnitSearch(val, cb) {
  auxiliaryService.search({ unitName: val, }, res => cb(res))
}
function queryAuxiliaryExaminationTypeSearch(val, cb) {
  auxiliaryService.search({ name: val, }, res => cb(res))
}
</script>
<style scoped>
:deep(.el-card__body) {
  padding: 0px;
}
:deep(.card,.el-card) {
  border-radius: 15px;
}
:deep(.card .el-card__header) {
  height: 10%;
}
:deep(.card .el-card__body) {
  height: 80%;
}
:deep(.prescription_card .el-card__header) {
  height: 10%;
}
:deep(.prescription_card .el-card__footer) {
  height: 30%;
}
:deep(.prescription_card .el-card__body) {
  height: 60%;
}
:deep(.el-anchor__marker) {
  background-color: #6a5acd;
  height: 1.5em !important;
}
:deep(.el-card__body) {
  width: 100%;
  height: 100%;
}
:deep(.el-scrollbar__view) {
  height: 100%;
}
.custom-scrollbar {
  height: 90%; /* 设置滚动区域的高度 */
  position: relative; /* 使伪元素相对于容器定位 */
  overflow: hidden; /* 隐藏溢出的内容 */
}

/* 顶部渐变 */
.custom-scrollbar::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 15%; /* 渐变高度 */
  background: linear-gradient(to bottom, rgba(255, 255, 255, 1), rgba(255, 255, 255, 0));
  pointer-events: none; /* 使元素不响应鼠标事件 */
  z-index: 999;
}

/* 底部渐变 */
.custom-scrollbar::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 15%; /* 渐变高度 */
  background: linear-gradient(to top, rgba(255, 255, 255, 1), rgba(255, 255, 255, 0));
  pointer-events: none; /* 使元素不响应鼠标事件 */
  z-index: 999;
}
:deep(.el-card__header) {
  padding: 0px;
}
:deep(.el-autocomplete>.el-input) {
  height: 100% !important;
  font-size: 1.2em;
}
:deep(.price_input .el-input__inner) {
  height: 100%;
  font-size: 2em;
}
</style>
  