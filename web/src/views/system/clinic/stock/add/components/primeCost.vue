<template>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="24">
            <el-col :span="8" style="min-width: 300px;">
                <el-form-item prop="totalCost" label="总进货价" >
                    <el-input-number v-model="form.totalCost" @change="handle.totalCostChange" class="search_input" style="width: 150px !important; margin-right: 10px;"  :min="0"/>
                    <span>元</span>
                </el-form-item>
            </el-col>
            <el-col :span="8" style="min-width: 400px;">
                <el-form-item prop="cost" :label="'单' + fastUnitName + '进价'" :span="4">
                    <el-input-number v-model="form.cost" :disabled="true" class="search_input" style="width: 150px !important; margin-right: 10px;" />
                    <span>元</span>
                </el-form-item>
            </el-col>
            <el-col :span="8" style="min-width: 300px;">
                <el-form-item prop="provider" label="供应商">
                    <el-input v-model="form.provider" class="search_input" style="width: 150px !important;" />
                </el-form-item>
            </el-col>
        </el-row>
        <el-row :gutter="24">
            <el-col :span="8" style="min-width: 500px;">
                <el-form-item prop="price" label="零售价" :span="4">
                    <el-input-number v-model="form.price" class="search_input" style="width: 150px !important; margin-right: 10px;" :min="0" />
                    <span>元，每</span>
                    <el-select v-model="form.priceUnit" class="search_input m-2" style="width: 80px !important; margin-left: 10px;">
                        <el-option v-for="(item) in props.unit" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
            </el-col>
        </el-row>
    </el-form>
</template>
<script setup>
import { ref, reactive, defineProps, defineEmits, watch, computed, defineExpose } from 'vue'
import { rules } from '@/views/system/clinic/stock/add/index'

const event = defineEmits(['totalCostChange', 'numberChange'])
const props = defineProps(['unit', 'number', 'numberUnit'])

const form = reactive({
    number: 1,
    cost: 0.00,
})
const formRef = ref()

const handle = {
    totalCostChange: () => method.computeCost(),
    numberChange: () => method.computeCost(),
}

const method = {
    computeCost: () => {
        let totalCost = form.totalCost || 0
        let fastUnitIdx = 0, fastUnit = props.unit[fastUnitIdx]
        let number = fastUnit.number || 1
        form.cost = parseInt((totalCost / number).toFixed(2))
    }
}

watch(() => props.unit, () => {
    form.fastUnit = props.unit[0]
    form.lastUnit = props.unit[props.unit.length - 1]
    form.priceUnit = form.lastUnit.id
    method.computeCost()
})
const fastUnitName = computed(() => {
  return form.fastUnit ? form.fastUnit.name : '个'
})

defineExpose({ form, formRef, computeCost: method.computeCost })
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