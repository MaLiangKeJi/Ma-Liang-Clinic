
<template>
<el-card>
    <el-table :data="tableData" style="width: 100%;" scrollbar-always-on>
        <el-table-column prop="id" label="Log ID" width="90" />
        <el-table-column prop="userId" label="用户 ID" width="90" />
        <el-table-column prop="createTime" label="记录时间" width="150">
            <template #default="{row}">
                {{format(row.createTime)}}
            </template>
        </el-table-column>
        <el-table-column prop="service" label="业务" width="120" />
        <el-table-column prop="location" label="位置" width="410" />
        <el-table-column prop="operation" label="操作内容" width="450"/>
        <el-table-column prop="level" label="级别" fixed="right" width="100">
            <template #default="{$index, row}">
                <el-text class="mx-1" :type="method.computLogLevel(row)">{{method.computLogLevelStr(row)}}</el-text>
            </template>
        </el-table-column>
    </el-table>
    <el-pagination
        v-model:current-page="current"
        v-model:page-size="size"
        v-model:total="total"
        @current-change="handle.pageCurrentChange"
        style="margin-top: 50px;"
        background
    />
</el-card>    
</template>
<script setup>
import { ref, onMounted, } from 'vue'
import { logService, } from '@/api/clinic/index.js'

const tableData = ref([])
const current = ref(1)
const size = ref(10)
const total = ref(0)

const method = {
    setPage(page) {
        current.value = page.current
        total.value = page.total
    },
    getPage() {
        return { 
            current: current.value, 
            size: size.value 
        }
    },
    search() {
        logService.search({ ...this.getPage() }, (res) => {
            tableData.value = res.records
            total.value = res.total
        })
    },
    computLogLevel(row) {
        let type = 'info'
        switch(row.level) {
            case 0:
                type = ''
                break;
            case 10:
                type = 'info'
                break;
            case 20:
                type = 'info'
                break;
            case 30:
                type = 'warning'
                break;
            case 40:
                type = 'danger'
                break;
        }
        return type
    },
    computLogLevelStr(row) {
        let str = 'INFO'
        switch(row.level) {
            case 0:
                str = 'TRACE'
                break;
            case 10:
                str = 'DEBUG'
                break;
            case 20:
                str = 'INFO'
                break;
            case 30:
                str = 'WARN'
                break;
            case 40:
                str = 'ERROR'
                break;
        }
        return str
    },
}

const handle = {
    pageCurrentChange() {
        method.search()
    },
}

onMounted(() => {
    method.search()
})

function format(dateStr) {
      let date = new Date(dateStr)
      let year = date.getFullYear();
      let month = String(date.getMonth() + 1).padStart(2, '0');
      let day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
}

</script>