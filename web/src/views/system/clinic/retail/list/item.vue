<template>
    <el-table 
        :data="props.data" :border="true" 
        :cell-style="{ borderColor:'#C0C0C0' }" 
        :header-cell-style="{ background:'#f5f7fa', color: '#000', borderColor: '#C0C0C0'}" 
        class="exporttable"
        style="font-size: 1.2em;" 
    >
        <el-table-column type="index" label="序号" width="100" />
        <el-table-column prop="name" label="药品名称" width="400" />
        <el-table-column prop="dosageForm" label="剂型" width="120" />
        <el-table-column prop="spec" label="规格" width="300" />
        <el-table-column prop="manufacturer" label="厂商名称" width="600"  />
        <el-table-column prop="batchNumber" label="批次号" width="200" />
        <el-table-column prop="produceDate" label="生产日期" width="200">
            <template #default="{ row }">
                {{ convertDate(row.produceDate) }}
            </template>
        </el-table-column>
        <el-table-column prop="expiryDate" label="过期日期" width="200">
            <template #default="{ row }">
                {{ convertDate(row.expiryDate) }}
            </template>
        </el-table-column>
        <el-table-column prop="number" label="数量" width="120">
            <template #default="{ row }">
                {{ row.number + row.unit }}
            </template>
        </el-table-column>
        <el-table-column prop="price" label="单价（元）" width="200" />
        <el-table-column prop="remark" label="备注" />
    </el-table>
</template>
<script setup>
import { defineProps } from 'vue'

const props = defineProps(['data'])

function convertDate(cellValue){   //定义日期格式转换函数
    let date = new Date(cellValue)
    let year = date.getFullYear();
    let month = String(date.getMonth() + 1).padStart(2, '0');
    let day = String(date.getDate()).padStart(2, '0');
    let dateString = `${year}/${month}/${day}`;
    return dateString  //拼接成yyyy-mm-dd形式的字符串
}
</script>
<style scoped>
:deep(.el-table__header) {
    height: 50px !important;
    background-color: #fff;
    padding: 0 !important;
}
:deep(.el-table__row > .el-table__cell) {
    padding-left: 0 !important;
}
</style>