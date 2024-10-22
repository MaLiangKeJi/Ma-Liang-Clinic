<template>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="按">
            <el-select v-model="form.countType" class="search_input" style="width: 150px !important; margin-right: 10px;">
                <el-option label="百分比" :value="1" />
                <el-option label="数量" :value="2" />
            </el-select>
            统计，不足
            <el-select v-show="form.countType == 1" v-model="form.countNumber" class="search_input" style="width: 150px !important; margin: 0 10px;">
                <el-option label="10%" :value="10" />
                <el-option label="20%" :value="20" />
                <el-option label="30%" :value="30" />
                <el-option label="40%" :value="40" />
                <el-option label="50%" :value="50" />
            </el-select>
            <el-input-number v-show="form.countType == 2" v-model="form.countNumber" :min="1" class="search_input" style="width: 150px !important; margin: 0 10px;" />
            <el-select v-model="form.countUnit" class="m-2 search_input" style="width: 150px !important; margin: 0 10px;">
                <el-option v-for="(item) in props.unit" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
            时，提醒我
            <el-popover
                    placement="top-start"
                    :width="500"
                    trigger="never" class="card"
                    content="统计库存数量的方式，可以按【百分比】或者【剩余数量】统计并提醒"
                >
                <template #reference>
                    <el-button style="width: 32px; height: 32px; color: crimson;" text>
                        <el-icon><Warning /></el-icon>
                    </el-button>
                </template>
            </el-popover>
        </el-form-item>
    </el-form>
</template>
<script setup>
import { ref, defineProps, reactive, watch, defineExpose } from 'vue'
import { Warning } from '@element-plus/icons-vue'
import { rules } from '@/views/system/clinic/stock/add/index'

const form = reactive({
    countType: 0,
    countNumber: 20,
})
const formRef = ref(0)

defineExpose({ form, formRef })

const props = defineProps(['unit'])

watch(
    () => props.unit,
    () => {
        let lastUnitIdx = props.unit.length - 1
        let lastUnit = props.unit[lastUnitIdx]
        form.countUnit = lastUnit.id
    }
)
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