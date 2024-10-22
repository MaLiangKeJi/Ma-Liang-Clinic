<template>
    <el-form ref="formRef" :model="form" style="width: 100%; height: 100%;">
        <div style="width: 100%; height: 100%; display: flex; flex-direction: row; justify-content: flex-end; align-items: center;">
            <el-form-item style="width: 10%;">
                <el-text class="mx-1" style="width: 100%; height: 100%; text-align: right; font-size: 1vw; padding-right: 10%;">药品名称</el-text>
            </el-form-item>
            <el-form-item prop="name" style="width: 20%;">
                <el-autocomplete
                    v-model="form.name"
                    :fetch-suggestions="method.searchName"
                    @select="event('selectStockName',form.name)"
                    value-key="name"
                    class="search_input"
                    style="width: 100% !important;"
                    @change="event('search')"
                />
            </el-form-item>
            <el-form-item style="width: 10%;">
                <el-text class="mx-1" style="width: 100%; height: 100%; text-align: right; font-size: 1vw; padding-right: 10%;">库存状态</el-text>
            </el-form-item>
            <el-form-item prop="state" style="width: 20%;">
                <el-select v-model="form.state" @change="event('search')" class="search_input" style="width: 100% !important;">
                    <el-option label="正常" :value="1" />
                    <el-option label="不足" :value="2" />
                </el-select>
            </el-form-item>
            <el-form-item style="width: 20%;">
                <el-button class="search_button" @click="() => event('reset')" style="width: 10vw; border-radius: 5px; height: 6vh !important;"  type="primary" plain>
                    重置
                </el-button>
            </el-form-item>
        </div>
    </el-form>
</template>

<script setup>
import { ref, defineProps, defineEmits, defineExpose } from 'vue'

import { tagService, drugService } from '@/api/clinic/index.js'

const props = defineProps(['form', 'resetFields'])
const form = props.form

const event = defineEmits(['search', 'reset', 'quickAdd', 'manuallyAdd', 'selectStockName', 'selectTag'])

const selectedTag = ref()
const formRef = ref() // 获取表单元素

const method = {
    searchName(queryString, cb) {
        drugService.search({
            name: queryString,
            current: 1,
            size: 10,
        }, ({ records }) =>  cb(records))
    },
    queryTag: (queryString, cb) => {
        tagService.selectTag({ name: searchForm.tag }, (response) => cb(response))
    },
    reset() {
        formRef.value.resetFields()
    },
}

defineExpose({
    reset: method.reset,
})
</script>
<style scoped>
:deep(.el-card__body) {
    height: 100%;
}
:deep(.el-form-item__label) {
    height: 50px;
    margin-left: 10px;
}
.exporttable {
    border: solid 1px #000000;
}
:deep(.el-descriptions__label) {
    width: 120px;
}
:deep(.el-descriptions__body .el-descriptions__table.is-bordered .el-descriptions__cell) {
    border-color: #000000;
}
:deep(.el-input, .el-input__wrapper) {
    height: 100%;
}
:deep(.el-select__wrapper) {
    height: 100%;
}
:deep(.el-form-item__label) {
    height: 50px;
    line-height: 50px;
}
:deep(.el-form-item) {
    margin-bottom: 0;
}
</style>