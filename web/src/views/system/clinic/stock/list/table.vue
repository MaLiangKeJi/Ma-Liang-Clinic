<template>
    <el-table 
        :data="props.tableData" 
        height="100%"
        style="height: 90%; width: 100%; font-size: 1.5vh;" 
        :row-style="{ height: '5vh' }" 
        :header-cell-style="{ background:'#6a5acd', color:'#fff' }"
        scrollbar-always-on
    >
        <el-table-column align="center" property="name" label="名称" fixed>
            <template #default="{row}">
                {{row.name}}
            </template>
        </el-table-column>
        <el-table-column align="center" property="manufacturer" label="厂商">
            <template #default="{row}">
                {{row.manufacturer}}
            </template>
        </el-table-column>
        <el-table-column align="center" property="approvalNumber" label="批准文号">
            <template #default="{row}">
                {{row.approvalNumber}}
            </template>
        </el-table-column>
        <el-table-column align="center" property="factory" label="批次号">
            <template #default="{row}">
                {{row.batchNumber}}
            </template>
        </el-table-column>
        <el-table-column align="center" property="type" label="类型">
            <template #default="{row}">
                {{row.typeName}}
            </template>
        </el-table-column>
        <el-table-column align="center" property="dosageForm" label="剂型">
            <template #default="{row}">
                {{row.dosageForm}}
            </template>
        </el-table-column>
        <el-table-column align="center" property="spec" label="规格">
            <template #default="{row}">
                {{row.spec}}
            </template>
        </el-table-column>
        <el-table-column align="center" property="produceDate" width="120" label="生产日期" :formatter="method.formatDate" sortable />
        <el-table-column align="center" property="expiryDate" width="120" label="有效期至" :formatter="method.formatDate" sortable />
        <el-table-column align="center" property="inventory" label="库存" fixed="right">
            <template #default="{row}">
                <el-text class="mx-1" type="success" style="font-size: 1.2em; font-weight: 700;">
                    {{row.number + ' ' }}
                </el-text>
                {{row.numberUnitName}}
            </template>
        </el-table-column>
        <el-table-column align="center" width="170" label="操作" fixed="right">
            <template #default="{ row, $index }">
                <div style="display: flex;">
                    <!-- <el-button size="small" @click="event('edit', $index, row)" class="search_button" style="margin: 0 10px;">
                        修改
                    </el-button> -->
                    <el-button size="small" type="danger" @click="event('del', $index, row)" class="search_button" style="margin: 0 10px;">
                        删除
                    </el-button>
                </div>
            </template>
        </el-table-column>
    </el-table>

    <el-dialog v-model="addStockVisible" title="增加库存">
        <el-form-item label="药品:" >
            {{ form.name }}
        </el-form-item>
        <el-input-number v-model="form.number" :min="0.1" style="margin-left: 20px;"/>
        <el-select v-model="form.unitId" placeholder="Select" style="width: 100px; vertical-align: top">
            <el-option
                v-for="unit in form.unitList"
                :key="unit.id"
                :label="unit.name"
                :value="unit.id"
            />
        </el-select>
    </el-dialog>
</template>

<script setup>
import { ref, reactive, defineProps, defineEmits } from 'vue'

const props = defineProps(['tableData'])
const event = defineEmits(['addStock', 'edit', 'del'])

const form = reactive({
    number: 1,
    unitList: [],
})

const addStockVisible = ref(false)

const method = {
    formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        if(data == null) {
            return null
        }
        let dt = new Date(data)
            return dt.getFullYear() + '/' + ((dt.getMonth() + 1) > 9 ? '' : '0') + (dt.getMonth() + 1) + '/' + ((dt.getDate() < 10 ? '0' : '') + dt.getDate())
    },
}

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
.exporttable {
    border: solid 1px #000000;
}
:deep(.el-descriptions__label) {
    width: 120px;
}
:deep(.el-descriptions__body .el-descriptions__table.is-bordered .el-descriptions__cell) {
    border-color: #000000;
}
</style>