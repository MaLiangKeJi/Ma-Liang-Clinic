<template>
    <div style="height: 100vh; padding: 2%;">
        <el-form ref="formRef" :model="form" style="height: 10%; display: flex; justify-content: space-between;">
            <div style="width: 10%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
                <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">入库记录</el-text>
            </div>
            <div style="width: 90%; height: 100%; display: flex; align-items: center; justify-content: flex-end;">
                <el-form-item style="width: 10%;">
                    <el-text class="mx-1" style='width: 100%; height: 100%; text-align: right; font-size: 1vw; padding-right: 10%;'>药品/批号</el-text>
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-input v-model='form.val' @change='method.search()' clearable='true' placeholder="药品名称、生产批号等"
                        class="search_input" style='width: 100%;'                        
                    />
                </el-form-item>
                <el-form-item style="width: 10%;">
                    <el-text class="mx-1" style="width: 100%; height: 100%; text-align: right; font-size: 1vw; padding-right: 10%;">入库时间</el-text>
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-date-picker
                        v-model="searchForm.createTime"
                        type="datetimerange"
                        range-separator="到"
                        start-placeholder="起始时间"
                        end-placeholder="结束时间"
                        class="search_input"
                        @change='method.search()'
                        style="width: 60%; border-radius: 15px;"
                    />
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-button class="search_button" @click='method.reset()' style="width: 10vw; border-radius: 15px; height: 5vh !important;"  type="primary" plain>
                        重置
                    </el-button>
                </el-form-item>
            </div>
        </el-form>
        <el-card style="height: 85%; border-radius: 15px;">
            <el-table 
                :data="tableData" 
                height="100%"
                style="height: 90%; width: 100%; font-size: 1.5vh;" 
                :row-style="{ height: '5vh' }" 
                :header-cell-style="{ background:'#6a5acd', color:'#fff' }"
                scrollbar-always-on
                @row-click="(row) => method.toCure(row)"
            >
                <el-table-column align="center" prop="no" label="入库编号" min-width="150">
                    <template #default="{ row }">
                        {{ row.stockInDrugs.no.length > 10 ? row.stockInDrugs.no.slice(0, 6) + '...' : row.stockInDrugs.no }}
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="name" label="药品名称" min-width="200">
                    <template #default="{row}">
                        {{ row.stockInDrugs.name }}
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="dosageForm" label="剂型" min-width="200">
                    <template #default="{row}">
                        {{ row.stockInDrugs.dosageForm }}
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="spec" label="规格" min-width="150">
                    <template #default="{row}">
                        {{ row.stockInDrugs.spec }}
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="approvalNumber" label="批准文号" min-width="200">
                    <template #default="{row}">
                        {{ row.stockInDrugs.approvalNumber }}
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="batchNumber" label="生产批号" min-width="200">
                    <template #default="{row}">
                        {{ row.stockInDrugs.batchNumber.length > 10 ? row.stockInDrugs.batchNumber.slice(0, 6) + '...' : row.stockInDrugs.batchNumber }}
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="produceDate" label="生产日期" min-width="200">
                    <template #default="{row}">
                        {{ format(row.stockInDrugs.produceDate) }}
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="expiryDate" label="有效期" min-width="200">
                    <template #default="{row}">
                        {{ format(row.stockInDrugs.expiryDate) }}
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="manufacturer" label="生产厂商" min-width="600">
                    <template #default="{row}">
                        {{ row.stockInDrugs.manufacturer }}
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="provider" label="供货单位" min-width="200">
                    <template #default="{row}">
                        {{ row.stockInDrugs.provider }}
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="number" label="购货数量" min-width="200">
                    <template #default="{row}">
                        {{ row.stockInDrugs.number +row.stockInDrugs.unitName}}
                    </template>
                </el-table-column>
            
                <el-table-column align="center" prop="cost" min-width="200">
                    <template #header>
                        <el-tooltip
                            class="box-item"
                            effect="dark"
                            content="购货单位，单个成本价格"
                            placement="top"
                        >
                            <span>购进价格</span>
                        </el-tooltip>
                    </template>
                    <template #default="{row}">
                        {{ row.stockInDrugs.cost + row.stockInDrugs.costUnit}}
                    </template>
                </el-table-column>
          
                <el-table-column align="center" prop="totalCost" label="总进价" min-width="200">
                    <template #default="{row}">
                        {{ row.stockInDrugs.totalCost + row.stockInDrugs.totalCostUnit}}
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="acceptResult" label="验收结论" min-width="200">
                    <template #default="{row}">
                        {{ row.stockInDrugs.acceptResult }}
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="createTime" label="购货日期" min-width="200">
                    <template #default="{row}">
                        {{ format(row.stockInDrugs.createTime) }}
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="createName" label="验收人签名" min-width="200">
                    <template #default="{row}">
                        {{ row.stockInDrugs.createName }}
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="remark" label="备注" min-width="400">
                    <template #default="{row}">
                        {{ row.stockInDrugs.remark }}
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination
                v-model:current-page="searchForm.current"
                v-model:page-size="searchForm.size"
                v-model:total="searchForm.total"
                :page-sizes="[10, 20, 50, 100, 500]"
                @current-change="method.search"
                @size-change="method.search"
                style="height: 10%; padding: 0 5%; font-size: 1.2vh"
                background
                layout="total, sizes, ->, prev, pager, next, jumper"
                size="large"
            />
        </el-card>
    </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'

import { isArr, } from '@/utils/arrayUtils.js';

import { stockService } from '@/api/clinic/index.js'

const formRef = ref()

const form = reactive({})
const tableData = ref([])

const searchForm = ref({
    current: 1,
    size: 10,
    total: 0,
})
const searchFormVal = {
    name: null,
    createTime: null,
    current: 1,
    size: 10,
    total: 1,
}

const method = {
    search() {
        searchForm.value.name = form.val;
        initTime();
        stockService.searchStockIn(searchForm.value).then(({ data }) => {
            tableData.value = data.data.records;
        })
    },
    convertDate(row, column, cellValue, index){   //定义日期格式转换函数
        let date = new Date(cellValue)
        let year = date.getFullYear();
        let month = String(date.getMonth() + 1).padStart(2, '0');
        let day = String(date.getDate()).padStart(2, '0');
        let dateString = `${year}/${month}/${day}`;
        return dateString  //拼接成yyyy-mm-dd形式的字符串
    },
    reset() {
        searchForm.value = { ...searchFormVal }
        form.val = null;
        this.search();
    },

}

/**初始化入库时间列表 */
function initTime() {
    if (isArr(searchForm.value.createTime)) {
        const dateArr = searchForm.value.createTime;
        searchForm.value.createTimes = [dateArr[0].getTime(), dateArr[1].getTime()];
    } else
        searchForm.value.createTimes = [];
}

function format(dateStr) {
    let date = new Date(dateStr)
    let year = date.getFullYear();
    let month = String(date.getMonth() + 1).padStart(2, '0');
    let day = String(date.getDate()).padStart(2, '0');
    return `${year}/${month}/${day}`;
}



onMounted(() => {
    method.search()
    method.reset()
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

.exporttable {
    border: solid 1px #000000;
}
:deep(.el-descriptions__label) {
    width: 120px;
}
:deep(.el-descriptions__body .el-descriptions__table.is-bordered .el-descriptions__cell) {
    border-color: #000000;
}
:deep(.el-select__wrapper) {
    height: 100%;
}
</style>