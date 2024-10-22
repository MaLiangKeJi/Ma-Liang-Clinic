<template>
    <el-card style="height: 80%; margin: 2%; border: 15px;">
        <el-table :data="tableData" style="height: 80%; width: 100%;">
            <el-table-column prop="name" label="姓名" />
            <el-table-column prop="phone" label="手机号" />
            <el-table-column prop="expirationTime" label="过期时间" />
            <el-table-column prop="supperAdmin" label="是否为超管">
                <template #default="{ row }">
                    {{ row.supperAdmin ? '是' : null }}
                </template>
            </el-table-column>
            <el-table-column fixed="right" label="Operations">
                <template #default>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
            v-model:current-page="current"
            v-model:page-size="size"
            v-model:total="total"
            :page-sizes="[10, 20, 50, 100, 500]"
            @current-change="handle.pageCurrentChange"
            @size-change="handle.pageCurrentChange"
            style="height: 10%; margin-top: 5%; padding: 0 5%; font-size: 1.2vh"
            background
            layout="total, sizes, ->, prev, pager, next, jumper"
            size="large"
        />
    </el-card>
</template>
  
<script setup>
import { ref, onMounted } from 'vue'
import { searchUserList as searchUserListAPI } from '@/api/back.api.js'

// 分页组件参数
const current = ref(1)
const size = ref(10)
const total = ref(0)

const tableData = ref([])

onMounted(() => {
    handle.search()
})

const handle = {
    pageCurrentChange() { search(); },
    search() {
        searchUserListAPI({ current: current.value, size: size.value, }, (res) => {
            tableData.value = res.records
            total.value = res.total
        })
    },
}
</script>