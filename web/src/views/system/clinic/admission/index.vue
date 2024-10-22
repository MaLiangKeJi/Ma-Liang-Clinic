<template>
    <div style="height: 100%; padding: 2%;">
        <el-form ref="formRef" :model="form" style="height: 10%; display: flex; justify-content: space-between;">
            <div
                style="width: 10%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
                <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">接诊记录</el-text>
            </div>
            <div style="width: 90%; height: 100%; display: flex; align-items: center; justify-content: flex-end;">
                <el-form-item style="width: 10%;">
                    <el-text class="mx-1"
                        style="width: 100%; height: 100%; text-align: right; font-size: 1vw; padding-right: 10%;">患者姓名</el-text>
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-input style="width: 100%;" class="search_input" v-model="form.val" clearable="true"
                        placeholder="请输入姓名/电话" @change="handle.search" />
                </el-form-item>
                <el-form-item style="width: 10%;">
                    <el-text class="mx-1"
                        style="width: 100%; height: 100%; text-align: right; font-size: 1vw; padding-right: 10%;">就诊日期</el-text>
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-date-picker prop="createTime" v-model="form.createTime" value-format="YYYY-MM-DD" type="date"
                        style="width: 60%;" class="search_input" placeholder="请选择就诊日期"
                        @change="() => method.search()" />
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-button class="search_button" @click="handle.resetClick"
                        style="width: 10vw; border-radius: 15px; height: 5vh !important;" type="primary" plain>
                        重置
                    </el-button>
                </el-form-item>
            </div>
        </el-form>
        <el-card style="height: 85%; border-radius: 15px;">
            <el-table :data="tableData" height="100%" style="height: 90%; width: 100%; font-size: 1.5vh;"
                :row-style="{ height: '5vh' }" :header-cell-style="{ background: '#6a5acd', color: '#fff' }"
                scrollbar-always-on>
                <el-table-column align="center" prop="createTime" label="就诊日期">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ convertDate(row.createTime) }}</span>
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="name" label="姓名" :formatter="convertName" show-overflow-tooltip />
                <el-table-column align="center" prop="sex" label="性别" :formatter="convertSex" />
                <el-table-column align="center" prop="age" label="年龄" :formatter="convertAge" />
                <el-table-column align="center" prop="phone" label="手机号" :formatter="convertPhone" />
                <el-table-column align="center" prop="isFirst" label="初复诊" :formatter="convertIsFirst">
                    <template #default="{ row }">
                        <el-tag :type="row.isFirst == 0 ? 'success' : 'info'" effect="light"
                            style="width: 100px; height: 30px; font-weight: 700; font-size: 1.5vh; border-radius: 12px;">
                            {{ row.isFirst == 0 ? '初诊' : '复诊' }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="diagnosis" label="诊断" :formatter="convertDiagnosis"
                    show-overflow-tooltip />
                <el-table-column align="center" label="操作" fixed="right">
                    <template #default="{ row }">
                        <el-popover placement="right-start" :width="300" trigger="hover">
                            <template #default>
                                <div style="display: flex; align-items: center;">
                                    <el-icon color="#8cc63f"
                                        style="font-size: 2em; border: 2px solid #8cc63f; border-radius: 25px; padding: 5px;">
                                        <ArrowRightBold />
                                    </el-icon>
                                    <span style="margin-left: 20px; font-size: 1.5em;">查看接诊记录</span>
                                </div>
                            </template>
                            <template #reference>
                                <el-button @click.prevent="method.toCure(row)"
                                    :color="row.color == undefined ? '' : row.color" circle
                                    style="border: 2px solid #8cc63f; font-size: 20px; width: 36px; height: 36px;">
                                    <el-icon color="#8cc63f">
                                        <ArrowRightBold />
                                    </el-icon>
                                </el-button>
                            </template>
                        </el-popover>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination v-model:current-page="current" v-model:page-size="size" v-model:total="total"
                :page-sizes="[10, 20, 50, 100, 500]" @current-change="handle.pageCurrentChange"
                @size-change="handle.pageCurrentChange" style="height: 10%; padding: 0 5%; font-size: 1.2vh" background
                layout="total, sizes, ->, prev, pager, next, jumper" size="large" />
        </el-card>
    </div>
</template>
<script setup>
import { ArrowRightBold } from '@element-plus/icons-vue'
import { ref, reactive, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { convertDate, convertSex, convertName, convertDiagnosis, convertIsFirst, convertAge, convertPhone, } from '@/utils/converter.js'

const routerInstance = useRouter()

import { admissionService } from '@/api/clinic/index.js'

const formRef = ref()

const form = reactive({})
const tableData = ref([])

// 分页组件参数
const current = ref(1)
const size = ref(10)
const total = ref(0)

watch(
    () => form.val,
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
        admissionService.search({ ...this.getPage(), value: form.val, createTime: form.createTime }, (response) => {
            tableData.value = response.records
            this.setPage(response)
        })
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
        form.createTime = null
        form.val = null
        method.search()
    },
    pageCurrentChange() {
        method.search()
    },
}

onMounted(() => {
    let routerQuery = routerInstance.currentRoute.value.query
    if (routerQuery && routerQuery.val) {
        form.val = routerQuery.val
    }
    method.search()
})
</script>
<style scoped>
:deep(.el-card__body) {
    height: 100%;
    padding: 0;
}

:deep(.el-table__header) {
    height: 6vh;
    background-color: #6a5acd;
}

:deep(.el-table__header .cell) {
    font-weight: 700;
}

:deep(.el-form-item) {
    margin-bottom: 0;
}
</style>