<template>
    <!-- 药品添加 -->
    <el-row justify="space-between" style="margin-bottom: 20px;">
        <el-col :span="6" style="min-width: 300px;">
            <el-button @click="event('search-drug')" :icon="Search">搜索药品</el-button>
            <el-button @click="manuallyAdd" :icon="Search">手动添加</el-button>
        </el-col>
        <el-col :span="4">
            <el-button @click="batchRemove">批量删除</el-button>
        </el-col>
    </el-row>
    <!-- 表格 -->
    <el-table :data="newStocks" ref="tabletRef" style="width: auto;" >
        <!-- 自定义选择框（El 选择框无法获取下标） -->
        <el-table-column fixed width="50">
            <template #header>
                <el-checkbox v-model="isAllSelected" size="large" />
            </template>
            <template #default="{ row }">
                <div v-if="!isAllSelected">
                    <el-checkbox v-model="row.isSelected" size="large" />
                </div>
                <div v-else>
                    <el-checkbox :checked="true" size="large" />
                </div>
            </template>
        </el-table-column>
        <el-table-column fixed prop="name" label="名称" width="150" />
        <el-table-column prop="type" label="剂型" width="180" />
        <el-table-column prop="spec" label="规格" width="180" />
        <el-table-column prop="manufacturer" label="厂商" width="180" />
        <el-table-column prop="number" label="数量" width="60" />
        <el-table-column prop="unitObj.name" label="单位" width="60" />
        <el-table-column prop="approvalNumber" label="批准文号" width="180" />
        <el-table-column prop="batchNumber" label="生产批号" width="180" />
        <el-table-column label="备注" />
        <el-table-column fixed="right" label="操作" width="200">
            <template #default="scope">
                <el-button size="small" type="primary" @click="showEdit(scope.$index, scope.row)">
                    检查
                </el-button>
                <el-button size="small" type="danger" @click="remove(scope.$index)" >
                    删除
                </el-button>
            </template>
        </el-table-column>
    </el-table>

    <el-form-item label="备注" style="margin-top: 50px;">
        <el-input v-model="form.remark" type="textarea" :autosize="{ minRows: 5, maxRows: 9 }" />
    </el-form-item>

    <!-- 入库 -->
    <el-row style="margin-top: 2%;" :gutter="20">
        <el-col :span="10">
            <el-button type="success" @click="submit" style="width: 200px; height: 50px;">入库</el-button>
            <el-button @click="closeAddPage()" style="width: 200px; height: 50px;">取消</el-button>
        </el-col>
    </el-row>
 

    <!-- 编辑弹窗 -->
    <el-dialog
        v-model="editVisvible"
        width="80%"
    >
        <!-- <Edit /> -->

        <template #footer>
            <span class="dialog-footer">
                <el-button type="primary">修改</el-button>
                <el-button @click="searchDrubVisible = true">取消</el-button>
            </span>
        </template>
    </el-dialog>
</template>
<script setup>
import { ref, inject, defineProps, defineEmits } from 'vue'
import { Search } from '@element-plus/icons-vue'
// import Edit from '@/views/system/clinic/stock/components/edit/index.vue'
import { stockService } from '@/api/clinic/index.js'

const tabletRef = ref()


const newStocks = inject('newStocks')

const addVisvible = inject('addVisvible')

const closeAddPage = inject('closeAddPage')

const props = defineProps(['form', 'tableData'])

const event = defineEmits(['add', 'search-drug'])

const form = ref({})
const editForm = inject('selectedNewStock')

const isAllSelected = ref(false)

const batchRemove = () => {
    if(isAllSelected.value) {
        newStocks.value.forEach(newStock => {
            newStock.isAdd = false
            newStock.isSelected = false
        })
        newStocks.value = []
        isAllSelected.value = false
    } else {
        let notSelectStocks = []
        for (let index = 0; index < newStocks.value.length; index++) {
            const newStock = newStocks.value[index];
            if(newStock.isSelected) {
                newStock.isAdd = false
                newStock.isSelected = false
                continue
            }
            notSelectStocks.push(newStock)
        }
        newStocks.value = notSelectStocks
    }
}

const manuallyAdd = () => {
    editForm.value = {}
    editVisvible.value = true
}

const editVisvible = inject('editVisvible')
const showEdit = (index, row) => {
    editForm.value = {}
    editForm.value = row 
    editVisvible.value = true
}

const remove = (index) => {
    let deleteDrug = newStocks.value[index]
    newStocks.value.splice(index, 1)
    deleteDrug.isAdd = false
}

const searchDrubVisible = ref(false)

const submit = () => {
    let approvalNumbers = [], batchNumbers = []
    let batchList = []
    let totalCost = 0
    newStocks.value.forEach(drug => {
        approvalNumbers.push(drug.approvalNumber)
        batchNumbers.push(drug.batchNumber)
        let number = drug.number   // 数量（最大单位，如n 箱 ps箱/盒/瓶/片）
        let unitId = drug.selectUnits[drug.selectUnits.length - 1].id
        let unitList = []
        let cost = drug.cost
        for (let index = 0; index < drug.selectUnits.length; index++) {
            let unit = drug.selectUnits[index];
            let stepSize = drug.number
            if(index > 0) { // 跳过最大单位（index:0 最大单位）
                stepSize = unit.number
                number = number * unit.number
                cost = cost / unit.number
            }
            unitList.push({
                unitId: unit.id,
                sort: index,
                stepSize,
            })
        }
        batchList.push({
            name: drug.name,
            approvalNumber: drug.approvalNumber,
            manufacturer: drug.manufacturer,
            batchNumber: drug.batchNumber,
            produceDate: drug.produceDate,
            expiryDate: drug.expiryDate,
            dosageForm: drug.type,
            spec: drug.spec,
            typeName: drug.sort,
            essential: drug.essential,
            skinTest: drug.skinTest ? 1 : 0,
            drugUsage: drug.usage,
            singleDose: drug.singleDose,
            singleDoseUnit: drug.singleDoseUnit,
            frequency: drug.frequency,
            number,
            unitId: unitId,
            stateCountRule: drug.countType,
            countVal: drug.countNumber,
            countUnitId: drug.countUnit,
            stockUnitList: unitList,
            provider: drug.provider, 
            unitName: drug.selectUnits[drug.selectUnits.length - 1].name,
            cost,
        })
        totalCost += drug.cost
    });
    let params = {
        batchNumbers,
        approvalNumbers,
        batchList,
        remark: form.remark,
        totalCost,
    }
    stockService.add(params, (response) => closeAddPage())
}

</script>
<style>
.radius {
  height: 40px;
  width: 70%;
  border: 1px solid var(--el-border-color);
  border-radius: 0;
  margin-top: 20px;
}
</style>