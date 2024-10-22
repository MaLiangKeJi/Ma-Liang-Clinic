<template>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="24">
            <el-col :span="8">
                <el-form-item prop="name" label="药品名称">
                    <el-autocomplete
                        v-model="form.name"
                        :fetch-suggestions="handle.searchName"
                        value-key="name"
                        :trigger-on-focus="false"
                        placeholder="按名称搜索药品"
                        class="search_input" style="width: 300px !important;"
                    />
                </el-form-item>
            </el-col>
            <el-col :span="8">
                <el-form-item prop="alias" label="别名">
                    <el-input v-model="form.alias" class="search_input" style="width: 300px !important;" />
                </el-form-item>
            </el-col>
        </el-row>
        <el-row :gutter="24">
            <el-col :span="14">
                <el-form-item prop="approvalNumber" label="批准文号">
                    <el-autocomplete
                        v-model="form.approvalNumber"
                        :fetch-suggestions="handle.searchApprovalNumberName"
                        @select="handle.selectApprovalNumberName"
                        value-key="approvalNumber"
                        class="search_input"
                    >
                        <template #prepend>国药准字</template>
                    </el-autocomplete>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row :gutter="24">
            <el-col :span="8">
                <el-form-item prop="manufacturer" label="生产单位">
                    <el-autocomplete
                        v-model="form.manufacturer"
                        :fetch-suggestions="handle.searchManufacturer"
                        value-key="manufacturer"
                        class="search_input" style="width: 300px !important;"
                    />
                </el-form-item>
            </el-col>
            <el-col :span="8">
                <el-form-item prop="batchNumber" label="产品批号">
                    <el-input v-model="form.batchNumber" class="search_input" style="width: 300px !important;" />
                </el-form-item>
            </el-col>
        </el-row>
        <el-row :gutter="24">
            <el-col :span="8">
                <el-form-item prop="produceDate" label="生产日期">
                    <el-date-picker
                        v-model="form.produceDate"
                        type="date"
                        class="search_input" style="width: 300px !important;"
                    />
                </el-form-item>
            </el-col>
            <el-col :span="8">
                <el-form-item prop="expiryDate" label="有效期至">
                    <el-date-picker
                        v-model="form.expiryDate"
                        type="date"
                        class="search_input" style="width: 300px !important;"
                    />
                </el-form-item>
            </el-col>
        </el-row>
        <el-row :gutter="24">
            <el-col :span="8">
                <el-form-item prop="type" label="剂型">
                    <el-input v-model="form.type" class="search_input" style="width: 300px !important;" />
                </el-form-item>
            </el-col>
            <el-col :span="8">
                <el-form-item prop="spec" label="规格">
                    <el-input v-model="form.spec" class="search_input" style="width: 300px !important;" />
                </el-form-item>
            </el-col>
        </el-row>
        <el-row :gutter="24">
            <el-col :span="8">
                <el-form-item prop="sort" label="类型">
                    <el-select v-model="form.sort" class="search_input" style="width: 300px !important;">
                        <el-option label="化学药" :value="'H'" />
                        <el-option label="中药" :value="'Z'" />
                        <el-option label="生物制品" :value="'S'" />
                        <el-option label="保健药品(通过国家药品监督管理局整顿)" :value="'B'" />
                        <el-option label="体外化学诊断试剂" :value="'T'" />
                        <el-option label="药用辅料" :value="'F'" />
                        <el-option label="进口分包装药品" :value="'J'" />
                    </el-select>
                </el-form-item>
            </el-col>
            <el-col :span="8">
                <el-form-item prop="essential" label="国家基本药物">
                    <el-select v-model="form.essential" class="search_input" style="width: 300px !important;">
                        <el-option label="国家基本药物" :value="1" />
                        <el-option label="省增补基本药物" :value="2" />
                        <el-option label="地市增补基本药物" :value="3" />
                        <el-option label="区县增补基本药物" :value="4" />
                        <el-option label="非基本药物" :value="0" />
                        <el-option label="其他" :value="5" />
                    </el-select>
                </el-form-item>
            </el-col>
        </el-row>
    </el-form>
</template>
<script setup>
import { ref, reactive, defineExpose } from 'vue'

import { drugService } from '@/api/clinic/index.js'
import { rules } from '@/views/system/clinic/stock/add/index'

const form = reactive({})
const formRef = ref()

defineExpose({ form, formRef })

const dosageForm = [
    '注射剂',
    '片剂',
    '胶囊剂',
    '口服液体剂',
    '颗粒剂（冲剂）',
    '软膏剂',
    '栓剂',
    '气雾剂',
    '汤剂',
    '散剂',
    '丸剂',
    '膏剂',
    '栓剂',
    '洗剂',
    '丹剂'
]

const handle = {
    searchName: (name, cb) => {
        drugService.search({ name, current: 1, size: 10 }, 
        ({ records }) => cb(records))
    },
    searchManufacturer: (manufacturer, cb) => {
        drugService.search({ manufacturer, name: form.name || null, current: 1, size: 10 }, 
        ({ records }) => cb(records))
    },
    searchApprovalNumberName: (approvalNumber, cb) => {
        drugService.search({ 
            approvalNumber: approvalNumber ? ('国药准字' + approvalNumber) : null,
            name: form.name || null, 
            manufacturer: form.manufacturer || null, 
            current: 1, size: 10 
        }, ({ records }) => {
            records.forEach(drug => {
                drug.approvalNumber = drug.approvalNumber.substring(4, drug.approvalNumber.length)
            });
            cb(records)
        })
    },
    selectApprovalNumberName: (item) => {
        item.sort = item.approvalNumber.substring(0, 1)
        Object.assign(form, item)
    }
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