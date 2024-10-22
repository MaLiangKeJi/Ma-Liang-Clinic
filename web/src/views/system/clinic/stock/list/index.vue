<template>
    <div style="height: 100vh; padding: 2%;">
        <div style="height: 10%; display: flex; justify-content: space-between;">
            <div style="width: 10%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
                <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">全部库存</el-text>
            </div>
            <div style="width: 90%; height: 100%; display: flex; align-items: center; justify-content: flex-end;">
                <SearchModule 
                    ref="searchModuleRef"
                    :form="searchForm"
                    @search="search"
                    @reset="method.resetSearchFields"
                    @quickAdd="method.quickAdd"
                    @manuallyAdd="method.manuallyAdd"
                    @selectStockName="handle.stockNameSelect"
                    @selectTag="handle.tagSelect"
                />
            </div>
        </div>
        <el-card style="height: 85%; border-radius: 15px;">
            <!-- 表格 -->
            <div style="height: 90%">
                <TableModule 
                    :tableData="tableData"
                    @addStock="method.addStock"
                    @del="method.delStock"
                    @edit="method.editStock"
                />
            </div>
            <el-pagination
                v-model:current-page="current"
                v-model:page-size="size"
                v-model:total="total"
                :page-sizes="[10, 20, 50, 100, 500]"
                @current-change="handle.pageCurrentChange"
                @size-change="handle.pageCurrentChange"
                style="height: 10%; padding: 0 5%; font-size: 1.2vh"
                background
                layout="total, sizes, ->, prev, pager, next, jumper"
                size="large"
            />
        </el-card>
    </div>
</template>
  
<script setup>
import { ref, provide, reactive, onMounted } from 'vue'

import { drugService, stockService } from '@/api/clinic/index.js'
import SearchModule from './search.vue'
import TableModule from './table.vue'

import { visible as addDrugPageVisible, show as showAddDrugPage, hidden as hiddenAddDrugPage } from './addDrugPage.js'
import { show as showSearchDrugPage } from './searchDrugPage.js'
import { current, size, total, getPage, setPage } from './page'

const searchModuleRef = ref()

const data = {
    units: ref(),
    roles: ref(),
}

const newStockTableData = ref([])

provide('newStocks', newStockTableData)
provide('units', data.units)
const editForm = ref({})
provide('selectedNewStock', editForm)

provide('addVisvible', addDrugPageVisible)

const editVisvible = ref(false)
provide('editVisvible', editVisvible)
const showEditPage = () => editVisvible.value = true
const closeEditPage = () => editVisvible.value = false

const method = {
    /**
     * 重置搜索框 & 刷新表格
     */
    resetSearchFields: () => {
        searchModuleRef.value.reset()
        search()
    },
    /**
     * 快速添加药品
     */
    quickAdd: () => {
        showAddDrugPage()
        showSearchDrugPage()
    },
    /**
     * 手动添加药品
     */
    manuallyAdd: () => {
        editForm.value = {}
        showAddDrugPage() 
        showEditPage()
    },
    addStock: ($event, row) => {
        editForm.value = {}
        row.type = row.dosageForm
        row.unitObj = row.stockUnitList[row.stockUnitList.length - 1].unitList[0]
        row.approvalNumberName = row.approvalNumber.slice(4)
        row.selectUnits = []
        row.stockUnitList.forEach(stockUnit => {
            let unit = stockUnit.unitList[0]
            unit.number = stockUnit.stepSize
            unit.sort = stockUnit.sort
            row.selectUnits.push(unit)
        });
        row.countType = row.stateCountRule
        row.countNumber = row.countVal
        row.countUnit = row.selectUnits[0].id
        row.priceUnit = row.selectUnits[0].id
        newStockTableData.value.push(row)
        editForm.value = row
        showAddDrugPage()
        showEditPage()
    },
    async delStock(index, row) {
        stockService.removeStock(row.id, () => search())
    }
}

provide('closeAddPage', () => resetSearchFields() && hiddenAddDrugPage())

const selectedDrug = ref()


const tableData = ref([])

const searchForm = reactive({
    id: null,
    name: "",
    state: null,
    tag: null,
    createDate: null,
    productionDate: null,
    current: 1,
    size: 10,
    total: 1,
})

const handle = {
    pageCurrentChange: () => search(),
    stockNameSelect: (name) => {
        searchForm.name = name
        search()
    },
    tagSelect: () => {

    },
}

onMounted(() => {
    search()
    stockService.getRole((response) => data.roles.value = response)
    drugService.getUnit((response) => data.units.value = response)
})

const search = () => {
    stockService.searchStock(searchForm, (response) => {
        let data = []
        response.records.forEach(item => {
            item.batchList.forEach(batch => {
                batch.stockId = item.id
                batch.alias = item.alias
                batch.name = item.name

                batch.numberUnitName = batch.stockUnitList[0].unitList[0].name
                for (let index = 0; index < batch.stockUnitList.length - 1; index++) {
                    const stockUnit = batch.stockUnitList[index];
                    let parentNumber = batch.number / stockUnit.stepSize
                    if(parentNumber >= 1) {
                        batch.number = Math.ceil(parentNumber)
                        batch.numberUnitName = batch.stockUnitList[index + 1].unitList[0].name
                    } else return
                }

                
                batch.typeName = batch.typeObj.msg

                data.push(batch)
            });
        });
        tableData.value = data
        setPage(response.current, response.total)
    })
}
</script>
<style scoped>
:deep(.el-card__body) {
    height: 100%;
    padding: 0;
}
</style>