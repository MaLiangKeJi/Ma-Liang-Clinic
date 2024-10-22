<template>
    <el-card style="height: 80%; margin: 2%; border: 15px;">
        <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
            <el-tab-pane label="单笔记录" name="singleEntry">
                <el-table :data="tableData" style="height: 80%; width: 100%;">
                    <el-table-column prop="createTimeStr" label="支付日期" />
                    <el-table-column prop="orderId" label="订单号" />
                    <el-table-column prop="packageType" label="套餐类型">
                        <template #default="{ row }">
                            {{ row.packageType == 1 ? '月付' : (row.packageType == 2 ? '季付' : (row.packageType == 3 ? '年付' : '测试')) }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="name" label="用户名">
                        <template #default="{ row }">
                            {{ row.user.name }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="phone" label="手机号">
                        <template #default="{ row }">
                            {{ row.user.phone }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="phone" label="手机号">
                        <template #default="{ row }">
                            {{ row.user.phone }}
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
            </el-tab-pane>
            <el-tab-pane label="累计" name="second">
                <el-table :data="cumulativePaymentRecord" style="height: 80%; width: 100%;">
                    <el-table-column prop="name" label="用户名">
                        <template #default="{ row }">
                            {{ row.user.name }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="phone" label="手机号">
                        <template #default="{ row }">
                            {{ row.user.phone }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="phone" label="手机号">
                        <template #default="{ row }">
                            {{ row.user.phone }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="phone" label="支付累计金额">
                        <template #default="{ row }">
                            {{ row.money }}
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination
                    v-model:current-page="cumulativePaymentRecordCurrent"
                    v-model:page-size="cumulativePaymentRecordSize"
                    v-model:total="cumulativePaymentRecordTotal"
                    :page-sizes="[10, 20, 50, 100, 500]"
                    @current-change="handle.searchCumulativePaymentRecord"
                    @size-change="handle.searchCumulativePaymentRecord"
                    style="height: 10%; margin-top: 5%; padding: 0 5%; font-size: 1.2vh"
                    background
                    layout="total, sizes, ->, prev, pager, next, jumper"
                    size="large"
                />
            </el-tab-pane>
        </el-tabs>
    </el-card>
</template>
  
<script setup>
import { ref, onMounted } from 'vue'
import { searchRenewLogs as searchRenewLogAPI, searchCumulativePaymentRecord as searchCumulativePaymentRecordAPI } from '@/api/back.api.js'

// 分页组件参数
const current = ref(1)
const size = ref(10)
const total = ref(0)

const tableData = ref([])

const activeName = ref('singleEntry')

const cumulativePaymentRecord = ref([])
const cumulativePaymentRecordCurrent = ref()
const cumulativePaymentRecordSize = ref()
const cumulativePaymentRecordTotal = ref()

onMounted(() => {
    handle.search()
    handle.searchCumulativePaymentRecord()
})

const handle = {
    pageCurrentChange() { search(); },
    search() {
        searchRenewLogAPI({ current: current.value, size: size.value, }, (res) => {
            tableData.value = res.records
            total.value = res.total
        })
    },
    searchCumulativePaymentRecord() {
        searchCumulativePaymentRecordAPI({ current: cumulativePaymentRecordCurrent.value, size: cumulativePaymentRecordSize.value, }, res => {
            cumulativePaymentRecord.value = res.records
            cumulativePaymentRecordTotal.value = res.total
        })
    },
}
</script>