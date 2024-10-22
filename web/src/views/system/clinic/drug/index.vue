<template>
    <!-- 搜索框 -->
    <el-form ref="formRef" :model="searchForm" label-width="120px">
        <el-row :gutter="24">
            <el-col :span="6" style="min-width: 300px;">
                <el-form-item prop="name" label="药品名称">
                    <el-autocomplete
                        v-model="searchForm.name"
                        :fetch-suggestions="(queryString, cb) => search.name(queryString, ({ records }) => cb(records))"
                        @select="handle.drugNameSelected"
                        value-key="name"
                        :trigger-on-focus="false"
                    />
                </el-form-item>
            </el-col>
            <el-col :span="6" style="min-width: 300px;">
                <el-form-item prop="manufacturerName" label="生产厂家">
                    <el-autocomplete
                        v-model="searchForm.manufacturer"
                        :fetch-suggestions="(queryString, cb) => search.manufacturer(queryString, ({ records }) => cb(records))"
                        @select="handle.manufacturerSelected"
                        value-key="manufacturer"
                        :trigger-on-focus="false"
                    />
                </el-form-item>
            </el-col>
        </el-row>
        <el-row :gutter="24">
            <el-col :span="6" style="min-width: 300px;">
                <el-form-item prop="approvalNumber" label="批准文号">
                    <el-input v-model="searchForm.approvalNumber" />
                </el-form-item>
            </el-col>
            <el-col :span="6" style="min-width: 300px;">
                <el-form-item prop="drugNo" label="药品编码">
                    <el-input v-model="searchForm.drugNo" />
                </el-form-item>
            </el-col>
             <!-- 按钮 -->
            <el-col :span="4" >
                <el-button type="primary" @click="search.table">查询</el-button>
                <el-button @click="reset">重置</el-button>
            </el-col>
        </el-row>
    </el-form>
    <!-- 表格 -->
    <el-table :data="tableData" stripe style="width: 100%; height: 100%;">
        <el-table-column fixed property="name" label="药品名称" />
        <el-table-column property="type" label="剂型" />
        <el-table-column property="spec" label="规格"  />
        <el-table-column property="manufacturer" label="厂商"  />
        <el-table-column property="drugNo" label="药品编码"  />
        <el-table-column property="approvalNumber" label="批准文号"  />
        <el-table-column property="number" label="数量" fixed="right" width="200" >
            <template #default="{ row }">
                <el-input-number v-model="row.number" style="width: 100%;" class="mx-4" :min="1" :max="50" />
            </template>
        </el-table-column>
        <el-table-column property="unit" label="单位" fixed="right" width="60">
            <template #default="{ row }">
                箱
            </template>
        </el-table-column>
        <el-table-column label="添加" fixed="right" width="100">
            <template #default="{ $index, row }">
                <el-button 
                    type="success" 
                    :disabled="row.isAdd"
                    @click="handle.addDrug($index, row)"
                >
                    <el-icon>
                        <Plus v-show="!row.isAdd" />
                        <Check v-show="row.isAdd" />
                    </el-icon>
                </el-button>
            </template>
        </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
        v-model:current-page="current"
        v-model:page-size="size"
        v-model:total="total"
        @current-change="handle.pageCurrentChange"
        style="margin-top: 50px;"
        background
    />
</template>
<script setup>
import { ref, reactive, inject, onMounted, defineEmits } from 'vue'
import { drugService } from '@/api/clinic/index.js'

const newStocks = inject('newStocks')
const units = inject('units')

const event = defineEmits(['add'])


const searchForm = reactive({
    manufacturerId: null,
    name: null,
    manufacturer: null,
    approvalNumber: null,
    drugNo: null,
})

const addIds = ref([])

// 表格数据
const tableData = ref([])

// 分页组件参数
const current = ref(1)
const size = ref(10)
const total = ref(0)
const getPage = () => ({ current: current.value, size: size.value, total: total.value })
const setPage = (pageCurrent, pageTotal) => {
    current.value = pageCurrent
    total.value = pageTotal
}

// 设置页面数据（表格 + 分页）
const setTableData = ({ records, current, total }) => {
    let result = []
    records.forEach(drug => {
        let isAdd = false
        let obj
        newStocks.value.forEach(item => {
            if(drug.id == item.id) {
            // 已经选择过的药品，只需要重置按钮状态
                obj = item
                isAdd = true
                return
            }
        });
        if(!isAdd) {
            // 未选择的药品，重置属性
            obj = drug
            obj.number = 1
            obj.unit = 1
            obj.unitObj = units.value[0]
        }
        obj.isAdd = isAdd
        result.push(obj)
    });
    tableData.value = result
    setPage(current, total)
}

// 事件处理
const handle = {
    addDrug: (index, row) => {
        row.unitObj = units.value[row.unit - 1]
        row.skinTest = false
        row.index = newStocks.value.length
        row.sort = row.approvalNumber.charAt(4)
        newStocks.value.push(row)
        row.approvalNumberName = row.approvalNumber.slice(4, row.approvalNumber.length)
        row.isAdd = true
        if(addIds.value.filter(id => id == row.id).length == 0) {
            addIds.value.push(row.id)
        }
    },
    pageCurrentChange: () => search.table(),
    drugNameSelected: (item) => search.name(item.name, response => setTableData(response)),
    manufacturerSelected: (item) => search.name(item.manufacturer, response => setTableData(response)),
}

const formRef = ref() // 获取表单元素

// 表单重置
const reset = () => {
    formRef.value.resetFields()
    search.table()
}


onMounted(() => {
    search.table()
})

// 查询操作
const search = {
    table: () => drugService.search(Object.assign({}, searchForm, getPage()), (response) => setTableData(response)),
    name: (queryString, callback) => {
        if(queryString != null && queryString != 'null') {
            drugService.search({
                name: queryString,
                current: 1,
                size: 10,
            }, callback)
        }
    },
    manufacturer: (queryString, callback) => {
        if(queryString != null && queryString != 'null') {
            drugService.search({ 
                manufacturer: queryString,
                current: 1,
                size: 10,
            }, callback)
        }
    },
}
</script>