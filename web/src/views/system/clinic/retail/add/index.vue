<template>
    <div style="height: 100%; padding: 2%;">
        <el-form ref="formRef" :model="form" style="height: 10%; display: flex; justify-content: space-between;">
            <div style="width: 10%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
                <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">药品零售</el-text>
            </div>
        </el-form>
        <el-card style="height: 85%; border-radius: 15px;">
            <div v-if="isNotSubmit" style="height: 100%;">
                <el-scrollbar style="height: 80%; width: 100%;">
                    <el-form ref="formRef" :model="form" :rules="rules" label-width="10vw" style="height: 100%; width: 100%;">
                        <div style="height: 50%; padding: 2%;">
                            <h1>信息登记</h1>
                            <el-divider />
                            <el-row :gutter="24">
                                <el-col :span="8">
                                    <el-form-item prop="name">
                                        <template #label>
                                            <div style="height: 100%; display: flex; flex-direction: column; justify-content: center;">
                                                <span style="font-size: 1.2em;">姓名</span>
                                            </div>
                                        </template>
                                        <el-input v-model="form.name" class="search_input" style="width: 100% !important;" />
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item prop="sex" class="search_input" style="width: 100% !important;">
                                        <template #label>
                                            <div style="height: 100%; display: flex; flex-direction: column; justify-content: center;">
                                                <span style="font-size: 1.2em;">性别</span>
                                            </div>
                                        </template>
                                        <el-radio-group v-model="form.sex">
                                            <el-radio :value="1"><span style="font-size: 1.2em;">男</span></el-radio>
                                            <el-radio :value="0"><span style="font-size: 1.2em;">女</span></el-radio>
                                        </el-radio-group>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row :gutter="24">
                                <el-col :span="8">
                                    <el-form-item prop="age">
                                        <template #label>
                                            <div style="height: 100%; display: flex; flex-direction: column; justify-content: center;">
                                                <span style="font-size: 1.2em;">年龄</span>
                                            </div>
                                        </template>
                                        <el-input v-model="form.age" class="search_input" style="width: 100% !important; max-width: 300px;">
                                            <template #append>岁</template>
                                        </el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12" >
                                    <el-form-item prop="birthday">
                                        <template #label>
                                            <div style="height: 100%; display: flex; flex-direction: column; justify-content: center;">
                                                <span style="font-size: 1.2em;">出生日期</span>
                                            </div>
                                        </template>
                                        <el-date-picker v-model="form.birthday" type="date" class="search_input"  style="width: 100% !important;"/>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row :gutter="24">
                                <el-col :span="8">
                                    <el-form-item prop="phone">
                                        <template #label>
                                            <div style="height: 100%; display: flex; flex-direction: column; justify-content: center;">
                                                <span style="font-size: 1.2em;">手机号</span>
                                            </div>
                                        </template>
                                        <el-input v-model="form.phone" class="search_input" style="width: 100% !important; max-width: 300px;" />
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item prop="idCard">
                                        <template #label>
                                            <div style="height: 100%; display: flex; flex-direction: column; justify-content: center;">
                                                <span style="font-size: 1.2em;">证件</span>
                                            </div>
                                        </template>
                                        <el-input v-model="form.idCard" class="search_input" style="width: 100% !important;">
                                            <template #prepend>
                                                <el-select v-model="form.idCardType" style="height: 100%; width: 130px;">
                                                    <el-option label="居民身份证" :value="0" />
                                                    <el-option label="护照" :value="1" />
                                                </el-select>
                                            </template>
                                        </el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row :gutter="24">
                                <el-col :span="20">
                                    <el-form-item prop="address">
                                        <template #label>
                                            <div style="height: 100%; display: flex; flex-direction: column; justify-content: center;">
                                                <span style="font-size: 1.2em;">地址</span>
                                            </div>
                                        </template>
                                        <el-input v-model="form.address" class="search_input" style="width: 100% !important;" />
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row :gutter="24">
                                <el-col :span="8">
                                    <el-form-item prop="contact">
                                        <template #label>
                                            <div style="height: 100%; display: flex; flex-direction: column; justify-content: center;">
                                                <span style="font-size: 1.2em;">联系人</span>
                                            </div>
                                        </template>
                                        <el-input v-model="form.contact" class="search_input" style="width: 100% !important;" />
                                    </el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item prop="contactPhone">
                                        <template #label>
                                            <div style="height: 100%; display: flex; flex-direction: column; justify-content: center;">
                                                <span style="font-size: 1.2em;">联系人手机号</span>
                                            </div>
                                        </template>
                                        <el-input v-model="form.contactPhone" class="search_input" style="width: 100% !important;" />
                                    </el-form-item>
                                </el-col>
                            </el-row>
                        </div>
            
                        <div style="height: 50%; padding: 2%;">
                            <h1>零售药品</h1>
                            <el-divider />
                            <el-table 
                                :data="tableData" 
                                @cell-mouse-enter="handle.cellEnter"
                                style="margin-bottom: 300px; width: 100%; padding: 0 5%;"
                                :row-style="{height: '80px'}"
                            >
                                <el-table-column label="药品名称" prop="name" width="300" fixed>
                                    <template #default="{ row }">
                                    <div v-show="row.add" style="height: 100%;">
                                        <el-autocomplete
                                            v-model="row.name"   
                                            :fetch-suggestions="handle.queryDrugNameSearch"
                                            value-key="name"
                                            clearable
                                            class="inline-input w-50 search_input"
                                            placeholder="查询药品（别名、拼音首字母）"
                                            @select="handle.drugNameSelected($event, row)"
                                            :trigger-on-focus="true"
                                            style="width: 100% !important;"
                                        >
                                            <template #default="{ item }">
                                                <el-row :gutter="26" justify="space-between" style="width: 80vw; font-size: 1.2em; margin: 5px;" >
                                                <el-col :span="4">
                                                    <el-text class="mx-1" :type="item.isStock ? 'success' : ''" style="font-weight: 700; font-size: 1.2em;">{{ item.name }}</el-text>
                                                </el-col>
                                                <el-col :span="2">
                                                    <el-tag :type="item.isStock ? 'success' : 'info'">{{ item.isStock ? '系统库存' : '不在系统库存' }}</el-tag>
                                                    <el-tag v-if="item.expiryState == 2" type="danger" style="margin-left: 5px;">即将过期</el-tag>
                                                </el-col>
                                                <el-col :span="2" style="text-align: right;">
                                                    <span style="font-weight: 700;">{{ item.dosageForm }}</span>
                                                </el-col>
                                                <el-col :span="4">
                                                    <span style="font-weight: 700;">{{ item.spec }}</span>
                                                </el-col>
                                                <el-col :span="2">
                                                    <el-text class="mx-1" type="info">{{ item.manufacturer }}</el-text>
                                                </el-col>
                                                <el-col :span="4" style="text-align: right;">
                                                    <span :class="{ read: item.expiryState == 2 }">{{ item.expiryDate ? item.expiryDate + ' 过期' : '' }}</span>
                                                </el-col>
                                                <el-col :span="4" style="text-align: right;"> 
                                                    <span v-if="item.isStock" style="font-weight: 700;" :class="{ read: item.stateCountRule != 0 && item.stockState == 1 }">{{ item.stateCountRule == 0 ? '不统计库存' : (item.stockNumber ? item.stockNumber + ' ' + item.stockNumberUnit : '库存不足') }}</span>
                                                </el-col>
                                                </el-row>
                                            </template>
                                        
                                        </el-autocomplete>
                                    </div>
                                    <div v-show="!row.add" style="height: 100%;">
                                        <el-button type="success" :icon="Plus" @click="handle.addDrugClick(row)" style="width: 100%; height: 65px;">增加药品</el-button>
                                    </div>
                                    </template>
                                </el-table-column>
                
                                <el-table-column label="规格" prop="frequency" width="80">
                                    <template #default="{ row }">
                                    <div v-show="row.add">
                                        {{ row.spec }}
                                    </div>
                                    </template>
                                </el-table-column>
                                
                                <el-table-column label="有效期至" prop="frequency" width="100">
                                    <template #default="{ row }">
                                    <div v-show="row.add && row.id">
                                        {{ row.expiryDate?method.convertDate(row.expiryDate):'无' }}
                                    </div>
                                    </template>
                                </el-table-column>

                                <el-table-column label="总量" prop="number" width="250">
                                    <template #default="{ row }">
                                    <div v-show="row.add" style="height: 100%; display: flex; align-items: center;">
                                        <el-input v-model="row.countNumber" @change="handle.countNumberChange(row)"  style="height: 60%; width: 30%; vertical-align: top;" />
                                        <el-autocomplete
                                        v-model="row.stockNumberUnit"
                                        :fetch-suggestions="handle.queryUnitSearch"
                                        @select="handle.countNumberUnitNameClick(row, unit, index)"
                                        value-key="name"
                                        clearable
                                        class="inline-input w-50"
                                        style="height: 60%; width: 60%; margin-left: 10px"
                                        />
                                    </div>
                                    </template>
                                </el-table-column>
                
                                <el-table-column label="零售价" prop="date" width="100" >
                                    <template #default="{ row }">
                                    <div v-if="row.add && row.id" v-show="isShowCost" >
                                        {{ 
                                        (row.stockNumberUnit ? '每' + row.stockNumberUnit + ' ' : '') + 
                                        (row.price) 
                                        }} 元
                                    </div>
                                    </template>
                                </el-table-column>
                
                                <el-table-column label="成本" prop="date" width="100" >
                                    <template #default="{ row }">
                                    <div v-if="row.add && row.id" v-show="isShowCost" >
                                        {{ 
                                        (row.stockNumberUnit ? '每' + row.stockNumberUnit + ' ' : '') + 
                                        (row.cost) 
                                        }} 元
                                    </div>
                                    </template>
                                </el-table-column>
                
                                <el-table-column label="总成本" prop="date" width="100">
                                    <template #default="{ row }">
                                    <div v-show="row.add && row.id && isShowCost" >
                                        共 {{ row.totalCost }} 元
                                    </div>
                                    </template>
                                </el-table-column>
                
                                <el-table-column label="库存" prop="date" fixed="right" width="100">
                                    <template #default="{ row }">
                                    <div v-show="row.add && row.id" >
                                        <span v-if="row.stockNumber > 0">
                                            {{ (row.stockNumber || 0) + ' ' + (row.stockNumberUnit != undefined ? row.stockNumberUnit : '个') }}
                                        </span>
                                        <el-text v-else-if="row.isStock" class="mx-1" type="danger">
                                            {{ (row.number || 0) + ' ' + (row.takeNumberUnitName != undefined ? row.takeNumberUnitName : '个') }}
                                        </el-text>
                                        <el-text v-else class="mx-1">
                                            无库存
                                        </el-text>
                                    </div>
                                    </template>
                                </el-table-column>
                
                                <el-table-column label="操作" fixed="right" width="200px">
                                    <template #default="{ row }">
                                    <el-button v-if="row.add" @click="handle.deleteRowClick" type="danger" style="height: 65px; width: 100px;">
                                        删除
                                    </el-button>
                                    </template>
                                </el-table-column>
                                <el-table-column label="备注" prop="date">
                                    <template #default="{ row }">
                                        <div v-show="row.add && row.id">
                                            <el-input v-model="row.remark" style="vertical-align: top; width: 80px !important;" class="search_input" />
                                        </div>
                                        <div v-show="row.add && (!row.edit)">{{ row.remark }}</div>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </el-form>
                </el-scrollbar>
            
                <div style="height: 20%; display: flex; flex-direction: column; justify-content: center;">
                    <div style="text-align: right; margin: 5px 25% 3% 0px;">
                        <span v-show="isShowCost" style="margin-right: 10px;">
                            成本：{{ form.totalCost || 0 }}
                            <span>元</span>
                        </span>
                        <el-popover
                            placement="top-start"
                            title="显示进货价"
                            :width="300"
                            trigger="hover"
                            content="隐藏/显示，药品的【进货价格】"
                        >
                        <template #reference>
                            <el-button @click="isShowCost = !isShowCost" link>
                                <el-icon>
                                    <View v-show="isShowCost" />
                                    <Hide v-show="!isShowCost" />
                                </el-icon>
                            </el-button>
                        </template>
                        </el-popover>
                        <span style="margin: 0px 10px; font-size: 1.2em;">总售价</span>
                        <el-input-number v-model="form.totalPrice" :min="0" style="height: 100%; width: 200px;" />
                        <span style="margin-left: 10px; font-size: 1.2em;">元</span>
                    </div>
                    <div style="text-align: center">
                        <el-button @click="handle.onSubmit" type="primary" style="width: 200px; height: 50px;" >
                            保存
                        </el-button>
                    </div>
                </div>
            </div>
            <el-result
                v-else
                icon="success"
                title="记录保存成功！"
            >
                <template #extra>
                    <el-button @click="handle.continueClick" type="primary">继续添加</el-button>
                </template>
            </el-result>
        </el-card>
    </div>
</template>


<script setup>
import { ref, reactive } from 'vue'
import { stockService, retailService } from '@/api/clinic/index.js'
import { prescriptionService as service } from '@/api/clinic/index.js'
import { ElNotification } from 'element-plus'

import { Plus } from '@element-plus/icons-vue'


const form = reactive({
    idCardType: 0,
})
const formRef = ref()

const tableData = ref([ { add: true }, { add: false }])

const isNotSubmit = ref(true)
const isShowCost = ref(false)

const handle = {
    continueClick() {
        isNotSubmit.value = true
    },
    addDrugClick(row) {
        row.add = true
        tableData.value.push({ add: false })
    },
    deleteRowClick(index) {
        tableData.value.splice(index, 1)
        method.computeTotalCost()
    },
    queryDrugNameSearch(queryString, cb) { method.searchDrugByName(queryString, (drugs)=> cb(drugs)) },
    drugNameSelected($event, row) {
        Object.assign(row, $event)
        row.cost = $event.cost
        row.cost = row.cost
        row.periodUnit = 1
        method.countRowTotalNumber(row, tableData.value, form)
        method.computeTotalCost()
        method.computeTotalPrice()
    },
    countNumberChange(row) { 
        method.computeRowTotalCost(row)
        method.countTotalNumber(tableData.value, form)
        method.computeTotalPrice()
    },
    countNumberUnitNameClick(row, unit, index) {
        row.countNumberUnitIdx = index
        row.countNumberUnit = unit
        this.countNumberChange(row)
        method.computeTotalPrice()
    },
    onSubmit() {
        let drugList = []
        for (let index = 0; index < tableData.value.length; index++) {
            let row = tableData.value[index];
            if(row.add == false) {
                continue
            }
            if(row.name == null && row.name == '') {
                ElNotification({ title: '无法提交', message: '必须【填写名称】或【选择药品】', type: 'error', })
                return
            }
            if(row.stockNumberUnit == null || row.stockNumberUnit == ''  || row.countNumber == null || row.countNumber == '') {
                ElNotification({ title: '无法提交', message: '必须填写【总量】与【总量单位】', type: 'error', })
                return
            }
            drugList.push({
                name: row.name,
                stockBatchId: row.id,
                number: row.countNumber,
                unit: row.countNumberUnitName,
                price: row.cost,
                remark: row.remark,
                isStock: row.isStock
            })
        }

        if(drugList.length < 1) {
            ElNotification({ title: '无法提交', message: '未填写零售药品', type: 'error', })
            return
        }

        retailService.add({
            ...form,
            drugList,
        }, () => {
            isNotSubmit.value = false
        })
    },
    queryUnitSearch(queryString, cb) { service.searchUnit({ name: queryString != null && queryString != 'null' ? queryString : undefined, }, response => cb(response)) },
}

const method = {
    computeTotalCost() {
        let cost = 0
        tableData.value.forEach(row => {
            if(row && row.cost) cost += row.cost
        })
        form.totalCost = Math.floor(cost * 100) / 100
    },
    searchDrugByName(queryString, callback) {
        service.searchDrug({ name: queryString, current: 1, size: 10 }, records => {
            let drugs = []
            records.forEach(item => {
                if(!item.batchList==undefined&&item.batchList.length > 0){
                    item.batchList.forEach(batch => {
                        batch.countNumber = 1
                        batch.name = item.name
                        batch.period = 1
                        batch.periodUnitName = '天'
                        batch.is
                        drugs.push(batch)
                    });
                }
            });
            drugs = records
            callback(drugs)
        })
    },
    countRowTotalNumber(row, tableData, form) {
        if(row.add) {
            // 总量(最小单位) = 单次剂量 * 每天次数 * 天数
            row.minUnitCountNumber = 1  // 总量
            row.minUnitCountNumberUnitName = row.countNumberUnitName = row.takeNumberUnitName // 总量单位名称
            row.minUnitCountNumberUnitIdx = row.countNumberUnitIdx = 0  // 总量单位下标
            row.totalCost = (row.cost * row.minUnitCountNumber).toFixed(2) // 药品成本
            // 统计总成本
            this.countTotalNumber(tableData, form)
        }
    },
    countTotalNumber(tableData, form) {
        // 统计单个药品总成本
        let total = parseFloat(0)
        tableData.forEach(item => {
            if(item.add) total += parseFloat(item.totalCost)
        });
        form.totalCost = (total).toFixed(2)
        return true
    },
    computeRowTotalCost(row) {
        let minUnitCountNumber = row.countNumber
        for (let index = row.countNumberUnitIdx - 1; index >= 0; index--) {
            let unit = row.stockUnitList[index];
            minUnitCountNumber = minUnitCountNumber * unit.stepSize
        }
        row.totalCost = (minUnitCountNumber * row.cost).toFixed(2)
        return true
    },
    convertDate(cellValue){   //定义日期格式转换函数
        let date = new Date(cellValue)
        let year = date.getFullYear();
        let month = String(date.getMonth() + 1).padStart(2, '0');
        let day = String(date.getDate()).padStart(2, '0');
        let dateString = `${year}/${month}/${day}`;
        return dateString  //拼接成yyyy-mm-dd形式的字符串
    },
    computeTotalPrice() {
        let totalPrice = 0
        tableData.value.forEach(row => {
            if(row.id) {
                let rowPrice = row.price * row.countNumber
                if(row.isStock) {
                    let array = row.units
                    for (let index = 0; index < array.length; index++) {
                        const item = array[index];
                        if(index < row.countNumberUnitIdx) {
                            rowPrice = rowPrice * item.stepSize
                        }
                    }
                }
                totalPrice += rowPrice
            }
        });
        form.totalPrice = totalPrice
    },
}

const rules = reactive({
    totalPrice: [
        { required: true, message: '必须填写售卖价格', trigger: 'blur' },
    ],
})
</script>
<style scoped>
:deep(.el-card__body) {
    height: 100%;
}
:deep(.el-form-item__label) {
    height: 5vh;
    margin-left: 10px;
}
:deep(.el-input) {
    height: 100%;
    font-size: 1.2em;
}
:deep(.el-textarea__inner) {
    font-size: 1.2em;
}
:deep(.el-select__wrapper) {
    height: 100%;
}
</style>