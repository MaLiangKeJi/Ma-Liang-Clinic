<template>
    <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="date" label="名称" width="180">
            <template #default="{$index, row}">
                <div v-show="row.add" style="height: 100%;">
                    <el-input v-model="row.name" :disabled="isPay" />
                </div>
                <div v-show="!row.add" style="height: 100%;">
                    <el-button type="primary" @click="handle.addDrugClick(row)">增加收费</el-button>
                </div>
            </template>
        </el-table-column>
        <el-table-column prop="fee" label="费用" width="180" >
            <template #default="{$index, row}">
                <div v-show="row.add" style="height: 100%;">
                    <el-input v-model="row.fee" :disabled="isPay" style="width: 80%; margin-right: 5px;" /> 元
                </div>
            </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注">
            <template #default="{$index, row}">
                <div v-show="row.add" style="height: 100%;">
                    <el-input v-model="row.remark" :disabled="isPay" />
                </div>
            </template>
        </el-table-column>
        <el-table-column fixed="right" width="200">
        <template #default="{$index, row}">
          <el-button v-if="row.add && (!isPay)" @click="tableData.splice($index, 1)" type="danger">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-if="!isPay" style="text-align: center; margin-top: 20px;">
        <el-button v-if="isSubmit()" @click="handle.updateClick(formRef)" style="width: 200px; height: 50px;" >
            修改
        </el-button>
        <el-button v-else @click="handle.saveClick(formRef)" type="primary" style="width: 200px; height: 50px;" >
            下一步
        </el-button>
    </div>
</template>
<script setup>
import { ref, defineProps, defineEmits, onMounted, defineExpose } from 'vue'
import { ElNotification } from 'element-plus'

import { payService as service } from '@/api/clinic/index.js'

const props = defineProps(['admission'])

const event = defineEmits(['submit', 'update'])

const tableData = ref([])

const isPay = ref(false)

const handle = {
    addDrugClick(row) {
        row.add = true
        tableData.value.push({})
    },
    saveClick() {
        let fistRowName = tableData.value[0].name
        if(fistRowName && fistRowName != '') {
            ElNotification({ title: '通知', message: '数据提交中...', type: 'info' })
            service.addOtherCharge({
                payId: getPayId(),
                payOther: tableData.value.slice(0, tableData.value.length - 1),
            }, (response) => {
                ElNotification({ title: '成功', message: '信息提交成功!', type: 'success' })
                removeAddButtonRow()
                event('submit')
            })
        } else {
            removeAddButtonRow()
            event('submit')
        }
    },
    updateClick() {
        service.updateRecord({
            payId: getPayId(),
            payOther: tableData.value.slice(0, tableData.value.length - 1),
        }, response => {
            tableData.value = []
            response.forEach(payRecord => {
                tableData.value.push({
                    add: true,
                    ...payRecord,
                })
            })
            tableData.value.push({})
            event('update')
        })
    },
}
function removeAddButtonRow() {
    tableData.value.splice(tableData.value.length - 1, 1)
}
function getPayId() {
    return props.admission.payId
}
function isSubmit() {
    return (props.admission.payRecords && props.admission.payRecords.length >= 1)
}
onMounted(() => {
    if(props.admission.payRecords) {
        tableData.value = []
        props.admission.payRecords.forEach(payRecord => {
            tableData.value.push({
                add: true,
                ...payRecord,
            })
        })
    }
    if(props.admission.pay && props.admission.pay.state == 1) {
        isPay.value = true
    } else {
        tableData.value.push({})
    }
})
defineExpose({
    switchSubmitState: () => {
        isPay.value = true
        removeAddButtonRow()
    }
})
</script>
<style scoped>
</style>
