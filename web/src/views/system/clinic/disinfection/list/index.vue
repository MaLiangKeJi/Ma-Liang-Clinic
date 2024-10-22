<template>
    <div style="height: 100vh; padding: 2%;">
        <el-form ref="formRef" :model="form" style="height: 10%; display: flex; justify-content: space-between;">
            <div style="width: 10%; height: 100%; padding-left: 20px; text-align: center; display: flex; align-items: center;">
                <el-text class="mx-1" style="font-size: 1.5vw; font-weight: 700;">消杀记录</el-text>
            </div>
            <div style="width: 90%; height: 100%; display: flex; align-items: center; justify-content: flex-end;">
                <el-form-item style="width: 10%;">
                    <el-text class="mx-1" style="width: 100%; height: 100%; text-align: right; font-size: 1vw; padding-right: 10%;">消杀日期</el-text>
                </el-form-item>
                <el-form-item style="width: 20%;">
                    <el-date-picker
                        prop="createTime" 
                        v-model="form.createTime"
                        value-format="YYYY-MM-DD"
                        type="date"
                        style="width: 60%;"
                        class="search_input"
                        @change="() => method.search()"
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
                <el-table-column align="center" prop="disinfectionTime" label="消杀日期">
                    <template #default="{ row }">
                        <span style="font-weight: 700;">{{ method.dateFormat(row.disinfectionTime) }}</span>
                   </template>
                </el-table-column>
                <el-table-column align="center" prop="content" label="消杀内容" :formatter="method.arrayFormat" />
                <el-table-column align="center" prop="items" label="消杀药品、器械" :formatter="method.arrayFormat" />
                <el-table-column align="center" prop="timeRange" label="消杀时间（营业时间）" />
                <el-table-column align="center" prop="spot" label="消杀地点" :formatter="method.arrayFormat" />
                <el-table-column align="center" prop="executor" label="消毒人" />
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
import { ref, defineProps, onMounted, reactive } from 'vue'
import { disinfectionService as service } from '@/api/clinic/index.js'

defineProps(['params'])


const formRef = ref()

const form = reactive({})
const tableData = ref([])

// 分页组件参数
const current = ref(1)
const size = ref(10)
const total = ref(0)

const handle = {
    pageCurrentChange() {
        method.search()
    },
    resetClick() {
        formRef.value.resetFields()
        form.createTime = null
        method.search()
    },
}

const method = {
    setPage(page) {
        current.value =parseInt(page.current)
        total.value = parseInt(page.total)
    },
    getPage() {
        return { 
            current: current.value, 
            size: size.value 
        }
    },
    search() {
        service.search({ ...this.getPage(), createTime: form.createTime}, (response) => {
            tableData.value = response.records
            this.setPage(response)
        })
    },
    dateFormat(cellValue) {
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