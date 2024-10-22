<template>
    <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column type="index" width="50" />
        <el-table-column prop="name" label="名称" width="180" />
        <el-table-column prop="description" label="描述" width="180" />
        <el-table-column prop="admin" label="管理员" width="180">
            <template #default="{ row }">
                {{ row.admin.name }}
            </template>
        </el-table-column>
        <el-table-column prop="pid" label="父级系统" width="180" />
        <el-table-column prop="state" label="状态" />
    </el-table>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import { searchSystem as searchSystemAPI } from '@/api/back.api.js'

const tableData = ref([])

function research() {
    searchSystemAPI(res => {
        tableData.value = res
    })
}
onMounted(() => {
    research()
})
</script>