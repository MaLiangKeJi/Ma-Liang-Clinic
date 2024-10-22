<template>
    <el-table :data="data" stripe style="width: 100%">
        <el-table-column prop="date" label="就诊时间" width="180">
            <template #default="{row}">
                {{ row.dossierTime.slice(0, 10) }}
            </template>
        </el-table-column>
        <el-table-column prop="name" label="收费时间" width="180" >
            <template #default="{row}">
                {{ row.createTime.slice(0, 10) }}
            </template>
        </el-table-column>
        <el-table-column prop="diagnosis" label="初步诊断">
            <template #default="{row}">
                {{ row.diagnosis }}
            </template>
        </el-table-column>
        <el-table-column label="费用">
            <template #default="{row}">
                <span style="margin-right: 5px;">{{ row.fee }}</span>
                <span v-if="row.fee && row.fee >= 0">元</span>
            </template>
        </el-table-column>
        <el-table-column label="收费方式">
            <!-- //如果收费方式是3，则红字展示 -->
            <template #default="{row}">
                <el-text v-if="row.way == '挂账'" class="mx-1" type="danger">{{ row.way }}</el-text>
                <el-text v-else class="mx-1" type="success">{{ row.way }}</el-text>
            </template>
            <!-- <template #default="{row}">
                {{ row.way }}
            </template> -->
        </el-table-column>
        <el-table-column prop="address" label="状态">
            <template #default="{row}">
                <el-text v-if="row.state == '已支付'" class="mx-1" type="success">已支付</el-text>
                <el-button
                    v-else
                    @click="notPayBtnClick(row.id)"
                    type="danger"
                    text
                    style="padding-left: 0px;"
                >
                    未支付
                </el-button>
            </template>
        </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
        v-model:current-page="current"
        :page-size="10"
        :total="total"
        layout="prev, pager, next"
        background
        style="margin-top: 20px; position: absolute; right: 10%;"
    />



    <el-dialog
        v-model="payVisible"
        title="收费"
        width="30%"
    >
        <ThisCharge :patient="patient" :casePayRecordId="casePayRecordIdProp" @submit="paySubmit" />
    </el-dialog>
</template>
<script setup>
import { payService } from '@/api/clinic/index.js'
import { ref, defineProps, onMounted, defineExpose } from 'vue'

import ThisCharge from '@/views/system/clinic/cure/components/thisCharge.vue'

const data = ref([])

const props = defineProps(['patient'])
const patient = props.patient


const casePayRecordIdProp = ref(0)

const payVisible = ref(false)


// 分页组件参数
const current = ref(1)
const size = ref(10)
const total = ref(0)
const getPage = () => ({ current: current.value, size: size.value, total: total.value })
const setPage = (pageCurrent, pageTotal) => {
    current.value = pageCurrent
    total.value = pageTotal
}

function notPayBtnClick(payRecordId) {
    payVisible.value = true
    casePayRecordIdProp.value = payRecordId
}


function paySubmit() {
    loadTable()
    payVisible.value = false
}

function loadTable() {
    if(patient && patient.id) {
        payService.search({
            patientId: patient.id,
            current: current.value,
            size: size.value,
        }, (response) => {
            data.value = response.records
            setPage(response.current, response.total)
        })
    }
}

onMounted(() => {
    loadTable()
})

defineExpose({
    reloadTable: loadTable
})
</script>