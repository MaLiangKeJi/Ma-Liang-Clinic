<template>
    <div style="height: 100%; padding: 2%;">
        <el-form ref="formRef" :model="form" style="height: 10%; display: flex; justify-content: space-between;">
            <div style="width: 10%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
                <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">待接诊</el-text>
            </div>
            <div style="width: 90%; height: 100%; display: flex; align-items: center; justify-content: flex-end;">
                <el-form-item style="width: 20%;">
                    <el-input style="width: 100%;" class="search_input" v-model="form.val" placeholder="姓名/电话" />
                </el-form-item>
                <el-form-item style="width: 10%;">
                    <el-text class="mx-1" style="width: 100%; height: 100%; text-align: right; font-size: 1vw; padding-right: 10%;">就诊日期</el-text>
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-date-picker
                        prop="createTime" 
                        v-model="form.createTime"
                        value-format="YYYY-MM-DD"
                        type="date"
                        style="width: 60%;"
                        class="search_input"
                        @change="method.search"
                    />
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-button class="search_button" @click="handle.resetClick" style="width: 10vw; border-radius: 5px; height: 6vh !important;"  type="primary" plain>
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
                <el-table-column align="center" type="index" label="序号" width="100" />
                <el-table-column align="center" prop="createTime" label="就诊日期">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ convertDate(row.createTime) }}</span>
                   </template>
                </el-table-column>
                <el-table-column align="center" prop="name" label="姓名">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ row.name }}</span>
                   </template>
                </el-table-column>
                <el-table-column align="center" prop="sex" label="性别" :formatter="convertSex" />
                <el-table-column align="center" prop="age" label="年龄" :formatter="convertAge" />
                <el-table-column align="center" prop="address" label="住址" :formatter="convertAddr" />
                <el-table-column align="center" prop="phone" label="手机号" />
                <el-table-column align="center" label="就诊状态">
                    <template #default="{ row }">
                        <el-text class="mx-1" type="danger" style="font-weight: 700; font-size: 1.2em;">{{ state(row) }}</el-text>
                    </template>
                </el-table-column>
                <el-table-column align="right" fixed="right" width="200">
                    <template #default="{ $index, row }">
                        <div style="display: flex;">
                            <el-popover
                                placement="top-start"
                                :width="300"
                                trigger="hover"
                            >
                                <template #default>
                                    <div style="display: flex; align-items: center;">
                                        <el-icon color="#8cc63f" style="font-size: 2em; border: 2px solid #8cc63f; border-radius: 25px; padding: 5px;">
                                            <ArrowRightBold />
                                        </el-icon>
                                        <span style="margin-left: 20px; font-size: 1.5em;">开始接诊</span>
                                    </div>
                                </template>
                                <template #reference>
                                    <el-button 
                                        @click="handle.admissionsEdit(row)"
                                        :color="row.color == undefined ? '' : row.color" 
                                        circle
                                        style="border: 2px solid #8cc63f; font-size: 20px; width: 36px; height: 36px; margin-right: 0.5vw;"
                                    >
                                        <el-icon color="#8cc63f">
                                            <ArrowRightBold />
                                        </el-icon>
                                    </el-button>
                                </template>
                            </el-popover>
                            <el-popover
                                placement="top-start"
                                :width="300"
                                trigger="hover"
                            >
                                <template #default>
                                    <div style="display: flex; align-items: center;">
                                        <el-icon color="#f57575" style="font-size: 2em;">
                                            <DeleteFilled />
                                        </el-icon>
                                        <span style="margin-left: 20px; font-size: 1.5em;">取消该病人的待接诊</span>
                                    </div>
                                </template>
                                <template #reference>
                                    <el-button 
                                        @click="handle.delete(row)"
                                        :color="row.color == undefined ? '' : row.color" 
                                        circle
                                        style="border: 2px solid #f57575; font-size: 20px; width: 36px; height: 36px;"
                                    >
                                        <el-icon color="#f57575">
                                            <DeleteFilled />
                                        </el-icon>
                                    </el-button>
                                </template>
                            </el-popover>
                        </div>
                        
                    </template>
                </el-table-column>
                <template #empty>
                    <el-empty>
                        <template #description>
                            <div>没有待接诊的患者</div>
                            <el-button type="success" @click="routerInstance.push({ path: '/clinic/doctor/patient/add' })">开始接诊</el-button>
                        </template>
                    </el-empty>
                </template>
            </el-table>
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
import { ref, reactive, onMounted , watch} from 'vue'
import { useRouter } from 'vue-router'
const routerInstance = useRouter()

import admissionService from '@/api/clinic/admission.api.js'

const form = reactive({val:null,createTime:format(new Date())})
const tableData = ref([])
const formRef = ref()

// 分页组件参数
const current = ref(1)
const size = ref(10)
const total = ref(0)

watch(
    () => form.val,
    () => {
        method.search()
    }
)

const state = ({ state,payId,pay, prescriptionId, dossierId }) => {
    let result = '已完成'
    if(state == 1) {
        result = '正在接诊'
    }
    if(payId) {
        if(pay.state == 0){
            result = result+'：未支付'
        }
    } else {
        if(prescriptionId) {
            result = result+'：未支付'
        } else {
            if(dossierId) {
                result = result+'：未开处方'
            } else {
                result = result+'：未建病例'
            }
        }
    }
    return result
}

const handle = {
    admissionsEdit(row) {
        let routeData = routerInstance.resolve({
            path: `/clinic/cure/` + row.id,
            query: { 
                admissionLogId: row.id,
                title: row.name,
                index: row.id
            }
        });
        window.open(routeData.href, "_blank");
    },
    resetClick() {
        formRef.value.resetFields()
        form.createTime = format(new Date())
        form.val = null
        method.search()
    },
    pageCurrentChange() {
        method.search()
    },
    delete(row) {
        let param = { 
            id: row.id,
            ...method.getPage(), 
            value: form.val, 
            createTime: form.createTime,
            state: 1
        }
        admissionService.delete(param, (response) => {
            tableData.value = response.records
            method.setPage(response)
        })
    },
}

const method = {
    setPage(page) {
        current.value = parseInt(page.current)
        total.value = parseInt(page.total)
    },
    getPage() {
        return { 
            current: current.value, 
            size: size.value 
        }
    },
    search() {//1是正在接诊
        admissionService.search({ ...this.getPage(), value: form.val, createTime: form.createTime,state:1}, (response) => {
            tableData.value = response.records
            this.setPage(response)
        })
    },
    formatDateTime(row, column, cellValue, index) {
        // value-format="YYYY-MM-DD" 
        
        row.createTime
        return row.createTimeStr.substring(10);
    },

    convertSex(row, column, cellValue, index) {
        return cellValue == 0 ? '女' : '男'
    },
    convertAge(row, column, cellValue, index) {
        return cellValue + '岁'
    },
    convertAddr(row, column, cellValue, index) {
        return cellValue ? cellValue : '无'
    },
    convertIsFirst(row, column, cellValue, index) {
        return cellValue == 0 ? '初诊' : '复诊'
    },
    convertDiagnosis(row, column, cellValue, index) {
        return cellValue ? cellValue : '无'
    },
}

function format(dateStr) {
    let date = new Date(dateStr)
    let year = date.getFullYear();
    let month = String(date.getMonth() + 1).padStart(2, '0');
    let day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}
onMounted(() => {
    method.search()
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
</style>