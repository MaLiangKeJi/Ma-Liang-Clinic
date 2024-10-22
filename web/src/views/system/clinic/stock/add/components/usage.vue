<template>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="24">
            <el-col :span="8" style="min-width: 400px;">
                <el-form-item label="单次剂量">
                    <el-input-number v-model="form.singleDose" :min="1" class="search_input" style="width: 150px !important; margin: 0 5px;" />
                    <el-select v-model="form.singleDoseUnit" class="m-2 search_input" style="width: 80px !important; margin: 0 5px;">
                        <el-option v-for="(item) in props.unit" :key="item.id" :label="item.name" :value="item.id" />
                    </el-select>
                </el-form-item>
            </el-col>
            <el-col :span="8">
                <el-form-item label="频次">
                    <el-select v-model="form.frequency" class="search_input" style="width: 150px !important; margin: 0 5px;">
                        <el-option label="一天 1 次" value="一天 1 次" />
                        <el-option label="一天 2 次" value="一天 2 次" />
                        <el-option label="一天 3 次" value="一天 3 次" />
                        <el-option label="一天 4 次" value="一天 4 次" />
                        <el-option label="一天 5 次" value="一天 5 次" />
                    </el-select>
                </el-form-item>
            </el-col>
            <el-col :span="8">
                <el-form-item label="用法">
                    <el-select v-model="form.usage" class="search_input" style="width: 150px !important; margin: 0 5px;">
                        <el-option label="口服" value="口服" />
                        <el-option label="外用" value="外用" />
                        <el-option label="静脉点滴" value="静脉点滴" />
                    </el-select>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row :gutter="24">
            <el-col :span="8">
                <el-form-item prop="skinTest" label="皮试">
                    <el-switch
                        v-model="form.skinTest"
                        class="ml-2"
                        style="--el-switch-on-color: #ff4949; margin-right: 20px;"
                    />
                    <span v-show="!form.skinTest">不用皮试</span>
                    <span v-show="form.skinTest">需要皮试！</span>
                </el-form-item>
            </el-col>
        </el-row>
    </el-form>
</template>
<script setup>
import { ref, reactive, watch, defineProps, defineExpose } from 'vue'
import { rules } from '@/views/system/clinic/stock/add/index'

const props = defineProps(['unit'])
const form = reactive({
    singleDose: 1,
    frequency: "一天 3 次",
    usage: "口服",
    skinTest: false
})
const formRef = ref()

defineExpose({ form, formRef })

watch(
    () => props.unit,
    () => {
        let lastUnitIdx = props.unit.length - 1
        let lastUnit = props.unit[lastUnitIdx]
        form.singleDoseUnit = lastUnit.id
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