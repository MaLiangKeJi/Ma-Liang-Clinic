<template>
    <!-- 单位结构 -->
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="width: 100%;">
        <el-row :gutter="25">
            <el-col :span="8">
                <el-form-item prop="selectUnitIds">
                    <template #label>
                        结构
                    </template>
                    <el-cascader
                        v-model="form.selectUnitIds"
                        :ref="unitCascaderRef"
                        :options="props.options"
                        :props="unitCascaderConfig"
                        @change="handle.selectUnitChange"
                        class="search_input" style="width: 300px !important;"
                    />
                </el-form-item>
            </el-col>
        </el-row>
        <el-row :gutter="25">
            <el-col 
                v-for="(item, index) in form.selectUnits" 
                :key="index" 
                style="min-width: 300px;"
                :span="8"
            >
                <el-form-item>
                    <template #label>
                        <span v-if="index == 0">共</span>
                        <span v-else>{{ '每' + form.selectUnits[index - 1].name }}</span>
                    </template>
                    <el-input-number 
                        v-model="item.number" 
                        @change="handle.numberChange"
                        class="search_input" style="width: 150px !important; margin-right: 10px;"
                    />
                    <span>{{ index == 0 ? form.selectUnits[0].name : item.name}} </span>
                </el-form-item>
            </el-col>
        </el-row>
    </el-form>
</template>
<script setup>
import { ref, reactive, defineProps, defineEmits, defineExpose } from 'vue'

import { rules } from '@/views/system/clinic/stock/add/index'
const props = defineProps(['options'])
const event = defineEmits(['unitChange', 'numberChange'])
const form = reactive({
    selectUnitIds: [],
    selectUnits: [],
})
const formRef = ref()

defineExpose({ form, formRef })


const unitCascaderRef = ref()
const handle = {
    selectUnitChange: () => {
        let selectUnits = []
        let currentUnitID = form.selectUnitIds[0]
        let unitSet = props.options
        for (let index = 0; index < form.selectUnitIds.length; index++) {
            unitSet.forEach(unit => {
                if(unit.id == currentUnitID) {
                    unit.number = 1
                    selectUnits.push(unit)
                    currentUnitID = form.selectUnitIds[index + 1]
                    unitSet = unit.children
                }
            });
        }
        form.lastUnit = form.selectUnits[form.selectUnits.length - 1]
        form.selectUnits = selectUnits
        event('unitChange', selectUnits)
    },
    numberChange: () => event('numberChange'),
}
const unitCascaderConfig = {
    value: 'id',
    label: 'name',
}
</script>
<style scoped>
.card {
    border-width: 10px;
}
:deep(.el-divider) {
    margin-top: 12px !important;
}
:deep(.el-space__item) {
    width: 100% !important;
}
:deep(.el-select__wrapper) {
    height: 100%;
}
:deep(.el-input) {
    height: 100%;
}
:deep(.el-input_wrapper) {
    height: 100%;
}
:deep(.el-form-item__label) {
    height: 50px;
    line-height: 50px;
}
</style>