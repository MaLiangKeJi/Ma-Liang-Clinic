<template>
    <div style="width: 100%; height: 10%; display: flex; justify-content: space-between;">
        <div>
            <el-input v-model="input" style="width: 240px" placeholder="搜索公司名称" />
        </div>
        <el-button @click="create" type="primary" :icon="Plus" style="width: 200px; height: 50px;" >
            公司入驻
        </el-button>
    </div>
    <el-table :data="tableData" style="width: 100%">
        <el-table-column type="index" width="50" />
        <el-table-column prop="name" label="公司名称" width="180" />
        <el-table-column prop="adminName" label="管理人" width="180">
            <template #default="{ row }">
                {{ row.admin.name }} : {{ row.adminId }}
            </template>
        </el-table-column>
        <el-table-column prop="staffNum" label="公司人数" width="180" />
        <el-table-column prop="type" label="软件版本" width="180" />
        <el-table-column prop="expiration" label="过期时间" width="180" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column fixed="right" label="Operations" width="120">
            <template #default="scope">
                <el-button
                    link
                    type="primary"
                    size="small"
                    @click.prevent="deleteRow(scope.$index)"
                >
                    Remove
                </el-button>
            </template>
        </el-table-column>
    </el-table>

    <el-pagination v-show="queryParams.total > 0" v-model:current-page="queryParams.current" v-model:page-size="queryParams.size" :total="queryParams.total" @current-change="research" background layout="prev, pager, next" style="margin-top: 5%;" />
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { searchCompany as searchCompanyAPI } from '@/api/back.api.js'
import {
  Plus,
} from '@element-plus/icons-vue'
import { useRouter } from "vue-router";

const router = useRouter() // 路由

const queryParams = reactive({
    current: 1,
    size: 10,
    total: 0,
})
const tableData = ref([])

function research() {
    searchCompanyAPI(queryParams).then(({ data }) => {
        let { current, records, total } = data.data
        tableData.value = records
        queryParams.current = current
        queryParams.total = total
    })
}

onMounted(() => {
    research()
})

function create() {
    router.push({ path: "/back/company/create", })
}
</script>