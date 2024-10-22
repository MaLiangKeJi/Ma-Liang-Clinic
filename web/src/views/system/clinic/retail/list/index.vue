<template>
    <div style="height: 100vh; padding: 2%;">
        <el-form ref="formRef" :model="form" style="height: 10%; display: flex; justify-content: space-between;">
            <div style="width: 10%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
                <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">零售记录</el-text>
            </div>
            <div style="width: 90%; height: 100%; display: flex; align-items: center; justify-content: flex-end;">
                <el-form-item style="width: 20%;">
                    <el-input style="width: 100%;" class="search_input" v-model="form.val" placeholder="姓名/电话" 
                        @change="handle.search"
                    />
                </el-form-item>
                <el-form-item style="width: 10%;">
                    <el-text class="mx-1" style="width: 100%; height: 100%; text-align: right; font-size: 1vw; padding-right: 10%;">售卖日期</el-text>
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-date-picker
                        v-model="form.dateArr"
                        type="datetimerange"
                        range-separator="到"
                        start-placeholder="开始时间"
                        end-placeholder="结束时间"
                        size="large"
                        class="search_input"
                        @keyup.enter.native="method.search"
                        style="width: 60%;"
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
                ref="tableRef"
                :row-key="row => row.id" 
                :expand-row-keys="expands" 
                height="100%"
                stripe 
                style="height: 90%; width: 100%; font-size: 1.5vh;" 
                :row-style="{ height: '5vh' }" 
                :header-cell-style="{ background:'#6a5acd', color:'#fff' }"
                scrollbar-always-on
                @row-click="unfoldRow"
            >
                <el-table-column align="center" type="expand">
                        <template #default="{ row }">
                            <div style="padding: 20px;">
                                <div style="margin-bottom: 30px;">
                                    <span style="font-size: 1.5em; font-weight: 700;">购买人</span>
                                    <el-descriptions border size="large" :column="4" label-class-name="descriptions_label" class-name="descriptions_label">
                                        <el-descriptions-item label="姓名">{{ row.name }}</el-descriptions-item>
                                        <el-descriptions-item label="性别">{{ row.sex == 1 ? '男' : '女' }}</el-descriptions-item>
                                        <el-descriptions-item label="年龄">{{ row.age }}岁</el-descriptions-item>
                                        <el-descriptions-item label="出生年月">{{ row.birthday ? method.convertDate(row.birthday) : '无' }}</el-descriptions-item>
                                        <el-descriptions-item label="手机">{{ row.phone ? row.phone : '无' }}</el-descriptions-item>
                                        <el-descriptions-item label="身份证" :span="3">{{ row.idCard ? row.idCard : '无' }}</el-descriptions-item>
                                        <el-descriptions-item label="联系人">{{ row.contact ? row.contact : '无' }}</el-descriptions-item>
                                        <el-descriptions-item label="联系人电话" :span="3">{{ row.contactPhone ? row.contactPhone : '无' }}</el-descriptions-item>
                                    </el-descriptions>
                                </div>
                                <span style="font-size: 1.5em; font-weight: 700;">药品信息</span>
                                <ItemView :data="row.retailDrugRecords" />
                            </div>
                        </template>
                </el-table-column>
                <el-table-column align="center" prop="createTime" label="零售日期" width="250">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ method.convertDateTime(row.createTime) }}</span>
                   </template>
                </el-table-column>
                <el-table-column align="center" prop="name" label="购买人" width="200">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ row.name }}</span>
                   </template>
                </el-table-column>
                <el-table-column align="center" prop="date" label="性别" width="100">
                    <template #default="{ row }">
                        {{ row.sex == 1 ? '男' : '女' }}
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="date" label="年龄" width="100">
                    <template #default="{ row }">
                        {{ row.age }}
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="phone" label="手机号">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ row.phone }}</span>
                    </template>
                </el-table-column>
                <el-table-column align="center" prop="fee" label="总售价" width="300" sortable fixed="right">
                    <template #default="{ row }">
                        <div style="width: 100%; display: flex; justify-content: space-around; align-items: center; padding-right: 40px;">
                            <el-text class="mx-1" type="success" style="width: 100%; font-size: 1.5em; font-weight: 700;">
                                {{ row.totalPrice || 0 }}
                                <span>元</span>
                            </el-text>
    
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
                                        <span style="margin-left: 20px; font-size: 1.5em;">详细信息</span>
                                    </div>
                                </template>
                                <template #reference>
                                    <el-button 
                                        @rowClick="unfoldRow"
                                        :color="row.color == undefined ? '' : row.color" 
                                        circle
                                        style="border: 2px solid #8cc63f; font-size: 20px; width: 36px; height: 36px;"
                                    >
                                        <el-icon color="#8cc63f">
                                            <ArrowRightBold />
                                        </el-icon>
                                    </el-button>
                                </template>
                            </el-popover>
                        </div>
                    </template>
                </el-table-column>
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
import { ref, onMounted, reactive } from 'vue'
import { retailService as service } from '@/api/clinic/index.js'
import ItemView from './item.vue'

const tableData = ref([])

// 分页组件参数
const current = ref(1)
const size = ref(10)
const total = ref(0)

const form = reactive({
    val: null,
    dateArr: [],
})
const formRef = ref()


const handle = {
    pageCurrentChange() {
        method.search()
    },
}

const method = {
    resetClick() {
        formRef.value.resetFields()
        form.dateArr = []
        method.search()
    },
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
        let param = {
            ...method.getPage(),
            ...form,
            startDate: form.dateArr.length > 0 ? form.dateArr[0].getTime() : undefined,
            endDate: form.dateArr.length > 0 ? form.dateArr[1].getTime() : undefined,
        }
        param.dateArr = undefined
        service.searchList(param, (response) => {
            method.setPage(response)
            tableData.value = response.records
        })
    },
    dateFormat(row, column, cellValue, index) {
        if(cellValue) {
            let dt = new Date(cellValue)
            let year = dt.getFullYear();
            let month = (dt.getMonth() + 1).toString().padStart(2,'0');
            let date = dt.getDate().toString().padStart(2,'0');
            return `${year}-${month}-${date}`;
        }
        return null;
    },
    arrayFormat(row, column, cellValue, index) {
        let array = JSON.parse(cellValue)
        let content = ""
        for (let index = 0; index < array.length; index++) {
            const item = array[index];
            content += (index == 0 ? item : (', ' + item))
        }
        return content
    },
    convertDate(cellValue){   //定义日期格式转换函数
        let date = new Date(cellValue)
        let year = date.getFullYear();
        let month = String(date.getMonth() + 1).padStart(2, '0');
        let day = String(date.getDate()).padStart(2, '0');
        let dateString = `${year}/${month}/${day}`;
        return dateString  //拼接成yyyy-mm-dd形式的字符串
    },
    convertDateTime(value) {
        let datetime = new Date(value)
        // 获取年月日时分秒值  slice(-2)过滤掉大于10日期前面的0
        let year = datetime.getFullYear(),
        month = ("0" + (datetime.getMonth() + 1)).slice(-2),
        date = ("0" + datetime.getDate()).slice(-2),
        hour = ("0" + datetime.getHours()).slice(-2),
        minute = ("0" + datetime.getMinutes()).slice(-2),
        second = ("0" + datetime.getSeconds()).slice(-2);
        // 拼接
        return year + "-"+ month +"-"+ date +" "+ hour +":"+ minute +":" + second;
    },
}
onMounted(() => {
    method.search()
})

const expands = ref([])
const tableRef = ref()
function unfoldRow(row) {
    let index = expands.value.find(id => id == row.id)
    if(index == undefined) {
        expands.value.push(row.id)
    } else {
        expands.value = expands.value.splice(index, 1)
    }
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