<template>
    <el-row>
        <div style="margin-right: 10px;">{{ (patient.name) + ',' }}</div>
        <div style="margin-right: 10px;">{{ (patient.sex == 0 ? '女':'男')  + ',' }}</div>
        <div style="margin-right: 10px;">{{ (patient.age || '无') + ' 岁' + ',' }}</div>
        <div style="margin-right: 10px;">{{ getRemark(patient)  + ',' }}</div>
        <div style="margin-right: 10px;">{{ (patient.isFirst == 0 ? '初诊':'复诊') }}</div>
    </el-row>
</template>
<script setup>
import { defineProps } from 'vue'

const { patient } = defineProps(['patient'])

function getRemark(patient) {
    return (patient.remark) && patient.remark != 'null' && patient.remark != 'undefined' ? patient.remark : '无'
}
</script>