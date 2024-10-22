<template>
    <div style="height: 100%; padding: 2%;">
        <el-form ref="formRef" :model="form" style="height: 10%; display: flex; justify-content: space-between;">
            <div style="width: 10%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
                <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">药品入库</el-text>
            </div>
        </el-form>
        <el-card style="height: 85%; border-radius: 15px; padding: 2%;">
            <el-result
                v-if="saveIsSuccess"
                icon="success"
                title="入库成功"
            >
                <template #sub-title>
                    编号：{{ stockInNo }}
                </template>
                <template #extra>
                    <el-button type="primary" @click="handle.backAdd()">继续添加</el-button>
                </template>
            </el-result>
            <div v-else style="height: 100%;">
                <div style="height: 90%;">
                    <el-scrollbar height="100%">
                        <div style="padding: 2%">
                            <div style="margin-bottom: 50px;">
                                <h2>药品信息</h2>
                                <el-divider />
                                <div style="width: 100%;">
                                    <DrugModule ref="drugRef" />
                                </div>
                            </div>
                            <div style="margin-bottom: 50px;">
                                <h2>单位结构</h2>
                                <el-divider />
                                <div style="width: 100%;">
                                    <UnitStructureModule 
                                        ref="unitStructureRef" 
                                        :options="unitCascaderOptions" 
                                        @unitChange="handle.unitChange"
                                        @numberChange="handle.numberChange"
                                    />
                                </div>
                            </div>
                            <div style="margin-bottom: 50px;">
                                <h2>用法</h2>
                                <el-divider />
                                <div style="width: 100%;">
                                    <UsageModule ref="usageRef" :unit="unitStructure" />
                                </div>
                            </div>
                            <div style="margin-bottom: 50px;">
                                <h2>进售价格</h2>
                                <el-divider />
                                <div style="width: 100%;">
                                    <PrimeCostModule ref="primeCostRef" :unit="unitStructure" />
                                </div>
                            </div>
                            <div style="margin-bottom: 50px;">
                                <h2>缺货提醒</h2>
                                <el-divider />
                                <div style="width: 100%;">
                                    <OutStockReminderModule ref="outStockReminderRef" :unit="unitStructure" />
                                </div>
                            </div>
                            <div style="margin-bottom: 50px;">
                                <h2>其他</h2>
                                <el-divider />
                                <div style="width: 100%;">
                                    <el-form :model="form" label-width="100px">
                                        <el-row :gutter="24">
                                            <el-col :span="24">
                                                <el-form-item prop="remark" label="备注">
                                                    <el-input v-model="form.remark" type="textarea" :autosize="{ minRows: 5, maxRows: 9 }" />
                                                </el-form-item>
                                            </el-col>
                                        </el-row>
                                    </el-form>
                                </div>
                            </div>
                        </div>
                    </el-scrollbar>
                </div>
                <div style="text-align: center; height: 10%; display: flex; justify-content: center; align-items: center;">
                    <el-button  @click="handle.saveClick(formRef)" type="primary"  style="width: 200px; height: 65px;" >
                        <span style="font-size: 1.2em; font-weight: 700;">保存</span>
                    </el-button>
                    <el-button  @click="handle.resetClick(formRef)" style="width: 200px; height: 65px; font-weight: 700;" >
                        <span style="font-size: 1.2em; font-weight: 700;">清空</span>
                    </el-button>
                </div>
            </div>
        </el-card>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted, defineExpose } from 'vue'
import { drugService, stockService } from '@/api/clinic/index.js'

import DrugModule from './components/drug.vue'
import UnitStructureModule from './components/unitStructure.vue'
import UsageModule from './components/usage.vue'
import PrimeCostModule from './components/primeCost.vue'
import OutStockReminderModule from './components/outStockReminder.vue'

import { getParams } from './index.js'


const form = reactive({})

const unitStructure = ref([])

function setForm(method) { Object.assign(form, method(form)) }
defineExpose({ setForm })

const selectUnit = ref([])
var unitCascaderOptions = ref([])


const drugRef = ref()
const unitStructureRef = ref()
const usageRef = ref()
const primeCostRef = ref()
const outStockReminderRef = ref()

const saveIsSuccess = ref(false)
const stockInNo = ref('')

const handle = {
    unitChange: (units) => unitStructure.value = units,
    numberChange: () => primeCostRef.value.computeCost(),
    saveClick() {
        method.validate(() => {
            let params = getParams(form, drugRef, unitStructureRef, usageRef, primeCostRef, outStockReminderRef)
            stockService.add(params, (response) => {
                saveIsSuccess.value = true
                stockInNo.value = response
            })
        })
    },
    resetClick() {
        if(drugRef.value) drugRef.value.formRef.resetFields()
        if(unitStructureRef.value) unitStructureRef.value.formRef.resetFields()
        if(usageRef.value) usageRef.value.formRef.resetFields()
        if(primeCostRef.value) primeCostRef.value.formRef.resetFields()
        if(outStockReminderRef.value) outStockReminderRef.value.formRef.resetFields()
    },
    backAdd() {
        this.resetClick()
        saveIsSuccess.value = false
    },
}

const noRepeatAllDefUnit = ref([])

onMounted(() => {
    drugService.getUnitTree((response) => {
        unitCascaderOptions.value = response
        if(form.batchNumber) {
            form.selectUnits.forEach(selectUnitObj => selectUnit.value.push(selectUnitObj.id))
        }
    })

    if(form.batchNumber) {
        form.selectUnits.sort((a,b) => a.sort - b.sort)
        form.number = 1
    } else {
        //初始化缺货提醒
        if(form.approvalNumber) form.sort = form.approvalNumber.charAt(4)
        drugService.getNoRepeatAllDefUnit((response) => {
            noRepeatAllDefUnit.value = response
            let initArr = [1, 9, 12, 15]
            selectUnit.value = initArr   // 对【单位结构】初始化
            //初始化缺货提醒
            form.selectUnits = method.getUnit(initArr)
            form.singleDoseUnit = form.countUnit = form.selectUnits[form.selectUnits.length - 1].id
        })
    }
    if(form.approvalNumber != null && form.approvalNumber != undefined) form.sort = form.approvalNumber.charAt(4)
})

const method = {
    getUnit(ids) {
        let result = []
        for (let index = 0; index < noRepeatAllDefUnit.value.length; index++) {
            const unit = noRepeatAllDefUnit.value[index];
            for (let idIndex = 0; idIndex < ids.length; idIndex++) {
                const id = ids[idIndex];
                if(unit.id == id) {
                    unit.number = 1
                    result.push(unit)
                }
            }
        }
        return result
    },
    validate(callback) {
        Promise.all([
            drugRef.value.formRef.validate(),
            unitStructureRef.value.formRef.validate(),
            usageRef.value.formRef.validate(),
            primeCostRef.value.formRef.validate(),
            outStockReminderRef.value.formRef.validate(),
        ]).then(res => callback()).catch(err => {});
    },
}
</script>
<style scoped>
.card {
    border-width: 10px;
}
.el-divider {
    margin-top: 12px !important;
}
.el-space__item {
    width: 100% !important;
}
:deep(.el-card__body) {
    height: 100%;
    padding: 0;
}
</style>