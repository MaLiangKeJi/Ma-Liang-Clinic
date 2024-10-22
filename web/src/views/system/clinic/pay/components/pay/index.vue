<template>
    <div style="height: 100%; padding: 2%;">
        <el-form ref="formRef" :model="form" style="height: 10%; display: flex; justify-content: space-between;">
            <div style="width: 14%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
                <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">收费列表</el-text>
                <el-popover placement="top-start" title="Tip：扫码打开药房处方" trigger="hover" content="点我打开二维码" width='200'>
                    <template #reference>
                        <el-button @click="handle.openDrugPhoneDialog" circle
                            style="width: 50px; height: 50px; font-size: 25px; margin-left: 30px; border-color: #605bff">
                            <el-icon size="36px" color="#605bff">
                                <FullScreen />
                            </el-icon>
                        </el-button>
                    </template>
                </el-popover>
            </div>
            <div style="width: 90%; height: 100%; display: flex; align-items: center; justify-content: flex-end;">
                <el-form-item style="width: 10%;">
                    <el-text class="mx-1" style="width: 100%; height: 100%; text-align: right; font-size: 1vw; padding-right: 10%;">患者姓名</el-text>
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-input style="width: 100%;" class="search_input" v-model="form.val" placeholder="姓名/电话" 
                        @keyup.enter.native="handle.search"
                        @change="handle.search"
                    />
                </el-form-item>
                <el-form-item style="width: 10%;">
                    <el-text class="mx-1" style="width: 100%; height: 100%; text-align: right; font-size: 1vw; padding-right: 10%;">就诊日期</el-text>
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-date-picker
                        prop="startDate" 
                        v-model="form.startDate"
                        type="date"
                        style="width: 60%;"
                        class="search_input"
                        @keyup.enter.native="handle.search"
                        @change="handle.search"
                    />
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-button class="search_button" @click="() => handle.resetForm(formRef)" style="width: 10vw; border-radius: 5px; height: 6vh !important;"  type="primary" plain>
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
            >
                <el-table-column align="center" prop="dossierTime" label="收费时间" sortable width="250">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ row.dossierTime }}</span>
                   </template>
                </el-table-column>
                <el-table-column align="center" prop="name" label="姓名">
                    <template #default="{ row }">
                            <span style="font-weight: 700;">{{ row.name }}</span>
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="sex" label="性别" :filter-method="handle.filterSexHandler" :filters="[
                    { text: '男', value: '1' },
                    { text: '女', value: '0' },
                ]">
                    <template #default="{ row }">
                        {{ row.sex == 1 ? '男' : '女' }}
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="age" label="年龄" sortable />
                <el-table-column align="center" prop="phone" label="手机号">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ row.phone }}</span>
                    </template>
                </el-table-column>
                <!-- <el-table-column align="center" prop="address" label="地址" /> -->
                <el-table-column align="center" prop="way" label="收费方式"/>
                <el-table-column align="center" prop="fee" label="费用（元）" sortable fixed="right">
                    <template #default="{ row }">
                        <el-text class="mx-1" type="success" style="font-size: 2em; font-weight: 700;">{{ row.fee }}</el-text>
                    </template>
                </el-table-column>
                <el-table-column fixed="right">
                    <template #default="{ row }">
                        <el-button type="primary" size="small" plain @click="openPrescriptDialog(row)">查看详情</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination
                v-model:current-page="current"
                v-model:page-size="size"
                v-model:total="total"
                :page-sizes="[10, 20, 50, 100, 500]"
                @current-change="handle.search"
                @size-change="handle.search"
                style="height: 10%; padding: 0 5%; font-size: 1.2vh"
                background
                layout="total, sizes, ->, prev, pager, next, jumper"
                size="large"
            />
        </el-card>


        <PrescriptionDialog ref="prescriptionDialog"/>
        <DrugPhoneDialog ref="drugPhoneDialog" />
    </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { payService } from '@/api/clinic/index.js'
import PrescriptionDialog from '../PrescriptionDialog.vue'
import DrugPhoneDialog  from '@/views/system/clinic/cure/dialog/DrugPhoneDialog.vue';



const formRef = ref()

const form = reactive({})
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

const prescriptionDialog = ref(null)
function openPrescriptDialog(row) {
    //调用openDialog方法
    prescriptionDialog.value.openDialog(row)
}


const handle = {
    search: () => {
        let param = {
            ...form,
            current: current.value,
            size: size.value,
            startDate: form.startDate != undefined && form.startDate != null ? form.startDate.getTime() : null,
        }
        payService.searchIsPay(param, (response) => {
            tableData.value = response.records
            setPage(response.current, response.total)
        })
    },
    resetForm: (formEl) => {
        if (!formEl) return
        formEl.resetFields()
        handle.search()
    },
    filterSexHandler(value, row) {
        return value == row.sex
    },
    //打开手机药房弹窗
    openDrugPhoneDialog: () => {
        //调用openDialog方法
        drugPhoneDialog.value.openDialog()
    },
}

const drugPhoneDialog = ref(null)

onMounted(() => {
    handle.search()
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

.date_picker {
    font-size: 1.2vh !important;
}
</style>