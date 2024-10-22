<template>
    <div style="height: 100vh; padding: 2%;">
        <!-- 页面标题 -->
        <div style="padding-left: 20px; text-align: center; display: flex; align-items: center;">
            <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">药品采购</el-text>
        </div>
        <!-- 搜索条件 -->
        <el-form ref="formRef" :model="form" @submit.prevent="handle.searchClick" :inline="true">
            <div class="form-actions">
                <el-form-item>
                    <el-input v-model.trim="form.name" placeholder="药品名称" />
                </el-form-item>
                <el-form-item>
                    <el-button @click="handle.resetClick" type="primary" plain>重置</el-button>
                </el-form-item>
            </div>
        </el-form>
        <!-- 表格 -->
        <el-card class="card-content">
            <el-table ref="tableRef" :data="tableData" v-loading="loading" :row-style="{ height: '5vh' }"
                :header-cell-style="{ background: '#6a5acd', color: '#fff' }" :row-key="row => row.id"
                :expand-row-keys="expandRowKeys" @row-click="row => handle.rowClick(row)">
                <!-- 扩展列 -->
                <el-table-column type="expand">
                    <template #default="{ row }">
                        <div class="position-center">
                            <transition name="el-fade-in-linear">
                                <div class="search-status">
                                    <el-icon v-if="row.searchResult" size="2em" color="#67c23a">
                                        <CircleCheck />
                                    </el-icon>
                                    <div v-else class="spinner">
                                        <div class="double-bounce1"></div>
                                        <div class="double-bounce2"></div>
                                    </div>
                                    <el-text class="mx-1" style="margin-left: 1em;">检索补货渠道中...</el-text>
                                </div>
                            </transition>
                        </div>
                    </template>
                </el-table-column>
                <!-- 表格列定义 -->
                <el-table-column align="center" prop="name" label="药品名称" />
                <el-table-column align="center" prop="type" label="剂型" />
                <el-table-column align="center" prop="spec" label="规格" />
                <el-table-column align="center" prop="manufacturer" label="厂商" />
                <el-table-column align="center" fixed="right">
                    <template #header>
                        操作
                    </template>
                    <template #default="{ row }">
                        <el-button @click="row => handle.rowClick(row)">下单</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination v-model:current-page="current" v-model:page-size="size" v-model:total="total"
                :page-sizes="[10, 20, 50, 100, 500]" @current-change="handle.pageCurrentChange"
                @size-change="handle.pageCurrentChange" background layout="total, sizes, ->, prev, pager, next, jumper"
                size="large" />
        </el-card>
    </div>
</template>

<script setup>

import { CircleCheck } from '@element-plus/icons-vue'
import { ref, reactive, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import drugAPI from '@/api/clinic/drug.api.js'

const routerInstance = useRouter()
const formRef = ref()
const form = reactive({})
const tableRef = ref()
const tableData = ref([])
const expandRowKeys = ref([])

// 分页组件参数
const current = ref(1)
const size = ref(10)
const total = ref(0)
//loading
const loading = ref(false)

watch(
    () => form.name,
    () => {
        method.search()
    }
)

const method = {
    setPage(page) {
        current.value = parseInt(page.current)
        total.value = parseInt(page.total)
    },
    getPage() {
        return {
            current: current.value,
            size: size.value
        }
    },
    search() {
        this.loading = true
        try {
            drugAPI.search({ ...this.getPage(), name: form.name }, (response) => {
                tableData.value = response.records
                this.setPage(response)
                this.loading = false
            })
        } catch (error) {
            this.loading = false
        }
    },
    toCure(row) {
        let routeData = routerInstance.resolve({
            path: `/clinic/cure/` + row.id,
            query: {
                title: row.name,
                index: row.id
            }
        });
        window.open(routeData.href, "_blank");
    }
}

const handle = {
    searchClick() {
        method.search()
    },
    resetClick() {
        formRef.value.resetFields()
        form.name = null
        method.search()
    },
    pageCurrentChange() {
        method.search()
    },
    rowClick(row) {
        if (row.isOpen) {
            handle.closeRow(row)
        } else {
            handle.expandRow(row)
        }
    },
    expandRow(row) {
        if (expandRowKeys.value == undefined) {
            expandRowKeys.value = []
        }
        row.isOpen = true
        expandRowKeys.value.push(row.id)
    },
    closeRow(row) {
        if (expandRowKeys.value == undefined) {
            expandRowKeys.value = []
        }
        row.isOpen = false
        expandRowKeys.value = expandRowKeys.value.filter(id => id !== row.id)
    },
}

onMounted(() => {
    let routerQuery = routerInstance.currentRoute.value.query
    if (routerQuery && routerQuery.val) {
        form.name = routerQuery.val
    }
    method.search()
})
</script>

<style scoped>

/* 卡片内容样式 */
.card-content {
    height: 85%;
    border-radius: 15px;
}

/* 其他样式 */
.position-center {
    display: flex;
    justify-content: center;
    align-items: center;
}

.search-status {
    display: flex;
    align-items: center;
    margin-bottom: 0.8em;
}
</style>