<template>
    <el-form :model="form" ref="formRef" label-width="120px">
        <el-row :gutter="24">
            <el-col :span="4">
                <el-form-item prop="name" label="姓名">
                    <el-input v-model="form.name" />
                </el-form-item>
            </el-col>
            <el-col :span="4">
                <el-form-item prop="phone" label="手机号">
                    <el-input v-model="form.phone" />
                </el-form-item>
            </el-col>
            <el-col :span="4">
                <el-button @click="handle.search">查询</el-button>
                <el-button @click="handle.resetForm(formRef)">重置</el-button>
            </el-col>
        </el-row>
    </el-form>

    <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="dossierTime" label="就诊时间" width="180" />
        <el-table-column prop="name" label="姓名" width="180" />
        <el-table-column prop="sex" label="性别" width="180" />
        <el-table-column prop="age" label="年龄" width="180" />
        <el-table-column prop="phone" label="手机号" width="180" />
        <el-table-column prop="address" label="地址" />
        <el-table-column prop="fee" label="费用" />
    </el-table>

    <!-- 分页 -->
    <el-pagination
        v-model:current-page="current"
        v-model:page-size="size"
        v-model:total="total"
        @current-change="handle.pageCurrentChange"
        style="margin-top: 50px;"
        background
    />
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { payService } from '@/api/clinic/index.js'

const formRef = ref()

const form = reactive({})
const tableData = ref([])
// 分页组件参数
const current = ref(1)
const size = ref(10)
const total = ref(0)
const getPage = () => ({ current: current.value, size: size.value, total: total.value })
const setPage = (pageCurrent, pageTotal) => {
    current.value = pageCurrent
    total.value = pageTotal
}

const handle = {
    search: () => {
        payService.searchPayBack({
            name: form.name,
            phone: form.phone,
            current: current.value,
            size: size.value,
        }, (response) => {
            tableData.value = response.records
            setPage(response.current, response.total)
        })
    },
    resetForm: (formEl) => {
        if (!formEl) return
        formEl.resetFields()
    },
}

onMounted(() => {
    handle.search()
})
</script>