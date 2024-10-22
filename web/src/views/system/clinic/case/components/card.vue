<template>
    <el-card v-show="cases.length > 0" class="box-card">
        <template #header>
        <div class="card-header">
            <span>
            <h1 style="display: inline-block;">门诊病历</h1>
            <span style="font-size: 0.7em;">（最近三次）</span>
            </span>
            <el-button class="button" @click="isShowAllCase = true">全部病历</el-button>
        </div>
        </template>
        <el-collapse v-model="activeNames" max-height="400px" >
            <el-collapse-item v-for="(item, index) in cases" :key="item.id">
                <template #title>
                <el-space class="a-front">
                    <span>
                    {{ item.createTime || '无' }}
                    </span>
                    <span>{{ item.diagnosis || '无' }}</span>
                    <el-tag v-if="index == 0" class="ml-2" type="success">最新</el-tag>
                </el-space>
                </template>
                <el-row>
                    <el-col><div class="grid-content ep-bg-purple">主诉: {{ item.chiefComplaint || '无' }}</div></el-col>
                </el-row>
                <el-row>
                    <el-col><div class="grid-content ep-bg-purple">体重: {{ item.weight || '无' }}</div></el-col>
                </el-row>                
                <el-row>
                    <el-col><div class="grid-content ep-bg-purple">体温: {{ item.temperature || '无' }}</div></el-col>
                </el-row>
                <el-row>
                    <el-col><div class="grid-content ep-bg-purple">现病史: {{ item.historyPresentIllness || '无' }}</div></el-col>
                </el-row>
                <el-row>
                    <el-col><div class="grid-content ep-bg-purple">既往史（包括药物过敏史）: {{ item.pastMedicalHistory ? item.pastMedicalHistory : '无' }}</div></el-col>
                </el-row>
                <el-row>
                    <el-col><div class="grid-content ep-bg-purple">诊断: {{ item.diagnosis || '无' }}</div></el-col>
                </el-row>
                <el-row>
                    <el-col><div class="grid-content ep-bg-purple">处理: {{ item.dealWith || '无' }}</div></el-col>
                </el-row>
                <el-row>
                    <el-col><div class="grid-content ep-bg-purple">备注: {{ item.notes || '无' }}</div></el-col>
                </el-row>
            </el-collapse-item>
        </el-collapse>
    </el-card>
    <el-card v-show="cases.length == 0" class="box-card">
        <el-text class="mx-1" type="info">初诊，无病历信息</el-text>
    </el-card>
    <el-drawer v-model="isShowAllCase" size="60%">
        <template #header> <h1>全部病历</h1> </template>
        <template #default> <All /> </template>
        <template #footer>
            <div style="flex: auto">
                <el-button @click="isShowAllCase.value = false">离开</el-button>
            </div>
        </template>
    </el-drawer>
</template>
<script setup>
import All from '@/views/system/clinic/case/components/all.vue'
import { ref, inject } from 'vue'

const activeNames = ref(['1'])

const cases = inject('cases')
const isShowAllCase = inject('isShowAllCase')
</script>